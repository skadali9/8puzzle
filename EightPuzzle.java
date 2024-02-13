import java.util.*;

public class EightPuzzle {

    // The stringRep method is used to convert a matrix into a string.
    // This is used to print the puzzle state and also in a String set to check
    // repeated state.
    private static String stringRep(int state[][]) {
        String res = "";
        int rowLength = state.length;
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < rowLength; j++) {
                res += String.valueOf(state[i][j]);
                if (j != rowLength - 1) {
                    res += ",";
                }
            }
            if (i != rowLength - 1) {
                res += "\n";
            }
        }
        return res;
    }

    // Triple comparator class to compare two triples i.e., two puzzle states to
    // consider the cheapest node
    // The one with the least depth is to be considered if the heuristic value is
    // same
    private static class TripleComparator implements Comparator<Triple> {
        public int compare(Triple t1, Triple t2) {
            int score1 = t1.heuristic + t1.depth;
            int score2 = t2.heuristic + t2.depth;
            if (score1 == score2) {
                return Integer.compare(t1.depth, t2.depth);
            }
            return Integer.compare(score1, score2);
        }
    }

    // A set is used to hold all the explored states
    static Set<String> states = new HashSet<String>();
    static Scanner sc = new Scanner(System.in);
    // Hardcoding the goal state
    static int goal[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
    // Hardcoding a puzzle state
    // Uncomment to test for a particular depth
    // static int arr[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } }; //Depth 0
    // static int arr[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 0, 7, 8 } }; // Depth 2
    // static int arr[][] = { { 1, 2, 3 }, { 5, 0, 6 }, { 4, 7, 8 } }; // Depth 4
    static int arr[][] = { { 1, 3, 6 }, { 5, 0, 2 }, { 4, 7, 8 } }; // Depth 8
    // static int arr[][] = { { 1, 3, 6 }, { 5, 0, 7 }, { 4, 8, 2 } }; // Depth 12
    // static int arr[][] = { { 1, 6, 7 }, { 5, 0, 3 }, { 4, 8, 2 } }; // Depth 16
    // static int arr[][] = { { 7, 1, 2 }, { 4, 8, 5 }, { 6, 3, 0 } }; // Depth 20
    // static int arr[][] = { { 0, 7, 2 }, { 4, 6, 1 }, { 3, 5, 8 } }; // Depth 24
    // static int arr[][] = { { 6, 4, 7 }, { 8, 5, 0 }, { 3, 2, 1 } }; // Depth 31

    // Using a priority queue of type Triple which holds puzzle state, heursitc
    // value and depth
    static PriorityQueue<Triple> pq = new PriorityQueue<Triple>(new TripleComparator());
    // startTime, endTime to calculate the time taken and store in timeDiff
    static long startTime = -1, endTime = -1;
    static double timeDiff = 0;
    static int NODE_COUNT = 0;
    static int flag;
    static int MAX_QUEUE_SIZE = 0;

    // This method checks if a puzzle is valid
    static boolean checkValidity(int[][] puzzle) {
        int freq[] = new int[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (puzzle[i][j] >= 0 && puzzle[i][j] < 9) {
                    if (freq[puzzle[i][j]] > 0) {
                        return false;
                    }
                    freq[puzzle[i][j]]++;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    // This method takes user input for a user entered puzzle
    public static void readInput() {
        boolean validPuzzle = false;
        int n = 3; // n=3 for 8-puzzle
        int[][] puzzleInput = new int[n][n];
        // This loop terminates when user enters the valid puzzle input
        while (true) {
            System.out.println("Enter your puzzle using a zero to represent the blank");
            System.out.println("Enter the puzle delimiting the number with a space");

            for (int i = 0; i < n; i++) {
                System.out.printf("Enter row %d: ", i + 1);
                String line[] = sc.nextLine().split(" ");
                if (line.length != n) {
                    continue;
                }
                for (int j = 0; j < n; j++) {
                    puzzleInput[i][j] = Integer.parseInt(line[j]);
                }
            }

            validPuzzle = checkValidity(puzzleInput);
            if (validPuzzle) {
                break;
            } else {
                System.out.printf("Invalid input\n\n");
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = puzzleInput[i][j];
            }
        }
    }

    public static void main(String[] args) {

        System.out.printf("Welcome to 8 puzzle solver. ");
        System.out.printf("Type '1' to use a default puzzle, ");
        System.out.printf(" or '2' to create your own\n");
        System.out.printf("Enter your option: ");
        int option = sc.nextInt();
        sc.nextLine();

        if (option < 1 || option > 2) {
            System.out.println("Invalid option");
            System.exit(-1);
        }

        if (option == 2) {
            readInput();
        }

        System.out.println(
                "Select method:\n\n1.Uniform Cost Search\n\n2.A* with Misplaced Tile heuristic\n\n3.A* with Manhattan Distance heuristic\n");
        flag = sc.nextInt();
        if(flag < 1 || flag > 3) {
            System.out.println("Invalid heuristic option selected");
            System.exit(-1);
        }
        puzzle(arr);
        sc.close();
    }

    // The puzzle method first checks if the puzzle state is the goal state.
    // If it is not, the current state is added to the priority queue, the set(to
    // check repeated states).
    // Then, search method is called and the outputs are being printed.
    public static int puzzle(int[][] arr) {
        int finalDepth = 0;
        startTime = System.currentTimeMillis();

        // // System.out.println(a);
        if (isGoalState(arr)) {
            endTime = System.currentTimeMillis();
            timeDiff = (endTime - startTime) / 1000.0;
            System.out.println("Depth = 0");
            System.out.printf("Time taken: %.2f seconds\n", timeDiff);
            return 0;
        }
        // The heuristic is calculated based on the which method is selected by the
        // user.
        Triple triple = new Triple(arr, calculateHeuristic(arr), finalDepth);
        triple.matrix = arr;

        states.add(stringRep(triple.matrix));

        pq.add(triple);
        finalDepth = search();

        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime) / 1000.0;
        System.out.printf("Time taken: %.2f seconds\n", timeDiff);
        System.out.printf("Final depth = %d\n", finalDepth);
        System.out.println("Nodes expanded=" + NODE_COUNT);
        System.out.println("Max queue size: " + MAX_QUEUE_SIZE);
        return finalDepth;
    }

    /*
     * The search method first fetched the least state from the priority queue.
     * If it is the goal state, it's depth is returned.
     * Else, valid possible states are created from the current state and are added
     * to the priority queue.
     * The function terminates until the priority queue is empty or goal state has
     * been found
     */
    public static int search() {
        while (!pq.isEmpty()) {
            MAX_QUEUE_SIZE = Math.max(MAX_QUEUE_SIZE, pq.size());
            // The state in the front is polled
            Triple currentState = pq.poll();
            String curStateStr = stringRep(currentState.matrix);
            System.out.printf("Cur state:\n%s\n", curStateStr);
            // System.out.printf("Current Depth: %d\n", currentState.depth);
            // If the Set does not contain this state, add it
            if (!states.contains(curStateStr)) {
                states.add(curStateStr);
            }
            // If the current state is the goal state, return
            if (isGoalState(currentState.matrix)) {

                return currentState.depth;
            }
            // Number of expanded nodes
            NODE_COUNT++;
            // To generate next states and select the cheapest node
            List<Triple> nextStates = generateNextStates(currentState);
            // Adding all the generated states to the priority queue.
            pq.addAll(nextStates);
        }
        return -1;
    }

    // This method checks if the current puzzle state is goal state
    public static boolean isGoalState(int[][] arr) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] != goal[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // To calculate the heuristic. 0 is returned for Uniform cost,
    // mismatchedTiles(arr) is called for Misplaced Tiles heuristic and
    // calculateManhattanDistance(arr) is called for A* with Manhattan distance
    // heuristic
    public static int calculateHeuristic(int[][] arr) {
        if (flag == 1)
            return 0;
        else if (flag == 2)
            return mismatchedTiles(arr);
        else if (flag == 3)
            return calculateManhattanDistance(arr);
        return 0;
    }

    // This method calculates the heuristic value for misplaced tiles
    public static int mismatchedTiles(int[][] arr) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] != goal[i][j])
                    count++;
            }
        }
        return count;
    }

    // This method calculates the Manhattan Distance heuristic
    public static int calculateManhattanDistance(int[][] arr) {
        int distance = 0;
        int n = arr.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = arr[i][j];
                // Ignoring the blank tile
                if (tile == 0) {
                    continue;
                }
                // To find the correct position of the tile in goal state
                int row = (tile - 1) / n;
                int col = (tile - 1) % n;
                // Calculate the Manhattan distance for the tile
                int tileDistance = Math.abs(row - i) + Math.abs(col - j);
                // Adding the tile's Manhattan distance to the total distance
                distance += tileDistance;
            }
        }
        return distance;
    }

    // This method generates the next possible states from a current puzzle state
    public static List<Triple> generateNextStates(Triple currentState) {
        int[][] arr = currentState.matrix;
        List<Triple> nextStates = new ArrayList<Triple>();
        // The blank tile position is identified and stored in x and y
        int x = 0, y = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] == 0) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        // The possible ways to move the blank. Up, down, left, right
        // the x and y values are stored in 2 arrays
        int a[] = { x, x, x - 1, x + 1 };
        int b[] = { y - 1, y + 1, y, y };

        // Inititalising the fin array. This array holds the heuristic value for the
        // generated states.
        int fin[] = new int[4];
        for (int i = 0; i < 4; i++) {
            fin[i] = Integer.MAX_VALUE;
        }

        for (int k = 0; k < 4; k++) {
            // The current array state copied to a new mod array and next state is
            // generated.
            int[][] mod = new int[3][3];
            for (int g = 0; g < 3; g++) {
                for (int h = 0; h < 3; h++) {
                    mod[g][h] = arr[g][h];
                }
            }
            int temp = mod[x][y];
            // Conditions to check if the blank is within the index
            if (a[k] < 0 || a[k] > 2)
                continue;
            if (b[k] < 0 || b[k] > 2)
                continue;
            // blank swapped with a numbered tile
            mod[x][y] = mod[a[k]][b[k]];
            mod[a[k]][b[k]] = temp;
            // Checking it the result state is repeated
            if (states.contains(stringRep(mod))) {
                continue;
            }
            // Heuristic value calculated and stored in fin array
            int currentHeuristic = calculateHeuristic(mod);
            fin[k] = currentHeuristic;
            // Adding the generated states to a list
            Triple triple = new Triple(mod, currentHeuristic, currentState.depth + 1);
            nextStates.add(triple);
            states.add(stringRep(mod));
            // // System.out.println(calculateHeuristic(mod, goal));
        }
        // To get the index of the min heuristic from the fin array
        int min = Integer.MAX_VALUE;
        int in = -1;
        for (int i = 0; i < fin.length; i++) {
            if (fin[i] < min) {
                min = fin[i];
                in = i;
            }
        }
        // Swapping the arr with the min heuristic state and the next states returned to
        // be added to the priority queue.
        if (in != -1) {
            int temp = arr[x][y];
            arr[x][y] = arr[a[in]][b[in]];
            arr[a[in]][b[in]] = temp;
            return nextStates;
        }
        return nextStates;
    }
}
