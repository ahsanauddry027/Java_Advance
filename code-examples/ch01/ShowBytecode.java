import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.spi.ToolProvider;

/** Asks the JDK's javap tool to show the bytecode that javac produced for add(). */
public class ShowBytecode {

    // region add
    static int add(int a, int b) {
        return a + b;
    }
    // endregion add

    public static void main(String[] args) {
        ToolProvider javap = ToolProvider.findFirst("javap").orElseThrow();
        StringWriter disassembly = new StringWriter();
        String classPath = System.getProperty("java.class.path");
        javap.run(new PrintWriter(disassembly), new PrintWriter(System.err),
                "-c", "-cp", classPath, "ShowBytecode");

        boolean inAdd = false;  // print only the part of javap's output about add()
        for (String line : disassembly.toString().split("\\R")) {
            if (line.contains("static int add(int, int)")) {
                inAdd = true;
            } else if (inAdd && line.isBlank()) {
                break;
            }
            if (inAdd) {
                System.out.println(line);
            }
        }
    }
}
