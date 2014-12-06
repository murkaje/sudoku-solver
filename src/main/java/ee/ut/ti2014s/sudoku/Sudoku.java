package ee.ut.ti2014s.sudoku;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;

public class Sudoku {

	private static List<Integer> readBoard(Path file) throws IOException {
		try(Stream<String> s = Files.lines(file)) {
			return s.flatMap(l -> Arrays.stream(l.split("\\s"))).map(c -> c.equals("-") ? 0 : Integer.parseInt(c)).collect(toList());
		}
	}

	private static void display(List<Integer> b) {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				System.out.print(b.get(i * 9 + j));
				if(j != 8) {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		if(args.length < 1 || args.length > 2) {
			System.err.println("Kasutus: Sudoku [input-file] (region-file)");
			System.exit(1);
		}

		List<Integer> board = null;
		List<Integer> regions = null;

		try {
			Path sudokuFile = Paths.get(args[0]);
			board = readBoard(sudokuFile);
			if(args.length == 2) {
				Path regionFile = Paths.get(args[1]);
				regions = readBoard(regionFile);
			}
		} catch(IOException e) {
			System.err.println("Error reading input file: " + e);
			System.exit(1);
		}

		SudokuSolver s;
		if(regions == null) {
			s = new SudokuSolver(board);
		} else {
			s = new SudokuSolver(board, regions);
		}
		List<List<Integer>> solutions = s.solve();

		if(solutions.isEmpty()) {
			System.out.println("Võimalikud lahendid puuduvad");
		} else {
			if(solutions.size() == 1) {
				System.out.println("Leiti 1 lahend:");
			} else {
				System.out.println("Leiti mitu lahendit, kuvan ühte neist:");
			}
			display(solutions.get(0));
		}
	}
}
