package code.two;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.*;
import static java.lang.System.out;

public class Problem2 {

    public static void main(String[] args) {
        if(args.length < 2){
            err.println("Input array required. ");
            out.println("Usage:  ");
            out.println("java code.two.Problem2 \"1,2,3,4,5,6\" 7");
        }
        int target = Integer.parseInt( args[1]);
        int[] input = convertStringToIntArray(args[0]);
        Problem2 problem = new Problem2();
        List<List<Integer>> result = problem.process(input, target);
        out.println("Distinct pairs of these integers that sum to " + target);
        for (List<Integer> rs : result) {
            out.println(rs);
        }
    }

    private static int[] convertStringToIntArray(String arg) {
        String[] inputListStr = arg.split(",");
        int[] input = new int[inputListStr.length];
        try {
            for (int i = 0; i < inputListStr.length; i++) {
                input[i] = Integer.parseInt(inputListStr[i]);
            }
        }catch (NumberFormatException nfe){
            err.println("Bad input");
            throw nfe;
        }
        return input;
    }

    public List<List<Integer>> process(int[] inputArray, int target) {
        Set<Integer> input = convertArrayToSortedSet(inputArray);
        List<List<Integer>> result = new ArrayList<>();

        for (int v1 : inputArray) {
            int v2 = target - v1;
            if( input.contains(v2)){
                input.remove(v2);
                input.remove(v1);
                result.add( Arrays.asList(v1, v2 ));
            }
        }
        return result;
    }

    private Set<Integer> convertArrayToSortedSet(int[] arr) {
        if (arr == null) arr = new int[]{};
        Arrays.sort(arr);
        return IntStream.of(arr)
                .boxed()
                .collect(Collectors.toSet());
    }
}
