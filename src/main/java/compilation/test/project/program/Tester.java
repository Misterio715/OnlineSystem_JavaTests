package compilation.test.project.program;

import com.google.gson.*;
import compilation.test.project.main.Starter;

import java.io.*;
import java.util.*;

public class Tester {
    public static String getTests(Executer executeProgram, String jsonTestPath) {
        TestsJson[] jsonForms = new TestsJson[0];

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File(jsonTestPath)));
            Gson gson1 = new Gson();
            jsonForms = gson1.fromJson(reader, TestsJson[].class);
        }
        catch (Exception e) {
            System.out.println("Json test file parse error");
        }
        int n = jsonForms.length;
        String[] input = new String[n];
        String[] output = new String[n];
        try {
            Starter.sender.sendMessage(String.format("{\"status\":\"Running tests\",\"answer_id\":%d}", Program.ID));
        }
        catch (Exception e) {}
        for (int i = 0; i < n; i++) {
            input[i] = jsonForms[i].getInput();
            output[i] = jsonForms[i].getOutput();

            //executeProgram.setInput(input[i]);
            Map<Integer, String[]> map = new HashMap<Integer, String[]>();

            String stringOutValue = null;
            try {
                stringOutValue = executeProgram.executeProcess(input[i]);
            }
            catch (RuntimeException e) {
                System.out.println("Executer runtime error");
                try {
                    Starter.sender.sendMessage(String.format("{\"status\":\"Runtime error\",\"answer_id\":%d}", Program.ID));
                }
                catch (Exception ex) { }
            }
            catch (IOException e) {
                System.out.println("Executer IO error");
                try {
                    Starter.sender.sendMessage(String.format("{\"status\":\"IO error\",\"answer_id\":%d}", Program.ID));
                }
                catch (Exception ex) { }
            }
            finally {
                assert stringOutValue != null;
                if (stringOutValue.equals(output[i])) {
                    System.out.println("\nTest N" + (i + 1) + " " + input[i] + " | " + output[i] + " right answer" +
                            "\nExecution time: " + executeProgram.getExecuteTime() + "\nMemory used: " + executeProgram.getExecuteMemory() + "\n");
                } else if (stringOutValue.equals("TIME")) {
                    System.out.println("\nTest N" + (i + 1) + " Time limit error\n");
                    return "Time limit error in test N" + (i + 1);
                } else if (stringOutValue.equals(Program.MEMORY_ERROR) | stringOutValue.equals("MEMORY")) {
                    System.out.println("\nTest N" + (i + 1) + " memory limit error" + "\n");
                    return "Memory limit error in test N" + (i + 1);
                } else {
                    System.out.println("\nTest N" + (i + 1) + " wrong answer\n" + "We got : " + stringOutValue + " expected :" + output[i] + "\n");
                    return "Wrong answer in test N" + (i + 1);
                }
            }
        }
        return "All tests passed";
    }
}

class TestsJson {
    private String input;
    private String output;

    public String getInput() {
        return input;
    }
    public String getOutput() {
        return output;
    }
}