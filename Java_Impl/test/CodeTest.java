import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class CodeTest {

  Code code;
  String currentDirectory = "/abc/def";

  @Before
  public void setUp() {
    code = new Code(currentDirectory);
  }

  @Test
  public void process1() {
    String cString = "//";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/");
  }

  @Test
  public void process2() {
    String cString = "/..";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/");
  }

  @Test
  public void process3() {
    String cString = "..";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/abc");
  }

  @Test
  public void process4() {
    String cString = ".";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, currentDirectory);
  }

  @Test
  public void process5() {
    String cString = "../..";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/");
  }

  @Test
  public void process6() {
    String cString = "../../../..";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/");
  }

  @Test(expected = IllegalArgumentException.class)
  public void process7() {
    String cString = "..klm";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/abc");
  }

  @Test(expected = IllegalArgumentException.class)
  public void process8() {
    String cString = "....";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "");
  }

  @Test
  public void process9() {
    String cString = "/////";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/");
  }

  @Test
  public void process10() {
    String cString = "/../////";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/");
  }

  @Test
  public void process11() {
    String cString = "ghi";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/abc/def/ghi");
  }

  @Test
  public void process12() {
    String cString = "def";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/abc/def/def");
  }

  @Test
  public void process13() {
    String cString = "../gh///../klm/";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/abc/klm");
  }

  @Test(expected = IllegalArgumentException.class)
  public void process14() {
    String cString = "...";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "");
  }

  @Test
  public void process15() {
    String cString = "../gh///../klm/../.";
    String newDirectory = code.process(cString);
    assertEquals(newDirectory, "/abc/");
  }

}