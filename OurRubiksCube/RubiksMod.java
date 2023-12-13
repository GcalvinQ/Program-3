package OurRubiksCube;
import rubikcube.RubikCube;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Arrays;
import java.util.List;


// Got help with some of the modifications to this class from Troy and Rebecca
// as well as Chat.gpt of course
public class RubiksMod {
    private RubikCube rubiksMod;
    private int level;
    private RubiksMod parent;
    private List<RubiksMod> kids;
    public RubiksMod(RubikCube rubiksCube, int level, RubiksMod parent) {
        this.rubiksMod = rubiksCube;
        this.level = level;
        this.parent = parent;
        this.kids = new ArrayList<>();
    }
    public RubikCube getInstance() {
        return rubiksMod;
    }

    public RubiksMod getParent() {
        return parent;
    }

    public int getLevel() {
        return level;
    }

    public void addKid(RubiksMod kid){
        kids.add(kid);
    }

    public List<RubiksMod> getKids(){
        return kids;
    }

    // got help from Troy on this section
    @Override
    public boolean equals(Object x) {
        if (this == x) return true;
        if (!(x instanceof RubiksMod)) return false;
        RubiksMod other = (RubiksMod) x;
        List<List<Integer>> thisState = this.getInstance().generateArray();
        List<List<Integer>> otherState = other.getInstance().generateArray();

        return Arrays.deepEquals(thisState.toArray(), otherState.toArray());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getInstance().generateArray());
    }

}
