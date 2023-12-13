package OurRubiksCube;
import solutioning.strategy.Action;
import rubikcube.RubikCube;
import java.util.*;

// functions the 'opposite' of the back Tree// with exceptions of course
// help from stack and based off of the back tree
// we got ample help from Troy and Rebecca as well as stack overflow and GitHub
public class FTree {
    public Map<RubikCube, Integer> fTree;
    public Map<RubiksMod, Integer> arrayCollection;
    public BTree bTree;
    public int BFS;
    public int currentLevel;
    public FTree(BTree bTree) {
        BFS = bTree.BFS;
        fTree = new HashMap<>();
        arrayCollection = new HashMap<>();
        this.bTree = bTree;
        // a rubiks cube size 3x3
        RubikCube rubiks0 = new RubikCube(3);

        // this is the randomize function from the RubiksCube class
        // does not seem to always have a completely 'random' cube with the depth that we have stated
        // would have to increase the depth
        rubiks0.randomize();

        // generate the initial rubiks cube
        RubiksMod rubiks = new RubiksMod(rubiks0, 0, null);
        arrayCollection.put(rubiks, 1);
    }

    // FTree creation
    public void generateInitial() {
        for (int i = 0; i < BFS; i++) {
            generateFStates();
        }
    }

    // this finds the path that will generate a solution for the cube
    public List<RubiksMod> getPath(RubiksMod backTreeNode) {
        List<RubiksMod> path = new ArrayList<>();

        for (Map.Entry<RubiksMod, Integer> frontEntry : arrayCollection.entrySet()) {
            RubiksMod frontNode = frontEntry.getKey();

            // tries to see if the instances of the nodes are equal toe ach other
            if (frontNode.equals(backTreeNode)) {
                frontNode = frontNode.getParent();
                for (; frontNode != null; frontNode = frontNode.getParent()) {
                    path.add(frontNode);
                }
                // for the use of the back tree
                // and DLS
                Collections.reverse(path);
                return path;
            }
        }
        currentLevel += 1;
        return path;
    }


    // Generates more front faces
    public void generateFStates() {
        Map<RubiksMod, Integer> frontNodes = new HashMap<>(arrayCollection);

        // continue to next levels
        for (Map.Entry<RubiksMod, Integer> Entry : frontNodes.entrySet()) {
            RubiksMod node = Entry.getKey();
            Action<RubikCube>[] actions = node.getInstance().getAllActions();
            if (currentLevel == node.getLevel()) {
                for (Action<RubikCube> action : actions) {
                    try {
                        // cloning state and actions
                        RubikCube newState = node.getInstance().clone();
                        newState.performAction(action);

                        // using int for each calculation
                        // Got help from troy with the helper function to keep track
                        int misplacedCount = calculateMisplaced(newState);
                        RubiksMod newNode = new RubiksMod(newState, node.getLevel() + 1, node);
                        if (!arrayCollection.containsKey(newNode)) {
                            node.addKid(newNode);
                            arrayCollection.put(newNode, misplacedCount);
                        }
                    } catch (Exception x) {
                        x.getCause();
                    }
                }
            }
        }
        currentLevel += 1;
    }

    // function to help calculate misplaced nodes
    private int calculateMisplaced(RubikCube cube) {
        int misplacedCount = 0;
        List<List<Integer>> currentState = cube.generateArray();
        RubikCube solved = new RubikCube(3);
        List<List<Integer>> solvedState = solved.generateArray();

        for (int i = 0; i < currentState.size(); i++) {
            for (int j = 0; j < currentState.get(i).size(); j++) {
                if (!currentState.get(i).get(j).equals(solvedState.get(i).get(j))) {
                    misplacedCount++;
                }
            }
        }
        return misplacedCount;
    }
}

