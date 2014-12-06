package ee.ut.ti2014s.algox;

import java.util.*;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AlgoXTest {

	/**
	 * Test using same dataset as wiki and most other examples
	 * X={1,2,3,4,5,6,7}
	 * Y={A:{1,4,7},B:{1,4},C:{4,5,7},D:{3,5,6},E:{2,3,6,7},F:{2,7}}
	 *
	 * Solution: {B,D,F}
	 *
	 * Rows are also given indices instead of letters
	 * A=0
	 * B=1
	 * C=2
	 * D=3
	 * E=4
	 * F=5
	 */
	@Test
	public void testWikiTestcase() {
		List<Integer> X = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7});
		Map<Integer, List<Integer>> Y = new HashMap<>();
		Y.put(0, Arrays.asList(new Integer[]{1, 4, 7}));	//A
		Y.put(1, Arrays.asList(new Integer[]{1, 4}));		//B
		Y.put(2, Arrays.asList(new Integer[]{4, 5, 7}));	//C
		Y.put(3, Arrays.asList(new Integer[]{3, 5, 6}));	//D
		Y.put(4, Arrays.asList(new Integer[]{2, 3, 6, 7}));	//E
		Y.put(5, Arrays.asList(new Integer[]{2, 7}));		//F

		List<int[]> solutions = AlgoX.solve(X, Y);

		int[] expected = new int[]{1, 3, 5};

		assertEquals(1, solutions.size());

		Assert.assertArrayEquals(expected, solutions.get(0));
	}
}
