package slogo.model.tokens;


import java.util.regex.Pattern;
import slogo.model.CommandParser;

public class VariableList extends ListToken {

  private Pattern VARIABLE_REGEX;

  public VariableList(String command) {
    super(command);
  }

  @Override
  public int incrementParamCount(int blockSize, String command) {
    return blockSize++;
  }
}
