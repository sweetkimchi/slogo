package slogo.view;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import slogo.controller.FrontEndExternalAPI;

/**
 * Create the main screen where visuals and panes will be displayed
 *
 * @author Kathleen Chen
 * @author Ji Yun Hyo
 */
public class ScreenCreator {

  private static final String TITLE = "SLogo";
  private static final double DEFAULT_X = 1330.0;
  private static final double DEFAULT_Y = 800.0;
  public static final String DEFAULT_RESOURCE_PACKAGE = HistoryDisplayPane.class.getPackageName() + ".resources.";
  private static final String RESOURCE_PATH = DEFAULT_RESOURCE_PACKAGE + "buttons.languages";

  private BorderPane root;
  private Scene scene;
  private Stage stage;
  private HistoryDisplayPane historyDisplayPane;
  private UserCommandPane userCommand;
  private ViewPane viewPane;
  private CommandButtonPane commandButtonPane;
  private FrontEndExternalAPI viewController;
  private String styleSheet;
  private ResourceBundle languageResource;
  private int frameDelay;
  private Timeline timeline;

  public ScreenCreator(FrontEndExternalAPI viewController) {
    this.viewController = viewController;
    stage = new Stage();
    stage.setResizable(true);
    root = new BorderPane();
    scene = new Scene(root, DEFAULT_X, DEFAULT_Y);
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();

    String defaultLanguage = "English";
    languageResource = ResourceBundle.getBundle(RESOURCE_PATH + "." + defaultLanguage);

    styleSheet = "slogo/view/resources/default.css";
    scene.getStylesheets().add(styleSheet);

    historyDisplayPane = new HistoryDisplayPane(viewController);
    root.setRight(historyDisplayPane.getBox());

    userCommand = new UserCommandPane(viewController);
    root.setBottom(userCommand.getBox());

    viewPane = new ViewPane(stage);
    root.setCenter(viewPane.getBox());

    commandButtonPane = new CommandButtonPane(viewController, defaultLanguage);
    root.setLeft(commandButtonPane.getBox());

    runSimulation();

  }


  public void moveTurtle(List<Double> parameters) {

    System.out.println("parameters: " + parameters);
    viewPane.updateTurtle(parameters);

//    if(parameters.get(5) == 1){
//      reset();
//    }
  }

  //TODO: REMOVE LATER THIS IS ONLY FOR DEBUGGING
  private void reset() {

    viewPane = new ViewPane(stage);
    root.setCenter(viewPane.getBox());

  }

  public void setAnimationSpeed(){
    timeline.setRate(userCommand.getAnimationSpeed());
  }

  public String getLanguage() {
    return viewPane.getLanguage();
  }

  public void updateLanguage() {
    String language = viewPane.getLanguage();
    languageResource = ResourceBundle.getBundle(RESOURCE_PATH + "." + language);
  }

  public void updateCommandHistory(Queue<String> commandHistory) {
    historyDisplayPane.updateCommandHistory(commandHistory);
  }


  public void updateVariablesAndUserDefinedCommands(Map<String, Double> variables,
      Map<String, String> userDefinedCommands) {
    historyDisplayPane.updateDisplayOfInformation(variables, userDefinedCommands);
  }

  public void displayCommandStringOnTextArea(String command) {
    userCommand.displayCommandStringOnTextArea(command);
  }

  private void runSimulation() {
//      animationTimer = new AnimationTimer() {
//        @Override
//        public void handle(long now) {
//          if (sleepTimer < frameDelay) {
//            sleepTimer++;
//            return;
//          }
//          updateTurtlePosition();
//          sleepTimer = 0;
//        }
//      };
    timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {

      displayTurtleUpdates();
      setAnimationSpeed();


    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
    timeline.setRate(300);
  }

  private void displayTurtleUpdates() {
    viewPane.displayTurtleUpdates();
  }
}
