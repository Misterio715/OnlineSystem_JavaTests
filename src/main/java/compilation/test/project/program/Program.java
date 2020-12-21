package compilation.test.project.program;

import compilation.test.project.main.Starter;
import compilation.test.project.queue.*;
import javax.tools.*;
import com.google.gson.*;
import java.io.*;
import java.util.*;

public class Program {
    public final static String MEMORY_ERROR = "Error occurred during initialization of VM\n" +
            "Too small initial heap";
    public final static int MBYTE = 1024 * 1024;
    public static int ID;

    public static void run(String inputJson) {
        //BufferedReader reader;
        PathsJson pathsJson = new PathsJson();
        try {
            //reader = new BufferedReader(new FileReader(new File(new File(".").getAbsolutePath().replace(".", File.separator) + "json_files" + File.separator + "paths.json")));
            Gson gson = new Gson();
            //pathsJson = gson.fromJson(reader, PathsJson.class);
            pathsJson = gson.fromJson(inputJson, PathsJson.class);
        } catch (Exception e) {
            System.out.println("Json path file parse error");
        }

        ID = pathsJson.getAnswer_id();
        String javaFilePath = pathsJson.getStudent_answer().replace("/", File.separator);
        //String javaFileName = new File(javaFilePath, new File(javaFilePath).list()[0]).getName();
        //javaFileName = javaFileName.substring(0, javaFileName.lastIndexOf("."));
        String fileName = javaFilePath.substring(javaFilePath.lastIndexOf(File.separator) + 1);
        String pathName = javaFilePath.substring(0, javaFilePath.lastIndexOf(File.separator));
        String jsonTestPath = pathsJson.getTests().replace("/", File.separator);

        Compiler compileProgram = new Compiler(pathName, fileName);
        Executer executeProgram = new Executer(pathName, fileName, pathsJson.getTime_limit(), pathsJson.getMemory_limit());

        String outputJson = null;

        if (compileProgram.getTaskCall()) {
            System.out.println("Compilation is successful");
            //System.gc();
            try {
                outputJson = String.format("{\"status\":\"%s\",\"answer_id\":%d}", Tester.getTests(executeProgram, jsonTestPath), ID);
            }
            catch (NullPointerException e) {
                System.out.println("Null output error");
                try {
                    Starter.sender.sendMessage(String.format("{\"status\":\"Null output error\",\"answer_id\":%d}", Program.ID));
                }
                catch (Exception ex) { }
            }
            if (new File(javaFilePath + ".class").delete()) {
                System.out.println(javaFilePath + ".class file deleted");
            }
            else {
                System.out.println(javaFilePath + ".class file not deleted");
            }
        }
        else {
            outputJson = "{\"status\":\"";
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
                outputJson += String.format("%s, Line %d", diagnostic.getMessage(null), diagnostic.getLineNumber());
            }
            outputJson += String.format("\",\"answer_id\":%d}", ID);
            System.out.println("----------------------------------");
            System.out.println("Compilation is not successful");
        }
        try {
            if (outputJson != null) {
                Starter.sender.sendMessage(outputJson);
            }
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
    private Long time_limit;
    private Long memory_limit;
    private int answer_id;

    public String getStudent_answer() { return student_answer; }
    public String getRequired_form() {return required_form; }
    public String getTests() { return tests; }
    public Long getTime_limit() { return time_limit; }
    public Long getMemory_limit() { return memory_limit; }
    public int getAnswer_id() { return answer_id; }
}