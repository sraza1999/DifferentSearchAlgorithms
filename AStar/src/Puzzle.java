import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Puzzle {
	public static void main(String[] args) throws IOException {
		int countForPerf = 1;
		System.out.println("Please input numbers from 0-15");

		int perfectPuzzle[][] = new int[4][4];
		// We want to read in the input, and then save each number as a separate element
		// in an array of strings
		BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
		String line = b.readLine();
		String breakPoint[] = line.split(" ");

		// initialize our puzzle that needs to be solved and put the numbers that we got
		// from the input
		int puzzle[][] = new int[4][4];
		int count = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				puzzle[i][j] = Integer.parseInt(breakPoint[count]);
				count++;
			}
		}

		/*
		 * *********** The input has successfully been saved in the 2D array named
		 * puzzle. ***********
		 */

		// initialize the perfectPuzzle to the correct numbers
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == 3 && j == 3) {
					perfectPuzzle[i][j] = 0;
				} else {
					perfectPuzzle[i][j] = countForPerf;
					countForPerf++;
				}
			}
		}
		// initialize our puzzle, and the two heuristics to solve it
		PuzzleLogic puzzzle = new PuzzleLogic(puzzle, perfectPuzzle);
		AStarr misplaced = new AStarr();
		AStarr manhattan = new AStarr();

		// ******IMPORTANT reference:
		// https://stackoverflow.com/questions/37916136/how-to-calculate-memory-usage-of-a-java-program
		// used to calculate how much memory is used.could not find any other way
		long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long time = System.nanoTime();

		// This is the block for the misplaced tiles
		System.out.println("This is the solution to the problem using the MISPLACED TILES ");
		PuzzleLogic solution = misplaced.AStarMisplaced(puzzzle, "Misplaced");
		time = System.nanoTime();
		long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long actualMemUsed = afterUsedMem - beforeUsedMem;
		System.out.println("completed puzzle!");
		System.out.println("Moves: " + solution.pathOfPuzzle);
		System.out.println("Number of Nodes expanded: " + misplaced.numOfMoves);
		System.out.println("Time Taken: " + time / 1000000000);
		System.out.println("Memory used: " + actualMemUsed);

		System.out.println();
		System.out.println();
		System.out.println();

		// Manhattan Distance
		System.out.println("Solution using Manhattan Distance: ");
		beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		time = System.nanoTime();

		PuzzleLogic Manhattan = manhattan.AStarMisplaced(puzzzle, "Manhattan");

		time = System.nanoTime();
		afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		actualMemUsed = afterUsedMem - beforeUsedMem;

		System.out.println("Moves: " + Manhattan.pathOfPuzzle);
		System.out.println("Number of Nodes expanded: " + manhattan.numOfMoves);
		System.out.println("Time Taken: " + time / 1000000000);
		System.out.println("Memory used: " + actualMemUsed);

	}// end of main

	static void printPuzzle(int[][] puzzle) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(puzzle[i][j] + " ");
			}
		}
	}
}
