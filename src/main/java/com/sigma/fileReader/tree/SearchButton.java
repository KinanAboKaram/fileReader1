package com.sigma.fileReader.tree;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class SearchButton<E> {

	// create the LightYellow color from the HSB colors.
	private static TheHhighLight MyHighLight = new TheHhighLight(Color.red);
	private JsonUtils jsonUtils = new JsonUtils();

	public void searchAndHighLight(JButton searchButton, JTextComponent textArea, JTextField searchText) {

		searchButton.addActionListener(e -> {
			try {
				//removing the HighLight from the text Area
				HighLightAndsCrollToPosition.removeHighlights(textArea);
				// get the text from the field and delete special Character 
				String search = searchText.getText().toLowerCase();
				search = jsonUtils.deleteSpecialCharacter(search);
				
				// getting the length from the textArea 
				int textLength = textArea.getText().length();
				
				// getting the text from textArea and delete the special Characters and highlight
				String text = textArea.getText(0, textLength).toLowerCase();
				text = jsonUtils.deleteSpecialCharacter(text);
				Highlighter highlight = textArea.getHighlighter();
				
				// find and highlight all the match in the text
				int indxOfmatch = 0;
				if (!search.isEmpty()) {
					while ((indxOfmatch = text.indexOf(search, indxOfmatch)) >= 0) {
						highlight.addHighlight(indxOfmatch, indxOfmatch + search.length(), MyHighLight);
						indxOfmatch += search.length();
					}
				}
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		});
	}

	public void searchJTreeButton(JButton searchInJTreeButton, JTextField searchText, JTree tree) {
		
		//removing the old mouse listener
		for (MouseListener mouseListener : searchInJTreeButton.getMouseListeners()) {
			searchInJTreeButton.removeMouseListener(mouseListener);
		}

		searchInJTreeButton.addMouseListener(new ClickedListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String search = searchText.getText();
				search.replace("\\r\\n", "\r\n");
				if (search.trim().length() > 0) {

					DefaultMutableTreeNode node = findNode(search, tree);
					if (node != null) {
						TreePath path = new TreePath(node.getPath());
						tree.setSelectionPath(path);
						tree.scrollPathToVisible(path);

					}

				}
			}
		});

	}

	private class ClickedListener extends MouseAdapter {
		public final DefaultMutableTreeNode findNode(String searchString, JTree tree) {

			List<DefaultMutableTreeNode> searchNodes = getSearchNodes(
					(DefaultMutableTreeNode) tree.getModel().getRoot());
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			DefaultMutableTreeNode foundNode = null;
			int mark = -1;

			if (currentNode != null) {
				for (int index = 0; index < searchNodes.size(); index++) {
					if (searchNodes.get(index) == currentNode) {
						mark = index;
						break;
					}
				}
			}

			for (int index = mark + 1; index < searchNodes.size(); index++) {
				if (searchNodes.get(index).toString().toLowerCase().contains(searchString.toLowerCase())) {
					foundNode = searchNodes.get(index);
					break;
				}
			}

			if (foundNode == null) {
				for (int index = 0; index <= mark; index++) {
					if (searchNodes.get(index).toString().toLowerCase().contains(searchString.toLowerCase())) {
						foundNode = searchNodes.get(index);
						break;
					}
				}
			}
			return foundNode;
		}

		private final List<DefaultMutableTreeNode> getSearchNodes(DefaultMutableTreeNode root) {
			List<DefaultMutableTreeNode> searchNodes = new ArrayList<DefaultMutableTreeNode>();

			Enumeration<?> elements = root.preorderEnumeration();
			while (elements.hasMoreElements()) {
				searchNodes.add((DefaultMutableTreeNode) elements.nextElement());
			}
			return searchNodes;
		}
	}

}
