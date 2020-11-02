package compilation.test.project.program;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.util.*;
import java.io.*;

public class CompilationProgram {
    //путь до папки с программами
    final static String PATH = "D:" + File.separator + "Tests";
    //правильно написанная программа
    final static String CORRECT_TEST = "Test1";
    //программа с ошибками
    final static String FAIL_TEST = "Test2";
    //программа сложения двух целых чисел
    final static String ADD_TEST = "AdditionProgram";

    //метод executeProcess() исполняет выбранный .class файл в выбранной файловой директории
    public static void executeProcess(String file, String path) throws Exception {
        String commands;
        Process process = Runtime.getRuntime().exec(commands = "java -cp " + path + " " + file);
        System.out.println(commands);
        //передача входных данных программе
        Writer writer = new OutputStreamWriter(process.getOutputStream());
        writer.write(new char[]{'2', ' ', '3'});
        writer.close();
        //считывание выходного потока
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line, output = "";
        while ((line = bufferedReader.readLine()) != null) {
            output += line;
            //System.out.println(line);
        }
        //output - строка с выходными данными
        System.out.println(output);
    }

    public static void main(String[] args) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        String fileForTest = ADD_TEST;
        Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(PATH + File.separator + fileForTest + ".java"));
        CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, null, null, fileObjects);
        //создаем список для возможных ошибок компиляции
        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticCollector.getDiagnostics();
        //проверка успешности компиляции заданного .java файла
        //метод call() компилирует заданный файл и возвращает булево значение
        //true - компиляция прошла успешно, false - компиляции не прошла
        if (task.call()) {
            System.out.println("Successful");
            try {
                //в случае успешной компиляции вызывается метод для исполнения скомпилированного файла
                executeProcess(fileForTest, PATH);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            //если компиляция не прошла, в цикле выдается список ошибок
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
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
            System.out.println("Unsuccessful");
        }
    }
}