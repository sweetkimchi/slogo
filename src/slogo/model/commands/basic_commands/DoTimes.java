package slogo.model.commands.basic_commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import slogo.model.commands.basic_commands.command_types.Command;
import slogo.model.commands.basic_commands.command_types.ControlStructureCommand;
import slogo.model.execution.CommandInformationBundle;
import slogo.model.tree.TreeNode;

/**
 * Runs a block of commands for each value specified in a range, with a variable
 *
 * @author Casey Szilagyi
 */
public class DoTimes extends ControlStructureCommand {

  private final String VARIABLE;
  private final double LIMIT;
  private final TreeNode COMMAND_BLOCK;

  /**
   * Makes an instance of the DoTimes loop
   *
   * @param bundle   The pieces of information, such as variables and user defined commands, that
   *                 may be needed to execute the the command
   * @param children Has 2 nodes. One has the variable name and limit that it goes to, and the other
   *                 has the block of commands
   */
  public DoTimes(CommandInformationBundle bundle, List<TreeNode> children) {
    super(bundle);
    VARIABLE = children.get(0).getChildren().get(0).getValue();
    LIMIT = executeBlock(children.get(0).getChildren().get(1));
    COMMAND_BLOCK = children.get(1);
  }

  /**
   * Repeats the loop the specified number of times. The variable that is declared is stored
   * as the loop # that is currently happening
   *
   * @return The value of the final command executed
   */
  public double execute() {
    double val = 0;
    Map<String, Double> params = new HashMap<>();
    addParamMap(params);
    for (double i = 1; i <= LIMIT; i += 1) {
      params.put(VARIABLE, i);
      val = executeBlock(COMMAND_BLOCK);
    }
    removeParamMap();
    return val;
  }
}
