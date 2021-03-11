package slogo.view;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import slogo.controller.FrontEndExternalAPI;

/**
 * Create the main screen where visuals and panes will be displayed
 * @author Kathleen Chen
 * @author Ji Yun Hyo
 */
public class ScreenCreator {
  private static final String TITLE = "SLogo";
  private static final double DEFAULT_X = 1000.0;
  private static final double DEFAULT_Y = 860.0;

  private BorderPane root;
  private Scene scene;
  private Stage stage;
  private PossibleCommandPane possibleCommandPane;
  private UserCommandPane userCommand;
  private ViewPane viewPane;
  private FrontEndExternalAPI viewController;
  private String styleSheet;

  public ScreenCreator(FrontEndExternalAPI viewController) {
    this.viewController = viewController;
    stage = new Stage();
    stage.setResizable(true);
    root = new BorderPane();
    scene = new Scene(root, DEFAULT_X, DEFAULT_Y);
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();

    styleSheet = "slogo/view/resources/default.css";
    scene.getStylesheets().add(styleSheet);

    possibleCommandPane = new PossibleCommandPane();
    root.setRight(possibleCommandPane.getBox());

    userCommand = new UserCommandPane(viewController);
    root.setBottom(userCommand.getBox());

    viewPane = new ViewPane(stage);
    root.setCenter(viewPane.getBox());
  }

  public void moveTurtle(List<Double> parameters){
    System.out.println("parameters: " + parameters);
    viewPane.updateTurtle(parameters);
    if(parameters.get(5) == 1){
      reset();
    }
  }

  //TODO: REMOVE LATER THIS IS ONLY FOR DEBUGGING
  private void reset(){

    viewPane = new ViewPane(stage);
    root.setCenter(viewPane.getBox());

  }
}
