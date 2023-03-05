package dungeonmania.Goals;
import dungeonmania.Dungeon;

/**
 * @author Max Ovington
 */
public class BooleanGoal implements Goal {
    
    private String type;
    private Goal leftChild;
    private Goal rightChild;

    public BooleanGoal(String type, Goal leftChild, Goal rightChild) {
        this.type = type;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public boolean checkGoal(Dungeon d) {
        if (type.equals("AND")) {
            return leftChild.checkGoal(d) && rightChild.checkGoal(d);
        } else if (type.equals("OR")) {
            return leftChild.checkGoal(d) || rightChild.checkGoal(d);
        }
        return false;
    }

    public String toString(Dungeon d){
        return "(" + leftChild.toString(d) + " " + type + " " + rightChild.toString(d) + ")";
    }
}
