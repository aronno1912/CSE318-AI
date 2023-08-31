

import java.util.*;

class TreeNode {
    private String attribute;
     Map<String, TreeNode> children;
    String classification;

    public TreeNode(String attribute) {
        this.attribute = attribute;
        this.children = new HashMap<>();
        this.classification = null;
    }
    public void addChild(String value, TreeNode childNode) {
        children.put(value, childNode);
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getAttribute() {
        return attribute;
    }

    public boolean isLeaf() {
        return classification != null;
    }

    public boolean hasChild(String value) {
        return children.containsKey(value);
    }

    public TreeNode getChild(String value) {
        return children.get(value);
    }

    public String getClassification() {
        return classification;
    }
}
