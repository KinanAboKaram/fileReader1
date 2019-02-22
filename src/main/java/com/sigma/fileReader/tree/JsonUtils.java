package com.sigma.fileReader.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.wink.json4j.OrderedJSONObject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

//import org.codehaus.jackson.JsonNode;
//import org.codehaus.jackson.map.ObjectMapper;
//
//import com.fasterxml.jackson.core.JsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public final class JsonUtils {

	JsonUtils() {
		super();
	}

	public JsonNode rootJsonNode = null;
	public JsonParser parser = new JsonParser();
	private Gson gson = new Gson();
	private ObjectMapper mapper = new ObjectMapper();

	public boolean isJSONValid(String jsonInString) {
		try {
			gson.fromJson(jsonInString, Object.class);
//			parser.parse(jsonInString);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void updateModel(ArrayList<String> jsonStringList, JTree tree) {
		try {

			clearTree(tree);

			DefaultMutableTreeNode root = addRoot("Root", jsonStringList);
			DefaultTreeModel model = new DefaultTreeModel(root);
			System.out.println("+");

			tree.setModel(model);
			model.reload();
//			System.out.println(tree.getModel().toString());
			tree.setRootVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public DefaultMutableTreeNode addRoot(String rootName, ArrayList<String> jsonStringList) {

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootName);
		for (int i = 0; i < jsonStringList.size(); i++) {
			try {
				System.out.println("---------------------------------------------------------------****-----------");

				// OrderedJsonObject add the string to linkdHashMap
				OrderedJSONObject jObject = new OrderedJSONObject(jsonStringList.get(i));
				rootJsonNode = mapper.readTree(jObject.toString());

				rootNode.add(buildTree(jsonStringList.get(i), rootJsonNode));

//				System.out.println("-----------------------------------------------------------------------------");
			} catch (JSONException e) {

				e.printStackTrace();
			} catch (JsonProcessingException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} catch (org.apache.wink.json4j.JSONException e) {

				e.printStackTrace();
			}

		}
		return rootNode;
	}

	private DefaultMutableTreeNode buildTree(String name, JsonNode jsonNode)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {

		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(name);
		Iterator<Entry<String, JsonNode>> iterator = jsonNode.getFields();
		while (iterator.hasNext()) {
			Entry<String, JsonNode> entry = iterator.next();

			treeNode.add(buildTree(entry.getKey(), entry.getValue()));
		}
		if (jsonNode.isArray()) {
			String childArray = null;
			for (int i = 0; i < jsonNode.size(); i++) {
				JsonNode child = jsonNode.get(i);

				childArray = child.toString();
				childArray = deleteSpecialCharacter(childArray);

				if (child.isValueNode()) {
					treeNode.add(new DefaultMutableTreeNode(childArray));
				} else {
					AddingArrayJson(child, treeNode);
				}
			}
		} else if (jsonNode.isValueNode()) {
			treeNode.add(new DefaultMutableTreeNode(jsonNode.toString()));
		}
		return treeNode;
	}

	private void AddingArrayJson(JsonNode child, DefaultMutableTreeNode treeNode)
			throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		Iterator<Entry<String, JsonNode>> childIterator = child.getFields();

		while (childIterator.hasNext()) {
			Entry<String, JsonNode> entryChild = childIterator.next();
			JsonNode childNode = entryChild.getValue();

			if (childNode.isArray()) {
				treeNode.add(buildTree(entryChild.getKey().toString(), childNode));

			} else if (!childNode.isValueNode()) {
				treeNode.add(buildTree(entryChild.getKey().toString(), childNode));
			} else {
				String value = null;
				value = entryChild.getValue().toString();
				value = deleteSpecialCharacter(value);
				treeNode.add(new DefaultMutableTreeNode('"' + entryChild.getKey() + '"' + ": " + value));
			}
		}
	}

	public String deleteSpecialCharacter(String node) {

		if (node.contains("\\r\\n")) {
			node = node.replace("\\r\\n", "\r\n");
			if (node.contains("\\n")) {
				node = node.replace("\\n", "\n");
			}
		}
		if (node.contains("\\n")) {
			node = node.replace("\\n", "\n");
			if (node.contains("\\r\\n")) {
				node = node.replace("\\r\\n", "\r\n");
			}
		}
		if (node.contains("\\r")) {
			node = node.replace("\\r", "\r");
		}
		return node;
	}

	// it run if selected new file and delete the old tree and old selectionListener
	// wnd will remove the root and all nodes and the model
	public void clearTree(JTree tree) {
		if (tree != null) {

			for (TreeSelectionListener treeSelection : tree.getTreeSelectionListeners()) {
				tree.removeTreeSelectionListener(treeSelection);
			}
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			root.removeAllChildren();
			root = null;
			model.reload();
			model.setRoot(null);
		}
	}
}
