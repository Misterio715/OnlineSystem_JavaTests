package compilation.test.project.program;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class Tester {
    public static Map<Integer, String[]> getTests(Executer executeProgram, String jsonTestPath) throws FileNotFoundException {
        TestsJson[] jsonForms = new TestsJson[0];

        //File file = new File(new File(".").getAbsolutePath().replace(".", File.separator) + "json_files" + File.separator + "test.json");
        File file = new File(jsonTestPath);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            Gson gson1 = new Gson();
            jsonForms = gson1.fromJson(reader, TestsJson[].class);
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        int n = jsonForms.length;
        int space;
        //int[] input1 = new int[n];
        //int[] input2 = new int[n];
        String[] input = new String[n];
        int[] output = new int[n];
        for(int i = 0; i < n; i++) {
            space = jsonForms[i].getInput().indexOf(" ");
            /*String helps1 = jsonForms[i].getInput().toString().substring(0, space);
            String helps2 = jsonForms[i].getInput().toString().substring(space + 1);
            input1[i] = Integer.parseInt(helps1);
            input2[i] = Integer.parseInt(helps2);*/
            input[i] = jsonForms[i].getInput();
            output[i] = Integer.parseInt(jsonForms[i].getOutput());

            executeProgram.setInput(input[i]);
            Map<Integer, String[]> map = new HashMap<Integer, String[]>();
            //System.out.println(input1[i] + " "+ input2[i]+ " "+ jsonForms[i].getOutput());
            try {
                String stringOutValue;
                Integer intOutValue = null;
                stringOutValue = executeProgram.executeProcess();
                try {
                    intOutValue = Integer.parseInt(stringOutValue);
                }
                catch (NumberFormatException e) {}
                if (intOutValue.equals(Integer.parseInt(jsonForms[i].getOutput()))) {
                    //System.out.println("Test№" + (i+1) + " " + input1[i] + " " + input2[i]+ " " + Integer.parseInt(jsonForms[i].getOutput()));
                    System.out.println("\nTest N" + (i + 1) + " " + input[i] + " | " + Integer.parseInt(jsonForms[i].getOutput()) +
                            "\nExecution time: " + executeProgram.getExecuteTime() + "\nMemory used: " + executeProgram.getExecuteMemory() + "\nTest right!");
                }
                else {
                    map.put(i + 1, new String[]{executeProgram.getErrors(), input[i], jsonForms[i].getOutput()});
                    System.out.println("\nTest N" + (i + 1) + " wrong answer\n" + "We got : " + stringOutValue + " expected :" + Integer.parseInt(jsonForms[i].getOutput()) + "\n");
                    return map;
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
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
