package com.sigma.fileReader.tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;

public class ButtonSelect extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ButtonSelect() {
		RunButton();

	}

	HighLightAndsCrollToPosition highLightAndsCroll = new HighLightAndsCrollToPosition();
	private SearchButton<Object> searchClass = new SearchButton<Object>();
	private ExpandAndCollaps expandAndCollapseClass=new ExpandAndCollaps();
	private JCheckBox checkBox= new JCheckBox();
	private JButton buttonfC = new JButton("choose a file");
	private JButton searchTextButton = new JButton("Search In Text");
	private JButton searchInJTreeButton = new JButton("Search Node");
	private JsonUtils jsonUtils = new JsonUtils();
	private JTree tree = new JTree();
	private JTextArea textArea = new JTextArea();
	private JFileChooser fChooser;
	private JTextField searchText = new JTextField(20);
	private ArrayList<String> jsonStringList;
	private String fileContent;
	private JLabel labelFooter= new JLabel(" File Name :");
	int startIndexOfBracket = 0;
	int endIndexOfBracket = 0;

	

	private void RunButton() {
		Stack<Integer> fint = new Stack<>();
		Stack<Integer> lint = new Stack<>();

		// action listener for the button
		System.out.println("fffffffffffffffffffffff");
		buttonfC.addActionListener(e -> {

			fChooser = new JFileChooser();
			fChooser.cancelSelection();
			
			fChooser.setCurrentDirectory(new File("."));
			fChooser.setDialogTitle("Select a file ");
			
			if (fChooser.showOpenDialog(buttonfC) == JFileChooser.APPROVE_OPTION) {
				fChooser.cancelSelection();

				jsonStringList = new ArrayList<>();

				try {
					String result = new String(Files.readAllBytes(Paths.get(fChooser.getSelectedFile().getPath())));
					System.out.println(fChooser.getSelectedFile().getName());
					

					labelFooter.setText(" File Name :  "+fChooser.getSelectedFile().getName());
					fChooser.getSelectedFile().exists();

					fileContent = result.replace("\t", "");
					fileContent += fileContent.indexOf(fileContent.length()) + "{{}}";
					jsonStringList.clear();
				
					char[] charactersInFile = fileContent.toCharArray(); // To read the file to characters array
					int jsonNum = 0;
					fint.clear();
					lint.clear();
					int bracketCounter = 0;
					int characterNum = 0;

					for (char character : charactersInFile) {
						characterNum++;

						if (character == '{') { // Checking for the start of bracket
							bracketCounter++;

							startIndexOfBracket = characterNum - 1;
							
							fint.push(startIndexOfBracket);

						} else if (character == '}') { // Checking for the end of bracket
							bracketCounter--;
							endIndexOfBracket = characterNum;
							lint.push(endIndexOfBracket);
							try {
								String jsonInFile = fileContent.substring(fint.pop(), lint.pop());
								if (bracketCounter == 0) {
									boolean ifValid = jsonUtils.isJSONValid(jsonInFile);

									if (ifValid) {
										jsonNum++;
										jsonStringList.add(jsonInFile);
									}
								}	
							} catch (EmptyStackException EmptyStack) {
								EmptyStack.printStackTrace();
								return;
							}
						}
					}
					// set the text on the screen
					textArea.setText(fileContent);

					//add The list of Json to the Tree 
					jsonUtils.updateModel(jsonStringList, tree);

					// add the highLightAndsCrollToPosition
					highLightAndsCroll.highLightAndsCrollToPosition(tree, textArea, fileContent.replace("\\r\\n", "\r\n"));
				
					// the Search in the text
					searchClass.searchAndHighLight(searchTextButton, textArea, searchText);
					
					//the SEarch in the TreeNode 
					searchClass.searchJTreeButton(searchInJTreeButton, searchText, tree);
					
					//expand  And Collapse the JTreeNode
					expandAndCollapseClass.expandAndCollapse(checkBox, tree);
					
//					jsonUtils.clearTree(tree);
				} catch (OutOfMemoryError oOfM) {
					System.err.println("The file is larger than 2GB ");
					oOfM.printStackTrace();
				} catch (FileNotFoundException fNotFoundEx) {
					System.err.println("The File in not found OR it is made to open a read-only file for writing");
					fNotFoundEx.printStackTrace();
				} catch (Exception e2) {
					System.err.println("something went wrong");
					e2.printStackTrace();
				}
			}
			
		});

	}

	// Getters and Setters
	
	public JButton getButtonfC() {
		return buttonfC;
	}

	public void setButtonfC(JButton buttonfC) {
		this.buttonfC = buttonfC;
	}

	public JFileChooser getfChooser() {
		return fChooser;
	}

	public void setfChooser(JFileChooser fChooser) {
		this.fChooser = fChooser;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	public JButton getSearchButton() {
		return searchTextButton;
	}

	public void setSearchButton(JButton searchButton) {
		this.searchTextButton = searchButton;
	}

	public JTextField getSearchText() {
		return searchText;
	}

	public void setSearchText(JTextField searchText) {
		this.searchText = searchText;
	}

	public JButton getSearchInJTreeButton() {
		return searchInJTreeButton;
	}

	public void setSearchInJTreeButton(JButton searchInJTreeButton) {
		this.searchInJTreeButton = searchInJTreeButton;
	}

	public JCheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(JCheckBox checkBox) {
		this.checkBox = checkBox;
	}

	public JLabel getLabelFooter() {
		return labelFooter;
	}

	public void setLabelFooter(JLabel labelFooter) {
		this.labelFooter = labelFooter;
	}
	
}
