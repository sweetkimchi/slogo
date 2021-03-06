package slogo.model.commandParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slogo.ErrorHandler;
import slogo.controller.BackEndExternalAPI;
import slogo.controller.ModelController;
import slogo.model.parse.CommandParser;
import slogo.model.tree.TreeNode;

public class CommandParserTest {

  private CommandParser parser;

  /**
   * Sets up the commandParser
   */
  @BeforeEach
  void setUp() {

  }


  /**
   * Tests one parameters count
   */
  @Test
  void testOneCommand() {
    CommandParser tester = makeParser("fd 50", "English");
    TreeNode root = tester.makeTree();
    List<String> results = new ArrayList<>();
    results.add(null);
    results.add("Forward");
    results.add("50");
    assertEquals(results, tester.preOrderResults);
  }

  /**
   * Tests Sum
   */
  @Test
  void testSumCommand() {
    CommandParser tester = makeParser("sum 50 50", "English");
    TreeNode root = tester.makeTree();
    List<String> results = new ArrayList<>();
    results.add(null);
    results.add("Sum");
    results.add("50");
    results.add("50");
    assertEquals(results, tester.preOrderResults);
  }

  /**
   * Tests command with no parameters
   */
  @Test
  void testNoParam() {
    CommandParser tester = makeParser("cs fd 50", "English");
    TreeNode root = tester.makeTree();
    List<String> results = new ArrayList<>();
    results.add(null);
    results.add("ClearScreen");
    results.add("Forward");
    results.add("50");
    assertEquals(results, tester.preOrderResults);
//    assertEquals(tester.makeTree(), results);
  }

  /**
   * repeat 16 [
   *   fd random 500
   *   rt random 360
   * ]
   */
  @Test
  void testRepeatCommand() {
    CommandParser tester = makeParser("repeat 16 [ fd random 500\n rt random 360\n  ]", "English");
    TreeNode root = tester.makeTree();
    List<String> results = new ArrayList<>();
    results.add(null);
    results.add("Repeat");
    results.add("16");
    results.add("CommandBlock_1");
    results.add("Forward");
    results.add("RandomNumber");
    results.add("500");
    results.add("Right");
    results.add("RandomNumber");
    results.add("360");
    assertEquals(results, tester.preOrderResults);
    assertEquals(2, tester.commandParam.get("CommandBlock_1").size());
//    assertEquals(tester.makeTree(), results);
  }

  /**
   * repeat 11 [
   *    dotimes [ :t 360 ] [
   *       fd 1
   *       rt / sin :t 2
   *    ]
   * ]
   */
  @Test
  void testFlowerCommand() {
    CommandParser tester = makeParser("repeat 11 [ dotimes [ :t 360 ] [  fd 1 rt / sin :t 2 ] ] ", "English");
    TreeNode root = tester.makeTree();
    List<String> results = new ArrayList<>();
    results.add(null);
    results.add("Repeat");
    results.add("11");
    results.add("CommandBlock_1");
    results.add("DoTimes");
    results.add("CommandBlock_2");
    results.add(":t");
    results.add("360");
    results.add("CommandBlock_3");
    results.add("Forward");
    results.add("1");
    results.add("Right");
    results.add("Quotient");
    results.add("Sine");
    results.add(":t");
    results.add("2");

    assertEquals(results, tester.preOrderResults);
    assertEquals(1, tester.commandParam.get("CommandBlock_1").size());
    assertEquals(2, tester.commandParam.get("CommandBlock_2").size());
    assertEquals(2, tester.commandParam.get("CommandBlock_3").size());
//    assertEquals(tester.makeTree(), results);
  }

  /**
   * make :number 5
   * make :order 3
   * make :x / - * 2 :order :number * :number 2
   * make :step 5
   *
   * dotimes [ :k * 360 :number ] [
   *   fd :step
   *   rt + :k :x
   * ]
   */
  @Test
  void testRoseCommand() {
    CommandParser tester = makeParser("make :number 5\n"
        + "make :order 3\n"
        + "make :x / - * 2 :order :number * :number 2\n"
        + "make :step 5\n"
        + "\n"
        + "dotimes [ :k * 360 :number ] [ \n"
        + "  fd :step\n"
        + "  rt + :k :x\n"
        + "]", "English");
    assertEquals(2, tester.commandParam.get("CommandBlock_1").size());
    assertEquals(2, tester.commandParam.get("CommandBlock_2").size());
  }


