import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStarr {
	public Queue<PuzzleLogic> frontiers = new LinkedList<>(); // create a queue of puzzles
	public boolean goalF = false;
	int numOfMoves = 0;
	int depth = 0;
	int maxDepth = 0;
	public String waysToGo[] = { "up", "down", "left", "right" };
	PuzzleLogic anothaPuzzle;
	int countOfMisplaced = 0;
	int k = 0;
	int gDistancee = 0;

	ArrayList<PuzzleLogic> exploredList;
	PriorityQueue<PuzzleLogic> frontier;

	PuzzleLogic AStarMisplaced(PuzzleLogic currentP, String heuristic) {
		String heur;
		if (heuristic == "Misplaced") {
			heur = "Misplaced";
		}
		else {
			heur = "Manhattan";
		}
		// needed to be based off the f(n) which is g(n) + h(n)
		frontier = new PriorityQueue<>(Comparator.comparingInt(PuzzleLogic::getfDistance));
		exploredList = new ArrayList<>();

		// add our base case to both the frontier and our explored lists
		frontier.add(currentP);
		exploredList.add(currentP);

		while (true) {
			PuzzleLogic current;

			if (frontier.isEmpty()) {
				System.out.println("No solution could be found!");
				break;
			}

			current = frontier.poll(); // get the first puzzle off the pQueue

			if (current.checkForSolution()) {
				return current;
			}
			numOfMoves++;
			// lets start exploring the children
			for (int i = 0; i < 4; i++) {
				if (current.moveAvailable(waysToGo[i])) {
					PuzzleLogic anothaPuzzle = new PuzzleLogic(current);
					anothaPuzzle.makeTheMove(waysToGo[i]);

					// update what the heuristic is based on (misplaced tiles
					anothaPuzzle.hDistance = heuristicCheck(anothaPuzzle.puzzleSolving, anothaPuzzle.finalPuzzle, heur);
					// System.out.println("the hDistance here is: " + anothaPuzzle.hDistance);
					gDistancee++;
					anothaPuzzle.gDistance = current.gDistance + 1;

					// if this is not a child that has already been explored...
					if (!childInExplored(anothaPuzzle)) {
						frontier.add(anothaPuzzle);
						exploredList.add(anothaPuzzle);

					}
				}
			}
			try {
				Thread.sleep(00);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}

	int heuristicCheck(int currentP[][], int finalP[][],String h) {
		int f = 0;
		int element = 0;
		if(h == "Misplaced") {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (currentP[i][j] != finalP[i][j]) {
						f++;
					}
				}
			}
		}
		if(h == "Manhattan") {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					element = currentP[i][j]; // for EVERY single element...
					
					for (int a = 0; a < 4; a++) {
						for (int b = 0; b < 4; b++) {
							if (element == finalP[a][b]) {					
								f += Math.abs(a-i) + Math.abs(b-j); // find how far we are from the target value
							}
						}
					}
				}
			}
		}
		
		return f;
	}

	boolean childInExplored(PuzzleLogic child) {

		// we need to explore every single puzzle that is already in the explored list
		// to make sure we are not checking the same one again
		for (PuzzleLogic puz : exploredList) {
			boolean check = true;

			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (puz.puzzleSolving[i][j] != child.puzzleSolving[i][j]) {
						check = false;
					}
				}
			}
			// if the check is true after going through one whole puzzle then we know for
			// sure that the child has already been explored
			if (check == true) {
				return true;
			}
		}
		return false;
	}

	int calulateMisplaced() {
		return 0;
	}

}
