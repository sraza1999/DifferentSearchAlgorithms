
// This class contains ALL the logic for the game, 
	public class PuzzleLogic {
		public String waysToGo[] = { "up", "down", "left", "right" };
		// the puzzle we're constantly solving and the final puzzle that is our final
		// output
		public int[][] puzzleSolving;
		public int[][] finalPuzzle;
		int count = 1;
		int c = 0;
		public int depth = 0;
		public int gDistance = 0; // will retain g(f)
		public int hDistance = 0; // will retain h(f)
		public int fDistance = 0;

		public String pathOfPuzzle = " "; // keep track of the different moves we do

		// keeps track of the location of where the zero is and allows us to know valid
		// moves
		public int xZero;
		public int yZero;

		public int getfDistance() {
			return gDistance + hDistance;
		}

		// overloaded constructor that will 1) initialize the puzzles and 2) find which
		// tile the zero is in.
		public PuzzleLogic(int[][] p, int[][] perfectP) {
			puzzleSolving = p;
			finalPuzzle = perfectP;

			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (puzzleSolving[i][j] == 0) {
						xZero = j;
						yZero = i;
						// System.out.println("xZ: " + xZero + " yZ: " + yZero);
						break;
					}
				}
			}
		}

		public PuzzleLogic(PuzzleLogic nPuzzle) {
			puzzleSolving = new int[4][4];
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					puzzleSolving[i][j] = nPuzzle.puzzleSolving[i][j];
				}
			}
			finalPuzzle = nPuzzle.finalPuzzle;
			xZero = nPuzzle.xZero;
			yZero = nPuzzle.yZero;
			pathOfPuzzle = nPuzzle.pathOfPuzzle;
			gDistance = nPuzzle.gDistance+1;
			
		}

		boolean moveAvailable(String s) {
			if (s == "up") {
				if (yZero != 0) {
					// System.out.print("can move up ");
					return true;
				}
			}
			if (s == "down") {
				if (yZero != 3) {
					// System.out.print("can move down ");

					return true;
				}
			}
			if (s == "left") {
				if (xZero != 0) {
					// System.out.print("can move left ");

					return true;
				}
			}
			if (s == "right") {
				if (xZero != 3) {
					// System.out.print("can move right ");

					return true;
				}
			}
			return false;
		}

		void makeTheMove(String s) {
			if (s == "up") {
				// System.out.println("moving up");
				swap(yZero, xZero, (yZero - 1), xZero);
				pathOfPuzzle = pathOfPuzzle + "U";
			}
			if (s == "down") {
				// System.out.println("moving down");

				swap(yZero, xZero, (yZero + 1), xZero);
				pathOfPuzzle = pathOfPuzzle + "D";

			}
			if (s == "left") {
				// System.out.println("moving left");

				swap(yZero, xZero, yZero, (xZero - 1));
				pathOfPuzzle = pathOfPuzzle + "L";

			}
			if (s == "right") {
				// System.out.println("moving right");

				swap(yZero, xZero, yZero, (xZero + 1));
				pathOfPuzzle = pathOfPuzzle + "R";

			}
		}

		private void swap(int y1, int x1, int y2, int x2) {
			puzzleSolving[y1][x1] = puzzleSolving[y2][x2];
			puzzleSolving[y2][x2] = 0;

			yZero = y2;
			xZero = x2;
		}

		public boolean checkForSolution() {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (puzzleSolving[i][j] != finalPuzzle[i][j]) {
						return false;
					}
				}
			}
			return true;
		}

		public void printPuzzzle(PuzzleLogic p) {

			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					System.out.print(p.puzzleSolving[i][j] + " ");
				}
			}

		}
	}