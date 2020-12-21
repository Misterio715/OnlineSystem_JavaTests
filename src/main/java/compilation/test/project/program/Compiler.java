package compilation.test.project.program;

import compilation.test.project.main.Starter;

import javax.tools.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Compiler {
    private JavaCompiler compiler;
    private DiagnosticCollector<JavaFileObject> diagnosticCollector;
    private StandardJavaFileManager fileManager;
    private Iterable<? extends JavaFileObject> fileObjects;

    public Compiler(String path, String file) {
        try {
            Starter.sender.sendMessage(String.format("{\"status\":\"Compiling\",\"answer_id\":%d}", Program.ID));
        }
        catch (Exception e) {}
        compiler = ToolProvider.getSystemJavaCompiler();
        diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
        fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        fileObjects = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(path + File.separator + file + ".java"));
    }

    public boolean getTaskCall() {
        return compiler.getTask(null, fileManager, diagnosticCollector, null, null, fileObjects).call();
    }

    public List<Diagnostic<? extends JavaFileObject>> getDiagnosticsList() {
        return diagnosticCollector.getDiagnostics();
    }
}