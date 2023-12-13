package OurRubiksCube;

// specifying limits of BFS and hash and initializing a backtree to be solved
// timing of the solving of said cube is also tracked and called
// Also made edits to the RubiksCube class to house arrays
public class Main {
    public static void main(String[] args) {
        int BFS = 5;
        int Hash = 5;
        BTree cube = new BTree(BFS, Hash);
        cube.DFS(cube.solvedState, Hash+1);
        cube.Timing(cube);
    }
}
