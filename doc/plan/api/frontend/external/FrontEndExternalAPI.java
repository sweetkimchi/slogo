package slogo.controller;

import java.util.*;
import javax.swing.text.html.ImageView;
import slogo.model.Turtle;

/**
 * FrontEndExternal API methods the backend can call
 */
public interface FrontEndExternalAPI {

  /**
   * Sets the background color of the GUI
   */
  public void setBackGroundColor(String color);

  /**
   * Sets the image of the turtle
   */
  public void setTurtleImage(Turtle turtle, ImageView image);

  /**
   * Sets the language of the commands
   */
  public void setLanguage(String language);

  /**
   * Backend can call this method to relay information to be displayed to the frontend
   */
  public void displayCommandResult(List<String> resultsOfCommandExecution);

  /**
   * Displays error message
   */
  public void displayError(String errorMessage);

}