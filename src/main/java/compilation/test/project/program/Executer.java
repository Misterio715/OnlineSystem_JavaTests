package compilation.test.project.program;

import compilation.test.project.main.Starter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class Executer {
    private String path;
    private String file;
    private Long memoryLimit;
    private Long timeLimit;
    private long memory;
    private long time;
    private List<String> errorList = new ArrayList<String>();
    private final static int MBYTE = 1024 * 1024;

    public Process process;
    public boolean threadInterrupt = false;

    public Executer(String path, String file, Long timeLimit, Long memoryLimit) {
        this.path = path;
        this.file = file;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
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
                output += "\n";
            }
            output += i;
        }
        return output;
    }

    public String executeProcess(String input) throws RuntimeException, IOException {
        String xmx = memoryLimit == null ? "" : "-Xmx" + memoryLimit;
        String commands = "java " + xmx + " -cp " + path + " " + file;
        System.out.println(commands);

        BufferedReader outputReader, errorReader;
        String line, error;
        List<String> outputList = new ArrayList<String>();
        List<String> errorList = new ArrayList<String>();

        long startMemory = getMemory();
        long startTime = System.currentTimeMillis();

        threadInterrupt = false;
        Callable task = new Callable() {
            @Override
            public Object call() throws Exception {
                long curTime = System.currentTimeMillis() - startTime;
                long _timeLimit = timeLimit == null ? Long.MAX_VALUE : System.currentTimeMillis() + timeLimit;
                process = Runtime.getRuntime().exec(commands);
                while (!Thread.currentThread().isInterrupted()) {
                    if ((curTime = System.currentTimeMillis()) >= _timeLimit) {
                        //Thread.currentThread().interrupt();
                        threadInterrupt = true;
                        System.out.println("thread " + Thread.currentThread().isInterrupted());
                        process.destroy();
                        return curTime;
                    }
                }
                return curTime;
            }
        };
        FutureTask<Long> futureTask = new FutureTask(task);
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            TimeUnit.MILLISECONDS.sleep(5);
        }
        catch (Exception e) {
            try {
                Starter.sender.sendMessage(String.format("{\"status\":\"Java service error\",\"answer_id\":%d}", Program.ID));
            }
            catch (Exception ex) { }
        }

        if (process.isAlive()) {
            Writer writer = new OutputStreamWriter(process.getOutputStream());
            writer.write(input);
            writer.close();
            outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = outputReader.readLine()) != null) {
                outputList.add(line);
            }
        }
        memory += getMemory() - startMemory;
        try {
            if (!threadInterrupt) {
                thread.interrupt();
                time = futureTask.get() - startTime;
                System.out.println("Expected execution information" + "\nRun time: " + time + "\nUsed memory: " + ((memory) / MBYTE));
            }
            else {
                time = futureTask.get() - startTime;
                System.out.println("Expected execution information" + "\nRun time: " + time + "\nUsed memory: " + ((memory) / MBYTE));
                return "TIME";
            }
        }
        catch (Exception e) {
            System.out.println("Execute thread error");
        }

        errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((error = errorReader.readLine()) != null) {
            errorList.add(error);
        }
        if (errorList.isEmpty()) {
            return concat(outputList);
        }
        else if (errorList.get(0).contains("Exception in thread \"main\" java.lang.OutOfMemoryError")){
            return "MEMORY";
        }
        else {
            return null;
        }
    }
}