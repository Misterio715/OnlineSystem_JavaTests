import java.io.*;
import java.util.*;
public class AdditionProgram {
	public static void main(String[] args) {
		long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long startTime = System.currentTimeMillis();
		Scanner in = new Scanner(System.in);
		int a, b, sum;
		a = Integer.parseInt(in.next());
		b = Integer.parseInt(in.next());
		sum = a + b;
		System.out.println(sum);
		long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long endTime = System.currentTimeMillis();
		System.out.println("sum = " + sum + "\nExecution information from program" + "\nRun time: " + (endTime - startTime) + "\nUsed memory: " + (endMemory - startMemory) / 1048576);
	}
}