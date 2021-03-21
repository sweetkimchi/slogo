package slogo.model.commandParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slogo.ErrorHandler;
import slogo.controller.ModelController;
import slogo.model.parse.CommandParser;
import slogo.model.parse.InputCleaner;

public class CleanInputTest {

  /**
   * Sets up the commandParser
   */
  @BeforeEach
  void setUp() {

  }

  /**
   * Test specific command translation
   */
  @Test
  void testXCor() {
    String userInput = "xcor";
    InputCleaner cleaner = makeInputCleaner(userInput, "English");
    List<String> expected = new ArrayList<>();
    expected.add("XCoordinate");
    assertEquals(cleaner.parseResults(), expected);
  }

  /**
   * Test language translation
   */
  @Test
  void testLanguage() {
    String userInput = "# comment\nqianjin 50 ht 50 chongfu 5 [ qj 50 ]";
    InputCleaner cleaner = makeInputCleaner(userInput, "Chinese");
    List<String> expected = new ArrayList<>();
    expected.add("Forward");
    expected.add("50");
    expected.add("Backward");
    expected.add("50");
    expected.add("Repeat");
    expected.add("5");
    expected.add("CommandBlock_1");
    expected.add("Forward");
    expected.add("50");
    assertEquals(cleaner.parseResults(), expected);
  }

  /**
   * Test all steps (remove commands and group blocks
   */
  @Test
  void testCleaningAll() {
    String userInput = "if :size < 5 \n#comment\n[ forward :size back :size ]";
    InputCleaner cleaner = makeInputCleaner(userInput, "English");
    List<String> expected = new ArrayList<>();
    expected.add("If");
    expected.add(":size");
    expected.add("<");
    expected.add("5");
    expected.add("CommandBlock_1");
    expected.add("Forward");
    expected.add(":size");
    expected.add("Backward");
    expected.add(":size");
    assertEquals(cleaner.parseResults(), expected);
  }

  /**
   * Test all steps (remove commands and group blocks
   */
  @Test
  void testMultCommandBlocks() {
    String userInput = "if :size < 5 \n#comment\n[ forward :size back :size ] repeat 4 [ forward 5 ]";
    InputCleaner cleaner = makeInputCleaner(userInput, "English");
    List<String> expected = new ArrayList<>();
    expected.add("If");
    expected.add(":size");
    expected.add("<");
    expected.add("5");
    expected.add("CommandBlock_1");
    expected.add("Forward");
    expected.add(":size");
    expected.add("Backward");
    expected.add(":size");
    expected.add("Repeat");
    expected.add("4");
    expected.add("CommandBlock_2");
    expected.add("Forward");
    expected.add("5");
    assertEquals(cleaner.parseResults(), expected);
 }

  /**
   * Test two wrapped brackets
   */
  @Test
  void testTwoWrappedBrackets() {
    String userInput = "repeat 2 [ repeat 3 [ fd 100 ] ]";
    InputCleaner cleaner = makeInputCleaner(userInput, "English");
    List<String> expected = new ArrayList<>();
    expected.add("Repeat");
    expected.add("2");
    expected.add("CommandBlock_1");
    expected.add("Repeat");
    expected.add("3");
    expected.add("CommandBlock_2");
    expected.add("Forward");
    expected.add("100");
    assertEquals(cleaner.parseResults(), expected);
 }

  /**
   * Test mult wrapped brackets
   */
  @Test
  void testMultWrappedBrackets() {
    String userInput = "repeat 2 [ repeat 3 [ repeat 2 [ fd 100 ] ] ]";
    InputCleaner cleaner = makeInputCleaner(userInput, "English");
    List<String> expected = new ArrayList<>();
    expected.add("Repeat");
    expected.add("2");
    expected.add("CommandBlock_1");
    expected.add("Repeat");
    expected.add("3");
    expected.add("CommandBlock_2");
    expected.add("Repeat");
    expected.add("2");
    expected.add("CommandBlock_3");
    expected.add("Forward");
    expected.add("100");
    assertEquals(cleaner.parseResults(), expected);
  }

  /**
   * Test wrong num brackets
   */
  @Test
  void testWrongBrackets() {
    String userInput = "repeat 2 [ repeat 3 [ repeat 2 [ fd 100 ] ] ";
    InputCleaner cleaner = makeInputCleaner(userInput, "English");
    assertEquals(cleaner.parseResults(), new ErrorHandler("WrongParamNum"));
  }

  /**
   * Tests the Repeat command repeat 2 [ repeat 3 [ fd 100 ] ]
   */
  @Test
  void testUserDefCommands() {
    String input = "to Movement [ :distance ] [ fd :distance ]";
    InputCleaner cleaner = makeInputCleaner(input, "English");
    List<String> expected = new ArrayList<>();
    expected.add("MakeUserInstruction");
    expected.add("Movement");
    expected.add("CommandBlock_1");
    expected.add(":distance");
    expected.add("CommandBlock_2");
    expected.add("Forward");
    expected.add(":distance");
    assertEquals(expected, cleaner.parseResults());
  }

  /**
   * Tests the do times dotimes [ :size 10 ] [ fd :size right 5 ]
   */
  @Test
  void testDoTimes() {
    String input = "dotimes [ :size 10 ] [ fd :size right 5 ]";
    InputCleaner cleaner = makeInputCleaner(input, "English");
    List<String> expected = new ArrayList<>();
    expected.add("DoTimes");
    expected.add("CommandBlock_1");
    expected.add(":size");
    expected.add("10");
    expected.add("CommandBlock_2");
    expected.add("Forward");
    expected.add(":size");
    expected.add("Right");
    expected.add("5");
    assertEquals(expected, cleaner.parseResults());
  }

  /**
   * Tests the wrong input fd 50 60
   */
  @Test
  void testSimpleWrongInput() {
    String input = "fd 50 60";
    InputCleaner cleaner = makeInputCleaner(input, "English");
    assertEquals(new ErrorHandler("WrongParamNum"), cleaner.parseResults());
  }

  private InputCleaner makeInputCleaner(String userInput, String language) {
    ModelController modelController = new ModelController();
    InputCleaner cleaner = new InputCleaner(userInput, language);
    return cleaner;
  }


}