  /**
   * Tests one parameters count
   */
  void testOneWrongCommand() {
    CommandParser tester = makeParser("fd 50 60", "English");
    TreeNode root = tester.makeTree();
    List<String> results = new ArrayList<>();
    results.add(null);
    results.add("Forward");
    results.add("50");
    assertEquals(results, tester.preOrderResults);
  }

  /**
   * Tests multiple parameters count
   */
  @Test
  void testMultCommand() {
    String userInput = "forward 50 back 50 ";
    CommandParser tester = makeParser(userInput, "English");
    TreeNode root = tester.makeTree();
    List<String> results = new ArrayList<>();
    results.add(null);
    results.add("Forward");
    results.add("50");
    results.add("Backward");
    results.add("50");
    assertEquals(results, tester.preOrderResults);
  }

  /**
   * Tests multiple parameters count
   */
  @Test
  void testMultParamCommand() {
    String userInput = "Sum 50 50 ";
    CommandParser tester = makeParser(userInput, "English");
    TreeNode root = tester.makeTree();
    List<String> results = new ArrayList<>();
    results.add(null);
    results.add("Sum");
    results.add("50");
    results.add("50");
    assertEquals(results, tester.preOrderResults);
  }

  /**
   * Tests multiple parameters count
   */
  @Test
  void testBracketCommand() {
    String userInput = "To x [ :dist ] [ sum :dist 5 ] ";
    CommandParser tester = makeParser(userInput, "English");
    TreeNode root = tester.makeTree();
    List<String> results = new ArrayList<>();
    results.add(null);
    results.add("MakeUserInstruction");
    results.add("x");
    results.add("CommandBlock_1");
    results.add(":dist");
    results.add("CommandBlock_2");
    results.add("Sum");
    results.add(":dist");
    results.add("5");
    assertEquals(results, tester.preOrderResults);
    assertEquals(1, tester.commandParam.get("CommandBlock_1").size());
    assertEquals(1, tester.commandParam.get("CommandBlock_2").size());
  }

  /**
   * Tests one parameters count
   */
  @Test
  void testVariable() {
    CommandParser tester = makeParser(":size 50", "English");
    TreeNode root = tester.makeTree();
    List<String> results = new ArrayList<>();
    results.add(null);
    results.add(":size");
    results.add("50");
    assertEquals(results, tester.preOrderResults);
  }

  /**
   * Tests wrong param input
   */
  @Test
  void testWrongNumParam() {
    String error = null;
    try {
      CommandParser tester = makeParser("to 5 6", "English");
      tester.makeTree();
    } catch (Exception e) {
      error = e.getMessage();
    }
    assertEquals(error, "WrongParamNum");
  }

  /**
   * Tests wrong param input. correct list types but command is missing parameter count
   */
  @Test
  void testWrongNumParamComplex() {
    String error = null;
    try {
      CommandParser tester = makeParser("to x [ :y ] [ sum 50 ]", "English");
      tester.makeTree();
    } catch (Exception e) {
      error = e.getMessage();
    }
    assertEquals(error, "WrongParamNum");
  }



  /**
   * Tests wrong input type where brackets are misused
   */
  @Test
  void testWrongParamInput() {
    String error = null;
    try{
      CommandParser tester = makeParser("sum [ fd 50 ]", "English");
    } catch (Exception e) {
      error = e.getMessage();
    }
    assertEquals(error, "WrongParamNum");
  }


  private CommandParser makeParser(String userInput, String language) {
    BackEndExternalAPI modelController = new ModelController();
    CommandParser commandParser = new CommandParser(userInput, language, modelController);
    return commandParser;
  }

}
