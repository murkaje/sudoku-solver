package ee.ut.ti2014s.sudoku;

import ee.ut.ti2014s.algox.AlgoX;
import java.util.*;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;

public class SudokuSolver {

	private static final int[] BASE_REGIONS = IntStream.range(0, 81).map(i -> (i / 27) * 3 + (i % 9) / 3).toArray();

	private final int[] board;
	private final int[] regions;

	public SudokuSolver(List<Integer> board, List<Integer> regions) {
		checkInvariants(board, regions);

		this.board = board.stream().mapToInt(i -> i - 1).toArray();
		this.regions = regions.stream().mapToInt(i -> i - 1).toArray();
	}

	public SudokuSolver(List<Integer> board) {
		checkInvariants(board, null);

		this.board = board.stream().mapToInt(i -> i - 1).toArray();
		this.regions = BASE_REGIONS;
	}

	private void checkInvariants(List<Integer> board, List<Integer> regions) {
		if(board.size() != 81) {
			throw new IllegalArgumentException("Not a 9x9 sudoku board");
		}
		for(int i : board) {
			if(i < 0 || i > 9) {
				throw new IllegalArgumentException("Invalid board value: " + i);
			}
		}
		if(regions != null) {
			for(int i : regions) {
				if(i < 1 || i > 9) {
					throw new IllegalArgumentException("Invalid regions value: " + i);
				}
			}
		}
	}

	private Map<Integer, List<Integer>> genRows() {
		Map<Integer, List<Integer>> rows = new HashMap<>();
		int rowIndex = 0;

		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				int cellValue = board[i * 9 + j];

				//If fixed value, generate only 1 row, else all 9
				int[] posValues = cellValue == -1 ? IntStream.range(0, 9).toArray() : IntStream.of(cellValue).toArray();

				for(int value : posValues) {
					List<Integer> row = new ArrayList<>();
					row.add(i * 9 + j);	//Row-Column constraint 0...80
					row.add(9 * 9 + i * 9 + value);	//Row-Cell constraint 81...161
					row.add(9 * 9 * 2 + j * 9 + value);	//Column-Cell constraint 162..242
					row.add(9 * 9 * 3 + regions[i * 9 + j] * 9 + value);	//Box-Cell constraint 243...323
					rows.put(rowIndex++, row);
				}
			}
		}

		return rows;
	}

	public List<List<Integer>> solve() {
		List<List<Integer>> sudokuSolutions = new ArrayList<>();

		//Generate placement rows
		Map<Integer, List<Integer>> rows = genRows();
		List<Integer> columns = IntStream.range(0, 9 * 9 * 4).boxed().collect(toList());

		//Solve using Knuth's Algorithm X
		List<int[]> solutions = AlgoX.solve(columns, rows, -1);

		//Read boards from resulting subsets
		solutions.stream().map(solutionRows -> {
			List<Integer> solution = new ArrayList<>(Collections.nCopies(81, 0));
			Arrays.stream(solutionRows)
				.mapToObj(i -> rows.get(i))
				.forEach(row -> solution.set(row.get(0), (row.get(1) - 81) % 9 + 1));
			return solution;
		}).forEach(solution
			-> sudokuSolutions.add(solution)
		);

		return sudokuSolutions;
	}
}
