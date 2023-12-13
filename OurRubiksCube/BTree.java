package OurRubiksCube;
import java.util.concurrent.atomic.AtomicBoolean;
import solutioning.strategy.Action;
import rubikcube.RubikCube;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

// Back tree
public class BTree {
    private AtomicBoolean Found = new AtomicBoolean(false);
    public Map<RubiksMod, Integer> arrayCollection;
    public FTree fTree;
    public int BFS;
    public int LHash;
    public RubiksMod solvedState;
    private long BeginTime;
    public Map<RubikCube, Integer> bTree;
    public int Level = 0;

    public BTree(int BFS, int LHash) {
        this.BeginTime = System.nanoTime();
        this.BFS = BFS;
        this.LHash = LHash;
        arrayCollection = new HashMap<>();
        bTree = new HashMap<>();
        fTree = new FTree(this);

        // generate front trees then generate the initial state of the back trees
        // just put print statements here because I did not want you to think the code was not running
        // did not know where else to put them
        System.out.println("Please wait");
        fTree.generateInitial();
        // 36 initial combinations "States"
        System.out.println("Just a little Longer!");
        generateInitialB(36);
    }

    // Method to perform DFS traversal in the back tree
    public boolean DFS(RubiksMod rootNode, int limit) {
        Stack<RubiksMod> stack = new Stack<>();
        stack.push(rootNode);
        while (!stack.isEmpty()) {
            RubiksMod currentNode = stack.pop();

            if (limit < 0) {
                return false;
            }

            // checking conditions
            // checking the nodes of front tree
            if (fTree.arrayCollection.containsKey(currentNode)) {
                List<RubiksMod> p0 = fTree.getPath(currentNode);
                if (!p0.isEmpty()) {
                    RubiksMod curr = currentNode;
                    for (; curr != null; curr = curr.getParent()) {
                        p0.add(curr);
                    }
                    for (RubiksMod step : p0) {
                        step.getInstance().print();
                    }
                    Found.set(true);
                    return true;
                }
            }
            // expand current nodes
            List<RubiksMod> kids = currentNode.getKids();
            for (RubiksMod kid : kids) {
                stack.push(kid);
            }

            limit--;
        }
        // no match at all
        return false;
    }


    // to generate 36 initial states
    // can change if desired My partner and I decided on 36
    // due to the total amount of nodes in a complete 3x3 Rubiks Cube is 56
    public void generateInitialB(int x) {
        this.solvedState = new RubiksMod(new RubikCube(3), 0, null);
        arrayCollection.put(this.solvedState, 0);

        // cloning the actions
        for (Action<RubikCube> action : solvedState.getInstance().getAllActions()) {
            try {
                RubikCube State = solvedState.getInstance().clone();
                State.performAction(action);
                RubiksMod newNode = new RubiksMod(State, solvedState.getLevel()+1, solvedState);
                solvedState.addKid(newNode);
                arrayCollection.put(newNode, 1);
            } catch (Exception q) {
                break;
            }
        }
        Level +=1;
        for (int i = 0; i < LHash-1; i++) {
            generateStates();
        }

    }
    // function used for timing
    public void Timing(BTree x){
        //timing
        long EndTime = System.nanoTime();
        long TotalT = EndTime - BeginTime;
        System.out.println("Total Time elapsed: " + TotalT + "nanoseconds.");
    }

    // Generates more states in the tree
    public void generateStates() {
        Map<RubiksMod, Integer> bTreeNodes = new HashMap<>(arrayCollection);

        for (Map.Entry<RubiksMod, Integer> bEntry : bTreeNodes.entrySet()) {
            RubiksMod node = bEntry.getKey();
            Action<RubikCube>[] actions = node.getInstance().getAllActions();

            if (Level == node.getLevel()) {
                for (Action<RubikCube> action : actions) {
                    try {
                        RubikCube State = node.getInstance().clone();
                        State.performAction(action);

                        // Rebecca helped me with this portion to calculate the misplaced nodes
                        int misplacedCount = 0;
                        List<List<Integer>> Statecurr = State.generateArray();
                        RubikCube solved = new RubikCube(3);
                        List<List<Integer>> solvedState = solved.generateArray();

                        for (int i = 0; i < Statecurr.size(); i++) {
                            for (int j = 0; j < Statecurr.get(i).size(); j++) {
                                if (!Statecurr.get(i).get(j).equals(solvedState.get(i).get(j))) {
                                    misplacedCount++;
                                }
                            }
                        }
                        RubiksMod newNode = new RubiksMod(State, node.getLevel() + 1, node);
                        if (!arrayCollection.containsKey(newNode)) {
                            node.addKid(newNode);
                            arrayCollection.put(newNode, misplacedCount);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Level += 1;
    }
}