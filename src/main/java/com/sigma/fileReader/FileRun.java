package com.sigma.fileReader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;

import com.sigma.fileReader.tree.ButtonSelect;
import com.sigma.fileReader.tree.SearchButton;

public class FileRun {

	JScrollPane jScrollPaneTextArea, jScrollPaneTree, JScrollPanePanel;

	private JFrame window;

	// the Runnable class will run all the program
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileRun main = new FileRun();

					main.window.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Constructor for the main method
	public FileRun() {
		Layout();
	}

	ButtonSelect buttonC = new ButtonSelect();

	private void Layout() {

		window = new JFrame("Log Reader");
		BorderLayout borderLayout = (BorderLayout) window.getContentPane().getLayout();
		borderLayout.setVgap(0);
		borderLayout.setHgap(5);

//		window.setBounds(100, 100, 588, 415);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1000, 800);
		window.setResizable(true);
		
//		JPanel jPanel = new JPanel();

//		window.add(jPanel);

		buttonC.getButtonfC().setBounds(400, 20, 100, 35);
		buttonC.getButtonfC().setSize(100, 33);
		buttonC.getButtonfC().setBorderPainted(true);

		buttonC.setTextArea(new JTextArea(20, 20));
		buttonC.getTextArea().setLineWrap(false);
		buttonC.getTextArea().requestFocus();
		buttonC.getTextArea().setWrapStyleWord(false);
		buttonC.getTextArea().setFont(new Font("Arial", Font.PLAIN, 13));
		buttonC.getTextArea().setEditable(false);

		jScrollPaneTextArea = new JScrollPane(buttonC.getTextArea(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jScrollPaneTextArea.setPreferredSize(new Dimension(600, 300));

		buttonC.setTree(new JTree());
		buttonC.getTree().requestFocus();
		buttonC.getTree().setShowsRootHandles(true);

		jScrollPaneTree = new JScrollPane(buttonC.getTree(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jScrollPaneTree.setPreferredSize(new Dimension(300, 300));
		jScrollPaneTree.setMaximumSize(new Dimension(500, 300));

		JPanel panel = new JPanel();
		panel.setSize(50, 59);
//		JButton m_searchButton = new JButton("Search Node");

//		JTextField m_searchText = new JTextField(20);

		JPanel searchPanel = new JPanel();
		searchPanel.setBorder(BorderFactory.createEtchedBorder());

		JLabel label= new JLabel("Expand & Collapse :");
		buttonC.getCheckBox().setEnabled(false);

		searchPanel.add(label);
		buttonC.getSearchInJTreeButton().setEnabled(true);

		searchPanel.add(buttonC.getCheckBox());
		searchPanel.add(buttonC.getSearchText());
		searchPanel.add(buttonC.getSearchInJTreeButton());
		searchPanel.add(buttonC.getSearchButton());
		
		
		panel.add(searchPanel);
		
		panel.add(buttonC.getButtonfC());

		window.getContentPane().add(panel, BorderLayout.NORTH);

//		window.getContentPane().add(buttonC.getButtonfC(), BorderLayout.NORTH);
		window.getContentPane().add(jScrollPaneTextArea, BorderLayout.CENTER);
		window.getContentPane().add(jScrollPaneTree, BorderLayout.WEST);
		
		window.getContentPane().add(buttonC.getLabelFooter(), BorderLayout.SOUTH);
		
//		window.setLayout(null);
//		jPanel.add(buttonfC);
//		jPanel.add(jScrollPane);
//		jPanel.add(tree);
	}

	public JFrame getWindow() {
		return window;
	}

	public void setWindow(JFrame window) {
		this.window = window;
	}

}
