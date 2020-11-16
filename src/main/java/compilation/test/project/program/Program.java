package compilation.test.project.program;

import compilation.test.project.queue.*;
import javax.tools.*;
import com.google.gson.*;
import java.io.*;
import java.util.*;

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

    public static void run(String inputJson) {
        //BufferedReader reader;
        PathsJson pathsJson = new PathsJson();
        try {
            //reader = new BufferedReader(new FileReader(new File(new File(".").getAbsolutePath().replace(".", File.separator) + "json_files" + File.separator + "paths.json")));
            Gson gson = new Gson();
            //pathsJson = gson.fromJson(reader, PathsJson.class);
            pathsJson = gson.fromJson(inputJson, PathsJson.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String javaFilePath = pathsJson.getStudent_answer().replace("/", File.separator);
        String file = javaFilePath.substring(javaFilePath.lastIndexOf(File.separator) + 1);
        String path = javaFilePath.substring(0, javaFilePath.lastIndexOf(File.separator));
        String jsonTestPath = pathsJson.getTests().replace("/", File.separator);
        Compiler compileProgram = new Compiler(path, file);
        Executer executeProgram = new Executer(path, file);
        Map<Integer, String[]> testErrors;
        String outputJson = "";
        if (compileProgram.getTaskCall()) {
            System.out.println("Compilation is successful");
            try {
                //System.gc();
                testErrors = Tester.getTests(executeProgram, jsonTestPath);
                if (testErrors != null) {
                    outputJson = String.format("{ \"Errors\":[%s], \"Resources\":{\"time\":\"%dms\", \"memory\":%d}, \"TestErrors\":{\"num_test\":%d, \"input\":\"%s\", \"output\":\"%s\"}, \"answer_id\":%d}",
                            new ArrayList<String[]>(testErrors.values()).get(0)[0], executeProgram.getExecuteTime(), executeProgram.getExecuteMemory(), new ArrayList<Integer>(testErrors.keySet()).get(0),
                            new ArrayList<String[]>(testErrors.values()).get(0)[1], new ArrayList<String[]>(testErrors.values()).get(0)[2], pathsJson.getAnswer_id());
                }
                else {
                    outputJson = String.format("{ \"Errors\":[], \"Resources\":{}, \"TestErrors\":{}, \"answer_id\":%d}", pathsJson.getAnswer_id());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            outputJson = "{ \"Errors\":[";
            int k = 0;
            for (Diagnostic<? extends JavaFileObject> diagnostic : compileProgram.getDiagnosticsList()) {
                if (k++ > 0) {
                    outputJson += ", ";
                }
                System.out.println("----------------------------------");
                System.out.println("Code: " + diagnostic.getCode());
                System.out.println("Column Number: " + diagnostic.getColumnNumber());
                System.out.println("End Position: " + diagnostic.getEndPosition());
                System.out.println("Kind: " + diagnostic.getKind());
                System.out.println("Line Number: " + diagnostic.getLineNumber());
                System.out.println("Message: " + diagnostic.getMessage(null));
                System.out.println("Position: " + diagnostic.getPosition());
                System.out.println("Source: " + diagnostic.getSource());
                System.out.println("Start Position: " + diagnostic.getStartPosition());
                outputJson += String.format("\"%s Message: %s Line: %s Column: %s\"",
                        diagnostic.getCode(), diagnostic.getMessage(null), diagnostic.getLineNumber(), diagnostic.getColumnNumber());
            }
            outputJson += String.format("], \"Resources\":{}, \"TestErrors\":{}, \"answer_id\":%d}", pathsJson.getAnswer_id());
            System.out.println("----------------------------------");
            System.out.println("Compilation is not successful");
        }
        try {
            Send.sendMessage(outputJson);
        }
        catch (Exception e) { }
        //System.out.println(outputJson);
        System.out.println("All memory: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / MBYTE));
    }
}

class PathsJson {
    private String student_answer;
    private String required_form;
    private String tests;
    private int answer_id;

    public String getStudent_answer() { return student_answer; }
    public String getRequired_form() {return required_form; }
    public String getTests() { return tests; }
    public int getAnswer_id() { return answer_id; }
}
