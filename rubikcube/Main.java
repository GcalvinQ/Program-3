/* wrote this code to understand the code better this was
described in the paper to est the hypothesised solution and test it
 */

package rubikcube;

import solutioning.strategy.Action;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        RubikCube cube = new RubikCube(3);
        RubikSolution solution = new RubikSolution();
        List<Action<RubikCube>> randomActions = solution.randomActions(cube, 20);
        randomActions.forEach(action -> System.out.println(action.getName()));
        cube.print();
        System.out.println(cube.check());
        List<Action<RubikCube>> reverseActions = solution.reverseActions(randomActions);
        reverseActions.forEach(action -> System.out.println(action.getName()));
        reverseActions.forEach(cube::performAction);
        cube.print();
        System.out.println(cube.check());
    }
    /*public static List<Action<RubikCube>> randomActions(RubikCube rubikCube, int count){
        Random random = new Random();
        int actionCount = rubikCube.getAllActions().length;
        return IntStream.range(0, count).boxed().map(i -> {
            Action<RubikCube> action = rubikCube.getAllActions()[random.nextInt(actionCount)];
            rubikCube.performAction(action);
            return action;
        }).toList();
    }
    public List<Action<RubikCube>> reverseActions(List<Action<RubikCube>> originalActions){
        return IntStream.rangeClosed(1, originalActions.size())
                .boxed()
                .map(i -> originalActions.get(originalActions.size() - i))
                .map(Action<RubikCube>::oppositeAction)
                .toList();
    }
*/
}

