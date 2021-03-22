package slogo.view;

import java.util.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class TurtleDisplayPane {
  private static final double TURTLE_WIDTH = 50;
  private static final double TURTLE_HEIGHT = 50;

  private static final String PANE_BOX_ID = "TurtleView";
  private static final String LINE_ID = "Line";
  private final double centerX;
  private final double centerY;
  private GridPane viewPane;
  private AnchorPane turtleViewPane;


  private double penUP = 1;
  double x;
  double y;
  private Color penColor;

  private Deque<Double> commandsToBeExecuted;
  private Deque<String> typeToBeUpdated;

  private int INCREMENT_FACTOR = 10;
  private double lastXPosition = 0;
  private double lastYPosition = 0;
  private double lastAngle = 90;
  private double rows;
  private double cols;
  private double penThickness = 1.0;
  private Map<Integer, FrontEndTurtle> allTurtleInformation;
  private int FIRST_TURTLE = 1;
  private int currentID = 1;

  String turtleImageFile = "Turtle2.gif";
  String inactiveTurtleImageFile = "Turtle3.gif";
  String movingTurtleImageFile = "Turtle4.gif";


  public TurtleDisplayPane(GridPane root, double r, double c) {
    viewPane = root;
    rows = r;
    cols = c;

    turtleViewPane = new AnchorPane();
    viewPane.add(turtleViewPane, 0, 2);
    turtleViewPane.setId(PANE_BOX_ID);
    turtleViewPane.getStyleClass().add(PANE_BOX_ID);

    turtleViewPane.setMaxHeight(cols);
    turtleViewPane.setMaxWidth(rows);
    turtleViewPane.setMinHeight(cols);
    turtleViewPane.setMinWidth(rows);

    centerX = rows / 2 - TURTLE_HEIGHT / 2;
    centerY = cols / 2 - TURTLE_WIDTH / 2;

    commandsToBeExecuted = new ArrayDeque<>();
    typeToBeUpdated = new ArrayDeque<>();
    allTurtleInformation = new HashMap<>();

    createTurtle(FIRST_TURTLE);
  }

  public void updateTurtlePosition() {
    String nextUpdate;
//
//    System.out.println("Pen State: " + penUP);
    if(!typeToBeUpdated.isEmpty()) {
      nextUpdate = typeToBeUpdated.removeFirst();

      if (nextUpdate.equals("Positions")) {
        double nextX = commandsToBeExecuted.pop();
        double nextY = commandsToBeExecuted.pop();

        if (allTurtleInformation.get(currentID).getPenState() == 1) {
          createLine(nextX, nextY, penColor);
        }
        allTurtleInformation.get(currentID).getTurtle().setX(nextX);
        allTurtleInformation.get(currentID).getTurtle().setY(nextY);
      } else if (nextUpdate.equals("Angles")) {

        allTurtleInformation.get(currentID).getTurtle().setRotate(90 - commandsToBeExecuted.pop());
      } else if (nextUpdate.equals("Pen")){
        allTurtleInformation.get(currentID).setPenState(commandsToBeExecuted.removeFirst());
      } else if (nextUpdate.equals("Visibility")){
        allTurtleInformation.get(currentID).getTurtle().setVisible(commandsToBeExecuted.removeFirst() == 1);
      } else if (nextUpdate.equals("Clearscreen")){
        clearScreen();
      } else if (nextUpdate.equals("SetID")){
        currentID = (int) Math.round(commandsToBeExecuted.pop());

   //     updateTurtleImages();
      }

    }

  }

  private void updateTurtleImages() {

  }


  private void createTurtle(int id) {

    Image turtleImage = new Image(turtleImageFile);
    String imageID = "Turtle" + id;
    ImageView turtle = new ImageView(turtleImage);
    turtle.setFitWidth(TURTLE_WIDTH);
    turtle.setFitHeight(TURTLE_HEIGHT);
    turtle.setId(imageID);
    turtleViewPane.getChildren().add(turtle);

    turtle.setX(centerX);
    turtle.setY(centerY);
    turtle.setRotate(0);

    FrontEndTurtle turtleInformation = new FrontEndTurtle(centerX, centerY, turtle, penUP);

 //   activeTurtles.put(id, turtle);
    this.allTurtleInformation.put(id, turtleInformation);

//    lastXPosition = centerX;
//    lastYPosition = centerY;
  }

  public void moveTurtle(double xCoordinate, double yCoordinate, Color penColor) {
    this.penColor = penColor;

    System.out.println("Current ID: " + currentID);

    x = turtleViewPane.getWidth() / 2 + xCoordinate * turtleViewPane.getWidth() / rows - TURTLE_WIDTH / 2;
    y = turtleViewPane.getHeight() / 2 - yCoordinate * turtleViewPane.getHeight() / cols - TURTLE_HEIGHT / 2;

    double xIncrement = (x - allTurtleInformation.get(currentID).getxCoord())/ INCREMENT_FACTOR;
    double yIncrement = (y - allTurtleInformation.get(currentID).getyCoord())/ INCREMENT_FACTOR;

    for(int i = 1; i <= INCREMENT_FACTOR; i++){
      commandsToBeExecuted.add(allTurtleInformation.get(currentID).getxCoord() + xIncrement * i);
      commandsToBeExecuted.add(allTurtleInformation.get(currentID).getyCoord() + yIncrement * i);
      typeToBeUpdated.add("Positions");
    }

    allTurtleInformation.get(currentID).setxCoord(x);
    allTurtleInformation.get(currentID).setyCoord(y);

//    lastXPosition = x;
//    lastYPosition = y;
  }

  private void createLine(double x, double y, Color penColor) {
    Line line1 = new Line(allTurtleInformation.get(currentID).getTurtle().getX() + TURTLE_WIDTH / 2, allTurtleInformation.get(currentID).getTurtle().getY() + TURTLE_WIDTH / 2,
            x + TURTLE_HEIGHT / 2, y + TURTLE_HEIGHT / 2);
    line1.setStroke(penColor);
    line1.setId(LINE_ID);
    line1.setStrokeWidth(penThickness);
    turtleViewPane.getChildren().add(line1);
  }

  void clearScreen() {
    commandsToBeExecuted.clear();
    typeToBeUpdated.clear();

    turtleViewPane.getChildren().clear();
    for(Map.Entry<Integer, FrontEndTurtle> entry : allTurtleInformation.entrySet()){
      createTurtle(entry.getKey());
    }

  }

  public void setBackground(Background background) {
    turtleViewPane.setBackground(background);
  }

  public void updateTurtle(List<Double> parameters) {
    if (parameters.get(5) == 1) {
      clearScreen();
    }
    if(lastAngle != parameters.get(2)){
      lastAngle = parameters.get(2);
      commandsToBeExecuted.add(90 - parameters.get(2));
      typeToBeUpdated.add("Angles");
    }

    //   turtle.setRotate(90 - parameters.get(2));

    commandsToBeExecuted.add(parameters.get(3));
    typeToBeUpdated.add("Pen");

    commandsToBeExecuted.add(parameters.get(4));
    typeToBeUpdated.add("Visibility");

  }

  public void setTurtleImage(Image turtleImage) {
    allTurtleInformation.get(currentID).getTurtle().setImage(turtleImage);
    allTurtleInformation.get(currentID).getTurtle().setFitWidth(TURTLE_WIDTH);
    allTurtleInformation.get(currentID).getTurtle().setFitHeight(TURTLE_HEIGHT);
    allTurtleInformation.get(currentID).getTurtle().setId("Turtle" + currentID);
  }

  public void updateCommandQueue(String commandType, List<Double> commandValues) {
    typeToBeUpdated.add(commandType);
    commandsToBeExecuted.addAll(commandValues);

  }
  public void setActiveTurtle(int turtleID) {
    if(!allTurtleInformation.containsKey(turtleID)){
      createTurtle(turtleID);
    }

    currentID = turtleID;
    commandsToBeExecuted.add((double) turtleID);
    typeToBeUpdated.add("SetID");

  }

  public void setActiveTurtles(List<Integer> iDs) {
    for(Integer turtleID : allTurtleInformation.keySet().toArray(new Integer[0])){
      if(iDs.contains(turtleID)){
        allTurtleInformation.get(turtleID).getTurtle().setImage(new Image(turtleImageFile));
      }else{
        allTurtleInformation.get(turtleID).getTurtle().setImage(new Image(inactiveTurtleImageFile));
      }
    }


  }
}