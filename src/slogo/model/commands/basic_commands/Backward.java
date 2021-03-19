package slogo.model.commands.basic_commands;

import java.util.List;
import slogo.model.commands.basic_commands.command_types.TurtleAlteringCommand;
import slogo.model.execution.CommandInformationBundle;
import slogo.model.tree.TreeNode;

/**
 * The backward command
 *
 * @author Casey Szilagyi
 */
public class Backward extends TurtleAlteringCommand {

  private final double DISTANCE;

  /**
   * Makes an instance of the backward command
   *
   * @param bundle The information bundle that holds the turtle that this command operates on
   * @param nodes  All of the children nodes needed for this command
   */
  public Backward(CommandInformationBundle bundle, List<TreeNode> nodes) {
    super(bundle);
    DISTANCE = super.loadClass(bundle, nodes.get(0)).execute();
  }

  /**
   * Makes the turtle move the distance backward that is specified by the child node
   *
   * @return The distance backward that the turtle moved
   */
  public double execute() {
    updateTurtle(turtle -> {
      changeTurtleX(-1 * DISTANCE * Math.cos(getAngle() / 360 * Math.PI * 2));
      changeTurtleY(-1 * DISTANCE * Math.sin(getAngle() / 360 * Math.PI * 2));
    });
    return DISTANCE;
  }

}
