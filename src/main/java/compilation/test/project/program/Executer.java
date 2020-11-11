package compilation.test.project.program;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Executer {
    private String path;
    private String file;
    private String input;
    private long memory;
    private long time;
    private final static int MBYTE = 1024 * 1024;

    public Executer(String path, String file, String input) {
        this.path = path;
        this.file = file;
        this.input = input;
    }

    public long getExecuteMemory() {
        return  memory;
    }

    public long getExecuteTime() {
        return time;
    }

    private long getMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public List<String> executeProcess() throws Exception {
        String commands = "java " + "-cp " + path + " " + file;
        Process process;
        BufferedReader outputReader, errorReader;
        String line, error;
        List<String> outputList = new ArrayList<String>();
        List<String> errorList = new ArrayList<String>();
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
        System.out.println(commands);
        while ((line = outputReader.readLine()) != null) {
            outputList.add(line);
        }
        while ((error = errorReader.readLine()) != null) {
            errorList.add(error);
        }
        time = System.currentTimeMillis() - startTime;
        memory = getMemory() - startMemory;
        //--------------------------------------------------------------
        if (errorList.isEmpty()) {
            System.out.println(outputList);
            System.out.println("Expected execution information" + "\nRun time: " + time + "\nUsed memory: " + ((memory) / MBYTE));
            return outputList;
        }
        else {
            System.out.println(errorList);
            System.out.println("Expected execution information" + "\nRun time: " + time + "\nUsed memory: " + ((memory) / MBYTE));
            return errorList;
        }
    }
}
