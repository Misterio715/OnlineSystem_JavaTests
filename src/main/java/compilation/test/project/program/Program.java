package compilation.test.project.program;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.util.*;
import java.io.*;

public class Program {
    //путь до папки с программами
    final static String PATH = new File(".").getAbsolutePath().replace(".", File.separator) + "tests";
    //правильно написанная программа
    final static String CORRECT_TEST = "Test1";
    //программа с ошибками
    final static String FAIL_TEST = "Test2";
    //программа сложения двух целых чисел
    final static String ADD_TEST = "AdditionProgram";
    //программа с бесконечным циклом
    final static String INF_LOOP = "InfiniteLoop";
    final static int MBYTE = 1024 * 1024;

    public static void main(String[] args) {
        String file = ADD_TEST, input = "3 4";
        Compiler compileProgram = new Compiler(PATH, file);
        List<String> outputValues;
        if (compileProgram.getTaskCall()) {
            System.out.println("Compilation is successful");
            try {
                //System.gc();
                Executer executeProgram = new Executer(PATH, file, input);
                outputValues = executeProgram.executeProcess();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            for (Diagnostic<? extends JavaFileObject> diagnostic : compileProgram.getDiagnosticsList()) {
                System.out.println("----------------------------------");
                System.out.println("Code: " +  diagnostic.getCode());
                System.out.println("Column Number: " + diagnostic.getColumnNumber());
                System.out.println("End Position: " + diagnostic.getEndPosition());
                System.out.println("Kind: " + diagnostic.getKind());
                System.out.println("Line Number: " + diagnostic.getLineNumber());
                System.out.println("Message: "+ diagnostic.getMessage(null));
                System.out.println("Position: " + diagnostic.getPosition());
                System.out.println("Source: " + diagnostic.getSource());
                System.out.println("Start Position: " + diagnostic.getStartPosition());
            }
            System.out.println("----------------------------------");
            System.out.println("Compilation is not successful");
        }
        System.out.println("All memory: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / MBYTE));
    }
}