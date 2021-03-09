package slogo.model.commands.basic_commands;

import java.util.List;
import slogo.model.commands.basic_commands.command_types.TurtleAlteringCommand;
import slogo.model.execution.CommandInformationBundle;
import slogo.model.tree.TreeNode;

/**
 * The rotate right command
 *
 * @author Casey Szilagyi
 */
public class Right extends TurtleAlteringCommand {

  private final double ANGLE;

  /**
   * Makes an instance of the right command
   *
   * @param bundle Contains the turtle that will need to be altered for this command
   * @param nodes All of the children nodes needed for this command
   */
  public Right(CommandInformationBundle bundle ,List<TreeNode> nodes) {
    super(bundle);
    ANGLE = loadClass(bundle, nodes.get(0)).execute();
  }

  /**
   * Makes the turtle rotate right by the specified number of degrees
   *
   * @return The angle that it rotated
   */
  public double execute() {
    changeTurtleAngle(-1 * ANGLE);
    updateFrontEnd();
    return ANGLE;
  }
}