import java.util.LinkedList;
import java.util.Queue;

public class BFS {
	public Queue<PuzzleLogic> frontiers = new LinkedList<>(); // create a queue of puzzles
	public String waysToGo[] = { "up", "down", "left", "right" };
	public int nodes = 0;

	// main method that will either return a complete solved puzzle or null
	public PuzzleLogic findFinalSolution(PuzzleLogic p) {
		frontiers.add(p); // start from what we're given

		// we will search through all the possible moves that will be added 
		while (!frontiers.isEmpty()) {
			PuzzleLogic currPuzzle = frontiers.poll(); // take out current head
			nodes++;
			if (currPuzzle.checkForSolution()) {
				return currPuzzle;
			}

			// iterate through the four options and save all the potentials in the
			// "frontier"
			for (int i = 0; i < 4; i++) {
				if (currPuzzle.moveAvailable(waysToGo[i])) {
					PuzzleLogic anothaPuzzle = new PuzzleLogic(currPuzzle);
					anothaPuzzle.makeTheMove(waysToGo[i]);
					frontiers.add(anothaPuzzle);
				}
			}
		}
		System.out.println("A valid solution was not found!");
		return null;

	}

}
