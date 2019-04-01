import code.two.Problem2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Problem2Test {

    @Test
    public void testDistinctCombinationsSumTo7() {
        Problem2 p2 = new Problem2();
        List<List<Integer>> expectedResult = new ArrayList<>();
        expectedResult.add(Arrays.asList(1, 6));
        expectedResult.add(Arrays.asList(2, 5));
        expectedResult.add(Arrays.asList(3, 4));
        int[] arr = new int[]{1, 2, 3, 4, 5, 6};
        List<List<Integer>> result = p2.process(arr, 7);
        validate(expectedResult, result);
    }

    @Test
    public void testDistinctCombinationsSumTo7WithNegativeNumbers() {
        Problem2 p2 = new Problem2();
        List<List<Integer>> expectedResult = new ArrayList<>();
        expectedResult.add(Arrays.asList(-3, 10));
        expectedResult.add(Arrays.asList(1, 6));
        expectedResult.add(Arrays.asList(2, 5));

        int[] arr = new int[]{10,1,2,7,6,1,5,-3};
        List<List<Integer>> result = p2.process(arr, 7);
        validate(expectedResult, result);

    }

    private void validate(List<List<Integer>> expectedResult, List<List<Integer>> result) {
        Assert.assertEquals("Matching pair count:", expectedResult.size(), result.size());

        for (int i = 0; i < expectedResult.size(); i++) {
            Assert.assertEquals("Must be pair", 2, result.get(i).size());
            Assert.assertEquals(result.get(i).get(0), expectedResult.get(i).get(0));
            Assert.assertEquals(result.get(i).get(1), expectedResult.get(i).get(1));
        }
    }


}