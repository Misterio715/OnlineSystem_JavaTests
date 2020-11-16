package compilation.test.project.program;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Executer {
    private String path;
    private String file;
    private String input;
    private long memory;
    private long time;
    private List<String> errorList = new ArrayList<String>();
    private final static int MBYTE = 1024 * 1024;

    public Executer(String path, String file, String input) {
        this.path = path;
        this.file = file;
        this.input = input;
    }
    public Executer(String path, String file) {
        this.path = path;
        this.file = file;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public long getExecuteMemory() { return  memory / MBYTE; }

    public long getExecuteTime() { return time; }

    public String getErrors() { return concat(errorList); }

    private long getMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    private String concat(List<String> list) {
        String output = "";
        int k = 0;
        for (String i : list) {
            if (k++ > 0) {
                output += " ";
            }
            output += i;
        }
        return output;
    }

    public String executeProcess() throws IOException {
        String commands = "java " + "-cp " + path + " " + file;
        //System.out.println(commands);
        Process process;
        BufferedReader outputReader, errorReader;
        String line, error;
        List<String> outputList = new ArrayList<String>();
        //List<String> errorList = new ArrayList<String>();
        //--------------------------------------------------------------
        //System.gc();
        long startMemory = getMemory();
        long startTime = System.currentTimeMillis();
        process = Runtime.getRuntime().exec(commands);
        Writer writer = new OutputStreamWriter(process.getOutputStream());
        writer.write(input);
        writer.close();
        outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((line = outputReader.readLine()) != null) {
            outputList.add(line);
        }
        while ((error = errorReader.readLine()) != null) {
            errorList.add(error);
        }
        time = System.currentTimeMillis() - startTime;
        memory = getMemory() - startMemory;
        //--------------------------------------------------------------
        /*if (errorList.isEmpty()) {
            //System.out.println(outputList);
            //System.out.println("Expected execution information" + "\nRun time: " + time + "\nUsed memory: " + ((memory) / MBYTE));
            return concat(outputList);
        }
        else {
            //System.out.println(errorList);
            //System.out.println("Expected execution information" + "\nRun time: " + time + "\nUsed memory: " + ((memory) / MBYTE));
            return concat(errorList);
        }*/
        return concat(outputList);
    }
}