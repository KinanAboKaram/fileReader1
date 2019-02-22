package com.sigma.fileReader.tree;

import java.awt.Color;
import java.awt.Rectangle;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Stack;

import javax.swing.JTree;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class HighLightAndsCrollToPosition {

	ArrayList<String> path;
//	private static final long serialVersionUID = 1L;

	int indexNew = 0;
	Highlighter hilite;

	public void highLightAndsCrollToPosition(JTree tree, JTextComponent textArea, String fileContent) {

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		// Selection listener for the tree
		tree.addTreeSelectionListener(e -> {
//			System.out.println("cascacac");
			highLightAndsCrollToPositionRender(tree, textArea, fileContent);
		});
	}

	public void highLightAndsCrollToPositionRender(JTree tree, JTextComponent textArea, String fileContent) {
//		System.out.println("ffasd");

		// get the path of the component using DefaultMutableTreeNode when the user
		// click on the object
		// to get the index of the selected node
		try {

			if (tree.getSelectionPath() == null) {
				System.out.println("  tree.getSelectionPath() == null ");
				return;
			}

			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getSelectionPath()
					.getLastPathComponent();

			// call method removing color
			if (hilite != null) {
				removeHighlights(textArea);
			}
			try {
				String theSelectedNode = selectedNode.toString();
				if (theSelectedNode != "Root") {

					TreePath treeSelectionPath = tree.getSelectionPath();
					hilite = textArea.getHighlighter();

					String selectedjsonObject = treeSelectionPath.getPathComponent(1).toString();

					String parentNode = selectedNode.getParent().toString();
					String previousNode = selectedNode.getPreviousNode().toString();

					path = new ArrayList<>();

					int theJsonStartTextIndex = 0;
					int theJsonEndTextIndex = 0;
					int ParentIndex = 0;

					int nextNodeIndex = 0;
					int previousNodeIndex = 0;

					theJsonStartTextIndex = fileContent.indexOf(selectedjsonObject);
					previousNodeIndex = fileContent.indexOf(previousNode, theJsonStartTextIndex);
					ParentIndex = fileContent.indexOf(parentNode, theJsonStartTextIndex);
					theJsonEndTextIndex = theJsonStartTextIndex + selectedjsonObject.length();
					if (selectedNode.getNextNode() != null) {
						String theNextNode = selectedNode.getNextNode().toString();
						nextNodeIndex = fileContent.indexOf(theNextNode, theJsonStartTextIndex);

					}

					Stack<Integer> sortedAllJsonIndex = getAllJsonIndex(treeSelectionPath, selectedNode, fileContent,
							selectedjsonObject);

					Stack<Integer> sortedAllPreviousNodeIndex = getAllPreviousNodeIndex(selectedNode, fileContent,
							selectedjsonObject, sortedAllJsonIndex, theJsonEndTextIndex);

					Stack<Integer> sortedAllNextNodeIndex = getAllNextNodeIndex(selectedNode, fileContent,
							selectedjsonObject, sortedAllJsonIndex, theJsonEndTextIndex, theJsonStartTextIndex);

					int theSelectedJson = 0;
					// Get the first index in the text

					if (sortedAllNextNodeIndex.isEmpty() && nextNodeIndex == 0) {
						theSelectedJson = fileContent.indexOf(theSelectedNode.replace("\\r\\n", "\r\n"),
								(theJsonEndTextIndex - selectedNode.toString().length() - 10));
					}
					if (theSelectedJson == 0 || theSelectedJson == -1) {

						theSelectedJson = between(fileContent, selectedNode, selectedjsonObject, theJsonStartTextIndex,
								theJsonEndTextIndex, nextNodeIndex, ParentIndex, previousNodeIndex,
								sortedAllPreviousNodeIndex, sortedAllNextNodeIndex, sortedAllJsonIndex);
						if (theJsonStartTextIndex != -1) {
//								System.out.println("6666666");
							indexNew = theSelectedJson;
						}
					}

					if (theJsonStartTextIndex == -1 || ParentIndex == -1) {
//							System.out.println(indexNew);
						theSelectedJson = indexNew;
					}
//					System.out.println("theJsonStartTextIndex: "+theJsonStartTextIndex);
//						System.out.println("sortedAllJsonIndex " + sortedAllJsonIndex.isEmpty());
//						System.out.println("sortedAllPreviousNodeIndex " + sortedAllPreviousNodeIndex.isEmpty());
					System.out.println("sortedAllNextNodeIndex " + sortedAllNextNodeIndex.isEmpty());
//					System.out.println("theJsonEndTextIndex: " + theJsonEndTextIndex);
//		
//					System.out.println("previousNodeIndex: " + previousNodeIndex);
//					System.out.println("ParentIndex: " + ParentIndex);
//					System.out.println("nextNodeIndex: " + nextNodeIndex);
//////					System.out.println();
//						System.out.println("contain" + (selectedNode.toString().contains("\\r\\n")));
//						System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++,");
//					System.out.println("the json " + theSelectedNode);
					System.out.println("the Selected " + theSelectedJson);
//
//						System.out.println(theSelectedNode.length());
////					System.out.println(theSelectedNode.replace("\\r\\n", ""));
//						System.out.println(fileContent.indexOf(theSelectedNode.replace("\\r\\n", "\r\n"),theJsonEndTextIndex - selectedNode.toString().length()));
//						System.out.println(fileContent.contains(theSelectedNode));
//						System.out.println("theSelectedJson " + theSelectedJson + " length "
//								+ (theSelectedJson + theSelectedNode.length()));

//						System.out.println();
					textArea.getHighlighter();

					try {
						hilite.addHighlight(theSelectedJson, theSelectedJson + theSelectedNode.length(), theHighLight);
//						System.out.println("ssssssdasdasd " + textArea.modelToView(theSelectedJson).toString());

						// Get the rectangle of the where the text would be visible
						Rectangle viewRect = new Rectangle(textArea.modelToView(theSelectedJson));
						// Add the viewRectangle to the scrollRect
						textArea.scrollRectToVisible(viewRect);
						// Move to the selected element in the text
//						System.out.println("length " + (theSelectedJson + theSelectedNode.length()) + " end "
//								+ theJsonEndTextIndex);
						try {
							textArea.moveCaretPosition(theSelectedJson + theSelectedNode.length());
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							return;
						}

						theSelectedJson += theSelectedNode.length();

					} catch (BadLocationException e2) {
						System.out.println("jkjkjkjkjkjkjkjkjkjkjkjkjkjkjkjkjkjkjkjk");
						e2.printStackTrace();
						return;
					}

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (NullPointerException e2) {
			e2.printStackTrace();
		}

//		System.err.println("ggggggggggggggggggggggggggggggg");

	}

	private Stack<Integer> getAllNextNodeIndex(DefaultMutableTreeNode selectedNode, String fileContent,
			String selectedjsonObject, Stack<Integer> sortedAllJsonIndex, int theJsonEndTextIndex,
			int theJsonStartTextIndex) {

		String text = fileContent.replace("\\r\\n", "\r\n");
		if (text.contains("\\n")) {
			System.out.println("sdasddsad");
		}

		ArrayList<Integer> allNextNodeIndex = new ArrayList<>();
		int startJson = sortedAllJsonIndex.get(sortedAllJsonIndex.size() - 1);
		int endJson = selectedjsonObject.length() + startJson;
		int nodeIndex = 0;
		while (selectedNode.getNextNode() != null) {
			selectedNode = selectedNode.getNextNode();
			nodeIndex = text.indexOf(selectedNode.toString().replace("\\r\\n", "\r\n"), startJson);
			startJson = nodeIndex;
			if (nodeIndex != -1) {

				for (Integer integer : allNextNodeIndex) {

					if (integer.equals(nodeIndex)) {
						nodeIndex = text.indexOf(selectedNode.toString().replace("\\r\\n", "\r\n"), nodeIndex + 1);
					}
				}
			}
			allNextNodeIndex.add(nodeIndex);
		}
		removeDuplicateInTheList(allNextNodeIndex);
		Collections.sort(allNextNodeIndex);

		Collections.reverse(allNextNodeIndex);
		Stack<Integer> sortedAllNextNodeIndex = new Stack<>();

		for (Integer integer : allNextNodeIndex) {

			if (integer != -1 && integer >= theJsonStartTextIndex) {
				sortedAllNextNodeIndex.push(integer);
			}
		}
		return sortedAllNextNodeIndex;
	}

	private Stack<Integer> getAllPreviousNodeIndex(DefaultMutableTreeNode selectedNode, String fileContent,
			String selectedjsonObject, Stack<Integer> sortedAllJsonIndex, int theJsonEndTextIndex) {

		ArrayList<Integer> allNodeIndex = new ArrayList<>();
		allNodeIndex.addAll(sortedAllJsonIndex);

		int startJson = allNodeIndex.get(allNodeIndex.size() - 1);
		int endJson = selectedjsonObject.length() + startJson;
		int nodeIndex = 0;
		DefaultMutableTreeNode next = selectedNode.getNextNode();
		DefaultMutableTreeNode select = selectedNode;
		int x = 0;
		while (selectedNode.getPreviousNode() != null) {
			selectedNode = selectedNode.getPreviousNode();
			if (next != null) {
				x = fileContent.indexOf(next.toString().replace("\\r\\n", "\r\n"), nodeIndex);
			}

			if (nodeIndex != -1 && nodeIndex <= x && nodeIndex <= endJson) {
				nodeIndex = fileContent.indexOf(selectedNode.toString().replace("\\r\\n", "\r\n"), 0);
				for (Integer integer : allNodeIndex) {

					if (integer.equals(nodeIndex)) {
						nodeIndex = fileContent.indexOf(selectedNode.toString().replace("\\r\\n", "\r\n"),
								nodeIndex + 1);
//						System.out.println("  "+selectedNode+"  "+nodeIndex);
					}
				}
				allNodeIndex.add(nodeIndex);
				if (select.getPreviousNode().toString().contains(select.toString())) {

					allNodeIndex.add(nodeIndex + selectedNode.toString().length() - 1);
				} else {
					allNodeIndex.add(nodeIndex);
				}
			}
		}
		removeDuplicateInTheList(allNodeIndex);
		Collections.sort(allNodeIndex);

		Stack<Integer> sortedAllPreviousNodeIndex = new Stack<>();

		for (Integer integer : allNodeIndex) {
			if (integer != -1) {
				sortedAllPreviousNodeIndex.push(integer);
			}
		}
		return sortedAllPreviousNodeIndex;
	}

	private Stack<Integer> getAllJsonIndex(TreePath treeSelectionPath, DefaultMutableTreeNode selectedNode,
			String fileContent, String selectedjsonObject) {
		DefaultMutableTreeNode previousSibling = (DefaultMutableTreeNode) treeSelectionPath.getPathComponent(1);

		ArrayList<Integer> allPreviousSiblingIndex = new ArrayList<>();
		int previousSiblingIndex = 0;
		if (allPreviousSiblingIndex.isEmpty()) {
			previousSiblingIndex = fileContent.indexOf(previousSibling.toString().replace("\\r\\n", "\r\n"));
			allPreviousSiblingIndex.add(previousSiblingIndex);
		}
		while (previousSibling.getPreviousSibling() != null) {
			previousSibling = previousSibling.getPreviousSibling();
			if (previousSiblingIndex != -1) {
				previousSiblingIndex = fileContent.indexOf(previousSibling.toString().replace("\\r\\n", "\r\n"));

				for (Integer integer : allPreviousSiblingIndex) {
					if (integer.equals(previousSiblingIndex)) {

						previousSiblingIndex = fileContent.indexOf(previousSibling.toString().replace("\\r\\n", "\r\n"),
								previousSiblingIndex + 1);
					}
				}
				allPreviousSiblingIndex.add(previousSiblingIndex);
			}
		}
		removeDuplicateInTheList(allPreviousSiblingIndex);
		Collections.sort(allPreviousSiblingIndex);

		Stack<Integer> sortedAllPreviousSiblingIndex = new Stack<>();

		for (Integer integer : allPreviousSiblingIndex) {
			sortedAllPreviousSiblingIndex.push(integer);
		}

		return sortedAllPreviousSiblingIndex;
	}

	public int between(String fileText, DefaultMutableTreeNode selectedNode, String selectedjsonObject,
			int theJsonStartTextIndex, int theJsonEndTextIndex, int nextNodeIndex, int ParentIndex,
			int previousNodeIndex, Stack<Integer> allPreviousIndex, Stack<Integer> allNextNodeIndex,
			Stack<Integer> allJsonIndex) {

		String match = selectedNode.toString().replace("\\r\\n", "\r\n");
		int previousIndex = previousNodeIndex;
		String text = fileText.replace("\\r\\n", "\r\n");

		int nextNode = nextNodeIndex;
		int indexOfmatch = 0;
		int startJson = 0;
		try {
			if (!allJsonIndex.isEmpty()) {
				startJson = allJsonIndex.pop();
			}
		} catch (NullPointerException e) {
			startJson = 0;
			e.printStackTrace();
		}

		int endJson = 0;
		for (; theJsonStartTextIndex < text.length() + 1; theJsonStartTextIndex++) {
			if (theJsonStartTextIndex >= indexOfmatch && indexOfmatch >= -1 && indexOfmatch <= theJsonEndTextIndex
					&& theJsonStartTextIndex >= ParentIndex) {
				endJson = selectedjsonObject.length() + startJson;
				boolean x = true;
				try {
					do {
						if (!allPreviousIndex.isEmpty()) {
							previousIndex = allPreviousIndex.pop();
						}
						if ((previousIndex >= startJson && previousIndex <= endJson) || allPreviousIndex.isEmpty()) {
							x = false;
						}

					} while (x);
					if (!allNextNodeIndex.isEmpty()) {
						nextNode = allNextNodeIndex.pop();
					} else if (nextNode == 0 || nextNodeIndex == 0) {
						nextNode = endJson;
					}
					System.out.println("indexOfmatch " + indexOfmatch);
					System.out.println("previousIndex " + previousIndex);
					System.out.println("previousNodeIndex " + previousNodeIndex);
					System.out.println("ParentIndex " + ParentIndex);
					System.out.println("nextNode " + nextNode);
					System.out.println("nextNodeIndex " + nextNodeIndex);
					System.out.println("startJson " + startJson);
					System.out.println("endJson " + endJson);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				theJsonStartTextIndex = startJson;
				if (startJson == -1 && indexOfmatch == -1 && previousNodeIndex == -1 && ParentIndex == -1) {
					System.out.println("return all = -1");
					return indexOfmatch;
				}
				indexOfmatch = text.indexOf(match.replace("\\r\\n", "\r\n"), previousIndex);
				if (startJson <= indexOfmatch) {
//					System.out.println("akbaaar");
					if (startJson == -1 && indexOfmatch == -1) {
						System.out.println("return -1 ");
						return indexOfmatch;
					} else if (indexOfmatch != -1 && indexOfmatch < endJson) {

						if (indexOfmatch <= theJsonEndTextIndex && previousNodeIndex <= indexOfmatch) {

							if (startJson <= indexOfmatch && indexOfmatch <= nextNode
									&& indexOfmatch <= theJsonEndTextIndex) {
								System.out.println("ppppppp");
								return indexOfmatch;
							} else if (indexOfmatch < nextNode) {
								return indexOfmatch;
							}
//							else if (indexOfmatch < theJsonEndTextIndex && previousNodeIndex >= nextNode) {
//								if (theJsonStartTextIndex <= previousNodeIndex && theJsonStartTextIndex <= nextNode) {
//									System.out.println("as");
//									return indexOfmatch;
//								}
//								return indexOfmatch;
//							} 
							else {
//								System.out.println("indexOfmatch "+indexOfmatch);
//								System.out.println("nextNode "+nextNode);
//								System.out.println("previousIndex "+previousIndex);
//								System.out.println("ParentIndex "+ParentIndex);
//								System.out.println("previousNodeIndex "+previousNodeIndex);
//								System.out.println("nextNodeIndex "+nextNodeIndex);
//								System.out.println("startJson "+startJson);
//								System.out.println("endJson "+endJson);
								do {

									if (allNextNodeIndex.isEmpty()
											|| indexOfmatch > nextNode && previousIndex < nextNode) {

										indexOfmatch = text.indexOf(match, previousIndex);
										System.out.println("aaaaaaasdasdassd");
										return indexOfmatch;
									} else if (previousNodeIndex < nextNode && previousIndex > nextNode
											&& previousIndex < nextNodeIndex) {
										System.out.println("ooooooooooooooooo");
										indexOfmatch = text.indexOf(match, previousNodeIndex);
										return indexOfmatch;
									}

									nextNode = allNextNodeIndex.pop();
								} while (indexOfmatch > nextNode);
//								if (indexOfmatch >= nextNode && indexOfmatch >= ParentIndex) {
//									System.out.println("gggggggggggg");
//									indexOfmatch = text.indexOf(match, indexOfmatch+1);
//								}
								System.err.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaahashas");
								return indexOfmatch;
							}

						} else if (theJsonStartTextIndex >= theJsonEndTextIndex && theJsonStartTextIndex >= nextNode
								&& theJsonStartTextIndex >= previousNodeIndex) {
//		
							if (indexOfmatch > nextNode && indexOfmatch >= nextNode && startJson >= indexOfmatch
									&& startJson >= nextNode && startJson <= theJsonEndTextIndex) {
							}
						} else if (indexOfmatch <= nextNode && startJson <= indexOfmatch && startJson <= nextNode) {
							System.out.println("ffasd");
							return indexOfmatch;
						} else {
							if (indexOfmatch <= previousNodeIndex) {
								System.out.println("return  sads");
								indexOfmatch = text.indexOf(match, previousNodeIndex);
								indexOfmatch = text.indexOf(match, indexOfmatch);
								return indexOfmatch;
							}
						}
					} else if (indexOfmatch >= previousNodeIndex && indexOfmatch >= endJson) {
						try {
							do {
								try {

									indexOfmatch = text.indexOf(match, startJson);

									if (previousIndex <= previousNodeIndex && indexOfmatch > startJson) {

										if (indexOfmatch > nextNode) {
											do {
												try {
													if (allNextNodeIndex.isEmpty() || indexOfmatch > nextNode) {

														indexOfmatch = text.indexOf(match, previousIndex);
														return indexOfmatch;
													}
													nextNode = allNextNodeIndex.pop();
												} catch (Exception e) {
													e.printStackTrace();
												}
											} while (indexOfmatch > nextNode);
										}
										return indexOfmatch;

									} else if (indexOfmatch <= previousIndex) {
									}

								} catch (NullPointerException e) {
									e.printStackTrace();
									return indexOfmatch;
								}
								if (startJson == -1 && previousNodeIndex == -1) {
									return indexOfmatch;
								}
								if (indexOfmatch > endJson && indexOfmatch > previousNodeIndex
										&& indexOfmatch > previousIndex && indexOfmatch > startJson) {
									System.out.println("fff");
									return indexOfmatch;
								} else if (previousNodeIndex > endJson && previousNodeIndex > endJson) {
									return indexOfmatch;
								}
							} while (indexOfmatch > endJson && indexOfmatch > previousNodeIndex
									&& indexOfmatch > previousIndex);
						} catch (StackOverflowError e) {
							e.printStackTrace();
							return indexOfmatch;
						}
					} else if (nextNode == -1) {
						return indexOfmatch;
					}
				} else if (indexOfmatch == 0) {
					return indexOfmatch;
				} else if (indexOfmatch == -1
						|| (allPreviousIndex.isEmpty() || allNextNodeIndex.isEmpty() || !allJsonIndex.isEmpty())) {
					indexOfmatch = text.indexOf(match, previousIndex);
					if (match.contains("\\r\\n") || indexOfmatch == -1 || match.contains("\\n")) {
						indexOfmatch = text.indexOf(match.replace("\\r\\n", "\r\n"), startJson);
						if (match.contains("\\n")) {
							indexOfmatch = text.indexOf(match.replace("\\n", "\n"), startJson);
						}
						if (indexOfmatch == -1) {
							System.out.println("return asxddddsa");
//							return between(fileText, selectedNode, selectedjsonObject, theJsonStartTextIndex,
//									theJsonEndTextIndex, nextNode, ParentIndex, previousNodeIndex, allPreviousIndex,
//									allNextNodeIndex, allJsonIndex);
						}
						return indexOfmatch;
					}
					return indexOfmatch;
				}
			}
		}
		System.out.println("last");
		return indexOfmatch;
	}

	// create the LightYellow color from the HSB colors.
	private static TheHhighLight theHighLight = new TheHhighLight(Color.getHSBColor(238, 34, 44));

	// method for remove the old color
	public static void removeHighlights(JTextComponent textArea) {

		Highlighter hilite = textArea.getHighlighter();

		Highlighter.Highlight[] highlights = hilite.getHighlights();

		for (int i = 0; i < highlights.length; i++) {
			if (highlights[i].getPainter() instanceof TheHhighLight) {
				hilite.removeHighlight(highlights[i]);
			}
		}
	}

	public static <T> void removeDuplicateInTheList(ArrayList<T> list) {
		HashSet<T> h = new HashSet<T>(list);
		list.clear();
		list.addAll(h);
	}

//	  public int between(String fileText, DefaultMutableTreeNode selectedNode, String selectedjsonObject,
//				int theJsonStartTextIndex, int theJsonEndTextIndex, int nextNodeIndex, int ParentIndex,
//				int previousNodeIndex, Stack<Integer> allPreviousIndex, Stack<Integer> allNextNodeIndex,
//				Stack<Integer> allJsonIndex) {
//
//			String match = selectedNode.toString().replace("\\r\\n", "\r\n");
//			int previousIndex = previousNodeIndex;
//			String text = fileText.replace("\\r\\n", "\r\n");
//
//			int nextNode = nextNodeIndex;
//			int indexOfmatch = 0;
//			int startJson = 0;
//			try {
//				if (!allJsonIndex.isEmpty()) {
//					startJson = allJsonIndex.pop();
//				}
//			} catch (NullPointerException e) {
//				startJson = 0;
//				e.printStackTrace();
//			}
//
//			int endJson = 0;
//			for (; theJsonStartTextIndex < text.length() + 1; theJsonStartTextIndex++) {
//				if (theJsonStartTextIndex >= indexOfmatch && indexOfmatch >= -1 && indexOfmatch <= theJsonEndTextIndex
//						) {
//					endJson = selectedjsonObject.length() + startJson;
//					boolean x = true;
//					try {
//						do {
//							if (!allPreviousIndex.isEmpty()) {
//								previousIndex = allPreviousIndex.pop();
//							}
//							if ((previousIndex >= startJson && previousIndex <= endJson) || allPreviousIndex.isEmpty()) {
//								x = false;
//							}
//
//						} while (x);
//						if (!allNextNodeIndex.isEmpty()) {
//							nextNode = allNextNodeIndex.pop();
//						} else if (nextNode == 0 || nextNodeIndex == 0) {
//							nextNode = endJson;
//						}
////						System.out.println("indexOfmatch "+indexOfmatch);
////						System.out.println("nextNode "+nextNode);
////						System.out.println("previousIndex "+previousIndex);
////						System.out.println("ParentIndex "+ParentIndex);
////						System.out.println("previousNodeIndex "+previousNodeIndex);
////						System.out.println("nextNodeIndex "+nextNodeIndex);
////						System.out.println("startJson "+startJson);
////						System.out.println("endJson "+endJson);
//					} catch (NullPointerException e) {
//						e.printStackTrace();
//					}
//					theJsonStartTextIndex = startJson;
//					if ((startJson == -1 && indexOfmatch == -1 && previousNodeIndex == -1 && ParentIndex == -1)||selectedNode.getPreviousNode().toString().equals("Root")) {
//						System.out.println("return all = -1");
//						return indexOfmatch;
//					}
//					indexOfmatch = text.indexOf(match.replace("\\r\\n", "\r\n"), (previousIndex + match.length()));
//					if (startJson <= indexOfmatch) {
////						System.out.println("akbaaar");
//						if ((startJson == -1 && indexOfmatch == -1)||(ParentIndex==-1&&previousNodeIndex==-1||previousIndex==-1 &&previousNodeIndex==-1)) {
//							System.out.println("return -1 ");
//							return indexOfmatch;
//						} else if (indexOfmatch != -1 && indexOfmatch < endJson) {
//
//							if (indexOfmatch <= theJsonEndTextIndex && previousNodeIndex <= indexOfmatch) {
//
//								if (startJson <= indexOfmatch && indexOfmatch <= nextNode
//										&& indexOfmatch <= theJsonEndTextIndex) {
//									System.out.println("pppphhppp");
//
//									return indexOfmatch;
//
//								} else if (indexOfmatch < nextNode) {
//									return indexOfmatch;
//								} else if (indexOfmatch < theJsonEndTextIndex && previousNodeIndex >= nextNode) {
//									if (theJsonStartTextIndex <= previousNodeIndex && theJsonStartTextIndex <= nextNode) {
//										System.out.println("as");
//										return indexOfmatch;
//									}
//									return indexOfmatch;
//								} else {
//									System.out.println("indexOfmatch " + indexOfmatch);
//									System.out.println("nextNode " + nextNode);
//									System.out.println("previousIndex " + previousIndex);
//									System.out.println("ParentIndex " + ParentIndex);
//									System.out.println("previousNodeIndex " + previousNodeIndex);
//									System.out.println("nextNodeIndex " + nextNodeIndex);
//									System.out.println("startJson " + startJson);
//									System.out.println("endJson " + endJson);
//									String previous = "";
//									int prevInt = 0;
//									if (selectedNode.getPreviousNode() == null) {
//										prevInt = previousIndex;
//									} else if (selectedNode.getPreviousNode() != null) {
//										previous = selectedNode.getPreviousNode().toString();
//										prevInt = indexOfmatch = text.indexOf(previous.replace("\\r\\n", "\r\n"),
//												previousIndex);
//										if (prevInt == -1) {
//											prevInt = previousIndex;
//										}
//										System.out.println("prevInt " + prevInt);
//									}
//
//									do {
//										if (allNextNodeIndex.isEmpty()
//												|| indexOfmatch <= nextNode && previousIndex < nextNode) {
//
//											indexOfmatch = text.indexOf(match.replace("\\r\\n", "\r\n"), previousIndex);
//											System.out.println("aaaaaaasdasdassd");
//											return indexOfmatch;
//										}
//
//										else if (previousNodeIndex <= nextNode && previousIndex >= nextNode) {
//											if (startJson == ParentIndex) {
//												indexOfmatch = text.indexOf(match.replace("\\r\\n", "\r\n"), startJson);
//												return indexOfmatch;
//											}
//											System.out.println("ooooooooooooooooo");
//											indexOfmatch = text.indexOf(match.replace("\\r\\n", "\r\n"), prevInt);
//											return indexOfmatch;
//										}
//
//										nextNode = allNextNodeIndex.pop();
//									} while (indexOfmatch > nextNode);
//									System.err.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaahashas");
//									return indexOfmatch;
//								}
//
//							} else if (theJsonStartTextIndex >= theJsonEndTextIndex && theJsonStartTextIndex >= nextNode
//									&& theJsonStartTextIndex >= previousNodeIndex) {
////			
//								if (indexOfmatch > nextNode && indexOfmatch >= nextNode && startJson >= indexOfmatch
//										&& startJson >= nextNode && startJson <= theJsonEndTextIndex) {
//								}
//							} else if (indexOfmatch <= nextNode && startJson <= indexOfmatch && startJson <= nextNode) {
//								System.out.println("ffasd");
//								return indexOfmatch;
//							} else {
//								if (indexOfmatch <= previousNodeIndex) {
//									System.out.println("return  sads");
//									indexOfmatch = text.indexOf(match.replace("\\n", "\n"), previousNodeIndex);
//									indexOfmatch = text.indexOf(match.replace("\\n", "\n"), indexOfmatch);
//									return indexOfmatch;
//								}
//							}
//						} else if (indexOfmatch >= previousNodeIndex && indexOfmatch >= endJson) {
//							try {
//								do {
//									try {
//
//										indexOfmatch = text.indexOf(match.replace("\\r\\n", "\r\n"), startJson);
//
//										if (previousIndex <= previousNodeIndex && indexOfmatch > startJson) {
//
//											if (indexOfmatch > nextNode) {
//												do {
//													try {
//														if (allNextNodeIndex.isEmpty() || indexOfmatch > nextNode) {
//
//															indexOfmatch = text.indexOf(match.replace("\\r\\n", "\r\n"),
//																	previousIndex);
//															return indexOfmatch;
//														}
//														nextNode = allNextNodeIndex.pop();
//													} catch (Exception e) {
//														e.printStackTrace();
//													}
//												} while (indexOfmatch > nextNode);
//											}
//											return indexOfmatch;
//
//										} else if (indexOfmatch <= previousIndex) {
//										}
//
//									} catch (NullPointerException e) {
//										e.printStackTrace();
//										return indexOfmatch;
//									}
//									if (startJson == -1 && previousNodeIndex == -1) {
//										return indexOfmatch;
//									}
//									if (indexOfmatch > endJson && indexOfmatch > previousNodeIndex
//											&& indexOfmatch > previousIndex && indexOfmatch > startJson) {
//										System.out.println("fff");
//										return indexOfmatch;
//									} else if (previousNodeIndex > endJson && previousNodeIndex > endJson) {
//										return indexOfmatch;
//									}
//								} while (indexOfmatch > endJson && indexOfmatch > previousNodeIndex
//										&& indexOfmatch > previousIndex);
//							} catch (StackOverflowError e) {
//								e.printStackTrace();
//								return indexOfmatch;
//							}
//						} else if (nextNode == -1) {
//							return indexOfmatch;
//						}
//					} else if (indexOfmatch == 0) {
//						return indexOfmatch;
//					} else if (indexOfmatch == -1
//							|| (allPreviousIndex.isEmpty() || allNextNodeIndex.isEmpty() || !allJsonIndex.isEmpty())) {
//						indexOfmatch = text.indexOf(match, previousIndex);
//						if (match.contains("\\r\\n") || indexOfmatch == -1 || match.contains("\\n")) {
//							indexOfmatch = text.indexOf(match.replace("\\r\\n", "\r\n"), startJson);
//							if (match.contains("\\n")) {
//								indexOfmatch = text.indexOf(match.replace("\\n", "\n"), startJson);
//							}
//							if (indexOfmatch == -1) {
//								System.out.println("return asxddddsa");
////								return between(fileText, selectedNode, selectedjsonObject, theJsonStartTextIndex,
////										theJsonEndTextIndex, nextNode, ParentIndex, previousNodeIndex, allPreviousIndex,
////										allNextNodeIndex, allJsonIndex);
//							}
//							return indexOfmatch;
//						}
//						return indexOfmatch;
//					}
//				}
//			}
//			System.out.println("last");
//			return indexOfmatch;
//		}

}
