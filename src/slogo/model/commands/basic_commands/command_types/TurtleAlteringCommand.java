package slogo.model.commands.basic_commands.command_types;

import java.util.List;
import slogo.model.commands.basic_commands.BasicCommand;
import slogo.model.execution.CommandInformationBundle;
import slogo.model.tree.TreeNode;
import slogo.model.turtle.Turtle;

/**
 * This abstract class is designed to be implemented by any BasicCommand that alters the state of
 * the turtle in any way
 *
 * @author Casey Szilagyi
 */
public abstract class TurtleAlteringCommand extends TurtleQueryCommand {

  Turtle TURTLE;

  /**
   * Makes the BasicCommand and saves the turtle
   *
   * @param informationBundle The only part of this bundle that is needed is the turtle
   * @param children          The children needed to execute this node
   */
  public TurtleAlteringCommand(CommandInformationBundle informationBundle,
      List<TreeNode> children) {
    TURTLE = informationBundle.getTurtle();
  }

  /**
   * Executes the command. This is implemented in the subclass because it is different for each
   * command
   *
   * @return The double value representing the result of the executed method
   */
  public abstract double execute();

  /**
   * Changes the X position of the turtle
   *
   * @param change The change in X position
   */
  protected void changeTurtleX(double change) {
    TURTLE.changeXPosition(change);
  }

  /**
   * Changes the Y position of the turtle
   *
   * @param change The change in Y position
   */
  protected void changeTurtleY(double change) {
    TURTLE.changeYPosition(change);
  }

  /**
   * Changes the angle of the turtle
   *
   * @param change The change in angle in degrees, in the counterclockwise direction
   */
  protected void changeTurtleAngle(double change) {
    TURTLE.rotateClockwise(change);
  }

  /**
   * Sets the X position of the turtle
   *
   * @param position The X position
   */
  protected void setTurtleX(double position) {
    TURTLE.setXPosition(position);
  }

  /**
   * Sets the Y position of the turtle
   *
   * @param position The Y position
   */
  protected void setTurtleY(double position) {
    TURTLE.setYPosition(position);
  }

  /**
   * Sets the angle position of the turtle
   *
   * @param angle The angle
   */
  protected void setAngle(double angle) {
    TURTLE.setAngle(angle);
  }

  /**
   * Sets the pen state
   *
   * @param penState The pen state
   */
  protected void changePenState(double penState) {
    TURTLE.setPenState(penState);
  }

  /**
   * Sets the turtle visibility
   *
   * @param visibility The visibility
   */
  protected void changeTurtleVisibility(double visibility) {
    TURTLE.setVisibility(visibility);
  }

  /**
   * Resets the screen and moves the turtle back to 0,0 Need to change this implementation, won't
   * work right now
   */
  protected void reset() {
    TURTLE.clearScreen();
  }


}
