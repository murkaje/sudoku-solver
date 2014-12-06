package ee.ut.ti2014s.algox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class AlgoX {

	private final Map<Integer, Set<Integer>> X;
	private final Map<Integer, List<Integer>> Y;
	private final List<int[]> solutions = new ArrayList<>();

	//Some object creation cache to ease load on GC
	private final Map<Integer, List<Set<Integer>>> coverColsStack = new HashMap<>(81);
	private final Map<Integer, Set<Integer>> branchRowsStack = new HashMap<>(81);
	private final int limit;

	private int curDepth = 0;

	//For debugging/benchmarking
	private final long begin;
	private long next;

	private List<Set<Integer>> getCurrentCols() {
		List<Set<Integer>> coverCols = coverColsStack.get(curDepth);

		if(coverCols == null) {
			coverCols = new ArrayList<>();
			coverColsStack.put(curDepth, coverCols);
		} else {
			coverCols.clear();
		}

		return coverCols;
	}

	private Set<Integer> copyCurrentBranchRows(Set<Integer> old) {
		Set<Integer> branchRows = branchRowsStack.get(curDepth);

		if(branchRows == null) {
			branchRows = new HashSet<>(old);
			branchRowsStack.put(curDepth, branchRows);
		} else {
			branchRows.clear();
			branchRows.addAll(old);
		}

		return branchRows;
	}

	//Remove rows and columns that intersect Y[rowNum]
	private void cover(int rowNum) {
		List<Set<Integer>> coverCols = getCurrentCols();

		for(int j : Y.get(rowNum)) {
			for(int i : X.get(j)) {
				for(int k : Y.get(i)) {
					if(k != j) {
						X.get(k).remove(i);
					}
				}
			}
			coverCols.add(X.remove(j));
		}
	}

	private void uncover(int rowNum) {
		List<Set<Integer>> coverCols = coverColsStack.get(curDepth);
		List<Integer> row = Y.get(rowNum);

		for(int ri = row.size() - 1; ri >= 0; ri--) {
			int j = row.get(ri);
			X.put(j, coverCols.get(ri));	//This is slow, maybe use custom map with hash=key
			for(int i : X.get(j)) {
				for(int k : Y.get(i)) {
					if(k != j) {
						X.get(k).add(i);
					}
				}
			}
		}
	}

	private AlgoX(List<Integer> X, Map<Integer, List<Integer>> Y, int limit) {
		this.X = new HashMap<>(X.size());
		this.Y = Y;
		this.limit = limit;

		X.stream().forEach(i -> this.X.put(i, new HashSet<>()));

		//Transform X to a map for fast column -> row access
		X.stream().forEach(col -> Y.entrySet().stream()
			.filter(row -> row.getValue().contains(col))
			.forEach(row -> this.X.get(col).add(row.getKey()))
		);

		begin = System.currentTimeMillis();
		next = begin + 1000;
	}

	public static List<int[]> solve(List<Integer> X, Map<Integer, List<Integer>> Y, int limit) {
		AlgoX problem = new AlgoX(X, Y, limit);
		problem.solve(new int[X.size()]);
		return problem.solutions;
	}

	public static List<int[]> solve(List<Integer> X, Map<Integer, List<Integer>> Y) {
		return solve(X, Y, 2);
	}

	private Set<Integer> getMinCol() {
		Set<Integer> minCol = null;
		for(Set<Integer> col : X.values()) {
			if(minCol == null || minCol.size() > col.size()) {
				if(col.isEmpty()) {
					return null;
				}
				minCol = col;
			}
		}

		return copyCurrentBranchRows(minCol);
	}

	/**
	 * Vital to use int array instead of ArrayList,
	 * because elements are added/removed frequently and would trash GC
	 */
	private void solve(int[] partial) {
		if(solutions.size() == limit) {
			return;
		}
		long now = System.currentTimeMillis();
		if(now > next) {
			System.out.println((now - begin)
				+ "ms\tunique: " + solutions.size()
				+ "\trate: "
				+ new BigDecimal(solutions.size())
				.scaleByPowerOfTen(3)
				.divide(new BigDecimal(now - begin), 0, RoundingMode.HALF_UP)
				+ "/sec");
			next = now + 1000;
		}
		if(X.isEmpty()) {
			int[] solution = new int[curDepth];
			System.arraycopy(partial, 0, solution, 0, curDepth);
			solutions.add(solution);
		} else {
			//Get column with least number of rows
			Set<Integer> branches = getMinCol();

			//One column is empty, not possible to cover
			if(branches == null) {
				return;
			}

			//Branch solving
			for(int r : branches) {
				partial[curDepth] = r;
				cover(r);
				curDepth++;
				solve(partial);
				curDepth--;
				uncover(r);
			}
		}
	}
}
