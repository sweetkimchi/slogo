package slogo.model.tree;

import java.util.LinkedList;
import java.util.List;

/**
 * https://ruslanspivak.com/lsbasi-part7/
 */
public class TreeNode {

  String val;
  List<TreeNode> children = new LinkedList<>();

  public TreeNode(String data) {
    val = data;
  }

  public TreeNode(String data, List<TreeNode> child) {
    val = data;
    children = child;
  }

  /**
   * Adds a single child to this node
   *
   * @param newChild - the child node to be added
   */
  public void addChild(TreeNode newChild) {
    children.add(newChild);
  }

  /**
   * Returns a list of the children of this node
   *
   * @return children - the children of the node
   */
  public List<TreeNode> getChildren() {
    return children;
  }

  /**
   * Returns the value saved in this node
   *
   * @return String representation of translated command, variable, or constant
   */
  public String getValue() {
    return val;
  }

    public String getVal(){
        return val;
    }

}
