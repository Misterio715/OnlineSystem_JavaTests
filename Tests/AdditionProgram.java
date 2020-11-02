import java.io.*;
import java.util.*;
public class AdditionProgram {
	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		int a, b, sum;
		a = Integer.parseInt(in.next());
		b = Integer.parseInt(in.next());
		sum = a + b;
		System.out.println(sum);
	}
}