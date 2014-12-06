package ee.ut.ti2014s.sudoku;

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SudokuTest {

	@Test
	public void testSimpleSudoku() {
		List<Integer> sudokuBoard = Arrays.asList(
			5, 3, 0, 0, 7, 0, 0, 0, 0,
			6, 0, 0, 1, 9, 5, 0, 0, 0,
			0, 9, 8, 0, 0, 0, 0, 6, 0,
			8, 0, 0, 0, 6, 0, 0, 0, 3,
			4, 0, 0, 8, 0, 3, 0, 0, 1,
			7, 0, 0, 0, 2, 0, 0, 0, 6,
			0, 6, 0, 0, 0, 0, 2, 8, 0,
			0, 0, 0, 4, 1, 9, 0, 0, 5,
			0, 0, 0, 0, 8, 0, 0, 7, 9);

		SudokuSolver s = new SudokuSolver(sudokuBoard);

		List<Integer> expected = Arrays.asList(
			5, 3, 4, 6, 7, 8, 9, 1, 2,
			6, 7, 2, 1, 9, 5, 3, 4, 8,
			1, 9, 8, 3, 4, 2, 5, 6, 7,
			8, 5, 9, 7, 6, 1, 4, 2, 3,
			4, 2, 6, 8, 5, 3, 7, 9, 1,
			7, 1, 3, 9, 2, 4, 8, 5, 6,
			9, 6, 1, 5, 3, 7, 2, 8, 4,
			2, 8, 7, 4, 1, 9, 6, 3, 5,
			3, 4, 5, 2, 8, 6, 1, 7, 9);

		List<List<Integer>> solutions = s.solve();

		assertEquals(solutions.size(), 1);

		assertThat(solutions.get(0), is(expected));
	}

	@Test
	public void testChaosSudoku() {
		List<Integer> sudokuBoard = Arrays.asList(
			3, 0, 0, 0, 0, 0, 0, 0, 4,
			0, 0, 2, 0, 6, 0, 1, 0, 0,
			0, 1, 0, 9, 0, 8, 0, 2, 0,
			0, 0, 5, 0, 0, 0, 6, 0, 0,
			0, 2, 0, 0, 0, 0, 0, 1, 0,
			0, 0, 9, 0, 0, 0, 8, 0, 0,
			0, 8, 0, 3, 0, 4, 0, 6, 0,
			0, 0, 4, 0, 1, 0, 9, 0, 0,
			5, 0, 0, 0, 0, 0, 0, 0, 7);

		List<Integer> regions = Arrays.asList(
			1, 1, 1, 2, 3, 3, 3, 3, 3,
			1, 1, 1, 2, 2, 2, 3, 3, 3,
			1, 4, 4, 4, 4, 2, 2, 2, 3,
			1, 1, 4, 5, 5, 5, 5, 2, 2,
			4, 4, 4, 4, 5, 6, 6, 6, 6,
			7, 7, 5, 5, 5, 5, 6, 8, 8,
			9, 7, 7, 7, 6, 6, 6, 6, 8,
			9, 9, 9, 7, 7, 7, 8, 8, 8,
			9, 9, 9, 9, 9, 7, 8, 8, 8);

		SudokuSolver s = new SudokuSolver(sudokuBoard, regions);

		List<Integer> expected = Arrays.asList(
			3, 5, 8, 1, 9, 6, 2, 7, 4,
			4, 9, 2, 5, 6, 7, 1, 3, 8,
			6, 1, 3, 9, 7, 8, 4, 2, 5,
			1, 7, 5, 8, 4, 2, 6, 9, 3,
			8, 2, 6, 4, 5, 3, 7, 1, 9,
			2, 4, 9, 7, 3, 1, 8, 5, 6,
			9, 8, 7, 3, 2, 4, 5, 6, 1,
			7, 3, 4, 6, 1, 5, 9, 8, 2,
			5, 6, 1, 2, 8, 9, 3, 4, 7);

		List<List<Integer>> solutions = s.solve();

		assertEquals(1, solutions.size());

		assertThat(solutions.get(0), is(expected));
	}
}
