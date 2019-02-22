package com.sigma.fileReader.tree;

import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class ExpandAndCollaps {


	public void expandAndCollapse(JCheckBox checkBox, JTree tree) {
		
		for (ActionListener action : checkBox.getActionListeners()) {
			checkBox.removeActionListener(action);
		}
		checkBox.setSelected(false);
		checkBox.setText("");
		checkBox.setEnabled(true);
		checkBox.addActionListener(e -> {
			expandAll(tree, checkBox);
		});
	}

	public void expandAll(JTree tree, JCheckBox checkBox) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll(tree, checkBox, new TreePath(root));
	}

	private void expandAll(JTree tree, JCheckBox checkBox, TreePath parent) {

		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode nodeChild = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(nodeChild);
				expandAll(tree, checkBox, path);
			}
		}
		if (checkBox.isSelected()) {
			checkBox.setText("Expanded");
			tree.expandPath(parent);
		} else if (!checkBox.isSelected()) {
			checkBox.setText("Collapsed");
			tree.collapsePath(parent);
		}
		
	}
}
