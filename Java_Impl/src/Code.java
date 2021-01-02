import java.util.HashMap;
import java.util.Map;

/**
 * The Code class has all the methods for simulating the cd command of UNIX.
 */
public class Code {

  private final String currentDirectory;
  private String newDirectory;
  private final Map<String, Runnable> map;

  /**
   * The constructor instantiates an object of the code class with the current directory as one of
   * the parameters.
   *
   * @param currentDirectory the current directory
   */
  public Code(String currentDirectory) {
    if (currentDirectory == null || currentDirectory.isEmpty()) {
      throw new IllegalArgumentException("invalid current directory");
    }
    this.currentDirectory = currentDirectory;
    this.newDirectory = "";
    this.map = loadMap();
  }

  private Map<String, Runnable> loadMap() {
    Map<String, Runnable> temp = new HashMap<>();
    temp.put("", this::processEmpty);
    temp.put(".", this::processPeriod);
    temp.put("..", this::processParent);
    return temp;
  }

  /**
   * Processes the command to change the current directory to a new directory.
   *
   * @param cString the command to change the directory
   * @return the new directory
   */
  public String process(String cString) {
    if (cString.equals("")) {
      this.newDirectory = this.currentDirectory;
      return newDirectory;
    }
    cString = setNewDirectory(cString);
    String[] temp = new String[]{"", cString};
    String[] list = cString.contains("/") ? cString.split("/") : temp;
    for (int i = 1; i < list.length; i++) {
      if (map.containsKey(list[i])) {
        map.get(list[i]).run();
      } else {
        if (!(list[i].matches("[a-zA-Z0-9]*"))) {
          throw new IllegalArgumentException("No such file or Directory exists");
        }
        this.processDirectory(list[i]);
      }
    }
    return newDirectory;
  }

  private String setNewDirectory(String cString) {
    if (cString.startsWith("../") || cString.equals("..")) {
      String[] list = this.currentDirectory.split("/");
      String tempDirectory = '/' + list[list.length - 1];
      int index = currentDirectory.indexOf(tempDirectory);
      this.newDirectory = currentDirectory.substring(0, index);
      if (!(newDirectory.contains("/"))) {
        newDirectory = "/";
      }
      cString = cString.replaceFirst("..", "");
    } else if (cString.startsWith("./") || cString.equals(".")) {
      newDirectory = currentDirectory;
      cString = cString.replaceFirst(".", "");
    } else if (cString.startsWith("/")) {
      newDirectory = "/";
    } else {
      newDirectory = currentDirectory;
    }
    return cString;
  }


  private void processEmpty() {
    return;
  }

  private void processPeriod() {
    return;
  }

  private void processParent() {
    String[] list = this.newDirectory.split("/");
    String directory = "";
    if (list.length == 0) {
      directory = "/";
      
    } else {
      directory = "/" + list[list.length - 1];
    }
    int index = newDirectory.indexOf(directory);
    this.newDirectory = newDirectory.substring(0, index + 1);
    if (!(newDirectory.contains("/"))) {
      newDirectory = "/";
    }
  }

  private void processDirectory(String directory) {
    if (!(newDirectory.charAt(newDirectory.length() - 1) == '/')) {
      newDirectory = newDirectory + '/';
    }
    newDirectory = newDirectory + directory;
  }

  public static void main(String[] args) {
    String currentDirectory = "";
    String commandString = "";
    currentDirectory = args[0];
    commandString = args[1];
    Code code = new Code(currentDirectory);
    System.out.println(code.process(commandString));
  }
}
