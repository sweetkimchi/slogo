package slogo.model.commands.basic_commands;

import java.util.List;
import slogo.model.commands.basic_commands.command_types.DisplayAlteringCommand;
import slogo.model.commands.basic_commands.command_types.TurtleAlteringCommand;
import slogo.model.execution.CommandInformationBundle;
import slogo.model.tree.TreeNode;

/**
 * Sets the background color
 *
 * @author Casey Szilagyi
 */
public class SetBackground extends DisplayAlteringCommand {

  private final double INDEX;

  /**
   * Makes an instance of the set background command
   *
   * @param bundle Contains the model controller that the command is sent through
   * @param nodes  1 child, which is the index corresponding to the color
   */
  public SetBackground(CommandInformationBundle bundle, List<TreeNode> nodes) {
    super(bundle);
    INDEX = loadClass(bundle, nodes.get(0)).execute();
  }

  /**
   * Sets the background color
   *
   * @return The index of the chosen background color
   */
  @Override
  public double execute() {
    setBackgroundColor((int) INDEX);
    return INDEX;
  }
}
