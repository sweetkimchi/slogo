package slogo.model.parse;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import slogo.ErrorHandler;
import slogo.controller.BackEndExternalAPI;
import slogo.model.commands.basic_commands.UserDefinedCommand;
import slogo.model.parse.tokens.Token;
import slogo.model.tree.TreeNode;

/**
 * creates a n-ary tree of all commands and their parameters associated with this string input so that the basic command class objects
 * can be created and executed properly andi in order
 *
 * @author jincho jiyunhyo
 */
public class CommandParser extends Parser {
  public List<String> preOrderResults = new ArrayList<>();
  private TreeNode commandTree;
  private List<Token> cleanCommands;
  private BackEndExternalAPI modelController;
  private InputCleaner inputCleaner;
  private TokensParser tokenMaker;
  private CommandBlockParser commandBlockParser;


  /**
   * constructs the command parser with instance variables it needs for creating the n-ary tree. it creates instances
   * of each of the parser types so that the raw string can be parsed through and checked for certain cleans. in this constructor, it also cleans
   * all the strings such that it is ready for creating the tree.
   *
   * @param rawInput direct input taken from the GUI
   * @param language the language selected by the user for commands
   * @param modelController the model controller that is running for the current input
   */
  public CommandParser(String rawInput, String language, BackEndExternalAPI modelController) {
    this.modelController = modelController;
    inputCleaner = new InputCleaner(rawInput, language, modelController);
    tokenMaker = new TokensParser(inputCleaner.parseResults());
    cleanCommands = tokenMaker.tokenString();
    commandBlockParser = new CommandBlockParser(cleanCommands, this);
    commandBlockParser.commandBlockParams();
    commandTree = new TreeNode(null, null);
    addUserDefParamCounts();
  }

  /**
   * adds a single command and parameter count pair to the map
   *
   * @param command    command type name
   * @param paramCount number of parameters command will take in
   */
  public void addSingleParamCount(String command, List<String> paramCount) {
    commandParam.put(command, paramCount);
  }

  private void addUserDefParamCounts() {
    Map<String, UserDefinedCommand> userDefCommands = modelController.getUserDefinedCommands();
    for (String key : userDefCommands.keySet()) {
      int paramCounts = userDefCommands.get(key).getParamCount();
      List<String> paramString = new ArrayList<>();
      for (int i = 0; i < paramCounts; i++) {
        paramString.add("NUM");
      }
      addSingleParamCount(key, paramString);
    }
  }

  /**
   * makes the tree at the tree root node commandTree
   *
   * @return root node of command tree
   */
  public TreeNode makeTree() {
    Deque<Token> commandQueue = new LinkedList<>(cleanCommands);
    while (!commandQueue.isEmpty()) {
      String command = commandQueue.removeFirst().getValue();
      TreeNode child = new TreeNode(command, null);
      child = checkCommandBlock(child);
      commandTree.addChild(child);
      insertNodeRecursive(commandQueue, child);
    }
    preOrderTraverse(commandTree);
    return commandTree;
  }

  private void preOrderTraverse(TreeNode root) {
    preOrderResults.add(root.getValue());
    if (root == null) {
      return;
    }
    for (TreeNode child : root.getChildren()) {
      preOrderTraverse(child);
    }
  }

  /**
   * returns a string representation of the commands in pre-order (aka matching the order of the input given)
   * @return
   */
  @Override
  public List<String> parseResults() {
    return preOrderResults;
  }

  private TreeNode insertNodeRecursive(Deque<Token> splitCommands, TreeNode root) {
    if (getParam(root.getValue()).size() == 0) {
      return root;
    }
    List<String> params = getParam(root.getValue());
    if (childOfMakeUserInstruction(root) && !root.getCommand().equals(COMMAND_BLOCK_CLASS)) {
      params = new ArrayList<>();
    }
    for (int i = 0; i < params.size(); i++) {
      String command;
      Token commandToken;
      try {
        commandToken = splitCommands.removeFirst();
        command = commandToken.getValue();
      } catch (Exception e) {
        throw new ErrorHandler("WrongParamNum");
      }
      TreeNode dummy = new TreeNode(command, root);
      dummy = checkCommandBlock(dummy);
      root.addChild(dummy);
      insertNodeRecursive(splitCommands, dummy);
    }
    if (!correctChildren(root.getChildren(), params)) {
      throw new ErrorHandler("WrongParamNum");
    }
    return root;
  }

  private boolean childOfMakeUserInstruction(TreeNode root) {
    TreeNode parent = root.getParent();
    return parent != null && parent.getCommand().equals("MakeUserInstruction");
  }

  private List<String> getParam(String text) {
    List<String> paramList = commandParam.get(text);
    return Objects.requireNonNullElseGet(paramList, ArrayList::new);
  }

  private TreeNode checkCommandBlock(TreeNode node) {
    if (node.getCommand().contains(COMMAND_BLOCK_CLASS)) {
      node = new TreeNode(node.getCommand(), COMMAND_BLOCK_CLASS, node.getParent());
    }
    return node;
  }

  private boolean correctChildren(List<TreeNode> children, List<String> expected) {
    if (children.isEmpty() && expected == null) {  return true; }
    if (expected == null || children.size() != expected.size()) {  return false; }
    List<String> toMatch = new ArrayList<>();
    for (int ind = 0; ind < children.size(); ind++) {
      TreeNode t = children.get(ind);
      if (match(t.getCommand(), syntaxMap.get("Command"), syntaxMap.get("Variable"), syntaxMap.get("Constant"))) {
        toMatch.add(NUM_TOKEN);
      }
      if (t.getCommand().equals(COMMAND_BLOCK_CLASS)) { toMatch.set(ind, LIST_TOKEN); }
    }
    return toMatch.equals(expected);
  }


}