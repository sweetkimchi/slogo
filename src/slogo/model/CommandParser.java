package slogo.model;

import java.util.*;

import slogo.ErrorHandler;
import slogo.controller.BackEndExternalAPI;
import slogo.controller.ModelController;
import slogo.model.commands.basic_commands.BasicCommand;
import slogo.model.commands.basic_commands.UserDefinedCommand;
import slogo.model.tree.TreeNode;

/**
 * creates a n-ary tree of all commands and their parameters associated with this string input
 *
 * @author jincho jiyunhyo
 */
public class CommandParser implements Parser {

    // where to find resources specifically for this class
    private static final String RESOURCES_PACKAGE = CommandParser.class.getPackageName()+".resources.commands.";

    // "types" and the regular expression patterns that recognize those types
    // note, it is a list because order matters (some patterns may be more generic)
    private Map<String, String> parameters;
    private TreeNode commandTree;
    private List<String> cleanCommands;
    private BackEndExternalAPI modelController;
    private InputCleaner inputCleaner;
    public List<String> preOrderResults = new ArrayList<>();



    public CommandParser(String rawInput, String language, BackEndExternalAPI modelController){
        this.modelController = modelController;
        parameters = new HashMap<>();
        inputCleaner = new InputCleaner(rawInput, language, modelController, this);
        cleanCommands = inputCleaner.cleanString();
        addParamCounts("Commands");
        addUserDefParamCounts();
        commandTree = new TreeNode(null);
        System.out.println("Command Taken in by the parser: " + rawInput);
        System.out.println("Clean command: "+cleanCommands);
    }

    /**
     * Adds the given resource file to this language's recognized types
     */
    public void addParamCounts(String syntax) {
        ResourceBundle resources = ResourceBundle.getBundle(RESOURCES_PACKAGE + syntax);
        for (String key : Collections.list(resources.getKeys())) {
            addSingleParamCount(key, resources.getString(key));
            parameters.put(key, resources.getString(key));
//            System.out.println("Key: " + key);
//            System.out.println("Number: " + resources.getString(key));
//            System.out.println();
        }
        System.out.println(parameters);

    }


    /**
     * adds a single command and parameter count pair to the map
     * @param command command type name
     * @param paramCount number of parameters command will take in
     */
    public void addSingleParamCount(String command, String paramCount){
        parameters.put(command, paramCount);
    }

    private void addUserDefParamCounts() {
        Map<String, UserDefinedCommand> userDefCommands= modelController.getUserDefinedCommands();
        for(String key : userDefCommands.keySet()) {
            int paramCounts = userDefCommands.get(key).getParamCount();
            parameters.put(key, String.valueOf(paramCounts));
        }
    }
    /**
     * makes the tree at the tree root node commandTree
     * @return root node of command tree
     */
    public TreeNode makeTree() {
        Deque<String> commandQueue = new LinkedList<>(cleanCommands);
        System.out.println("QUEUE: " + commandQueue);

        while (!commandQueue.isEmpty()) {
            String command = commandQueue.removeFirst();
            TreeNode child = new TreeNode(command);
            child = checkCommandBlock(child);
            commandTree.addChild(child);
            insertNodeRecursive(commandQueue, child);
        }
        printPreOrder(commandTree);
        if(preOrderResults.size() != cleanCommands.size()+1){
            throw new ErrorHandler("WrongParamNum");
        }
        return commandTree;
    }

    private void printPreOrder(TreeNode root) {
        preOrderResults.add(root.getValue());
        if (root == null) {
            return;
        }

        System.out.println("Value: " + root.getCommand());
        for(TreeNode child : root.getChildren()){
            printPreOrder(child);
        }
    }

    private TreeNode insertNodeRecursive(Deque<String> splitCommands, TreeNode root) {
        if (getParamCount(root.getValue()) == 0) {
            System.out.println(root.getValue() + " is a leaf");
        }


        System.out.println();
        int paramCount = getParamCount(root.getValue());
        System.out.println(paramCount);
        for(int i = 0; i < getParamCount(root.getValue()); i ++){
            String command = splitCommands.removeFirst();
            TreeNode dummy = new TreeNode(command);
            dummy = checkCommandBlock(dummy);
            root.addChild(dummy);
            System.out.println("Parent: " + root.getCommand());
            System.out.println("Child: " + dummy.getCommand());
            insertNodeRecursive(splitCommands, dummy);
        }

        System.out.println();
        return root;
    }

    /**
     * Returns respective parameter counts for the command specified
     * @param text String representation of the command
     * @return String rep of the number of params needed for command
     */
    public Integer getParamCount(String text) {
        try{
            return Integer.parseInt(parameters.get(text));
        }catch (Exception e){
            return 0;
        }
    }

    private TreeNode checkCommandBlock(TreeNode node){
        if(node.getCommand().contains("CommandBlock")) {
            node = new TreeNode(node.getCommand(), "CommandBlock");
        }
        return node;
    }

    @Override
    public List<String> translateCommand(List<String> commandsBeforeTranslation) {
        return null;
    }
}