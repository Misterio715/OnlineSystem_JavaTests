package testing.programs;

import java.util.*;

public class InfiniteLoop {
    public static void main(String[] args) {
        //long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        //long startTime = System.currentTimeMillis();
        List<Integer> list = new ArrayList<Integer>();
        int k = 0;
        long sum = 0;
        list.add(k);
        while (true) {
            list.add(++k);
            sum += k;
            //System.out.println(list.get(k));
        }
        //long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        //long endTime = System.currentTimeMillis();
        //System.out.println("sum = " + sum + "\nExecution information from program" + "\nRun time: " + (endTime - startTime) + "\nUsed memory: " + (endMemory - startMemory) / 1048576);
    }
}