package dungeonmania.Goals;
import java.io.Serializable;

import dungeonmania.Dungeon;

/**
 * @author Max Ovington
 */
public interface Goal extends Serializable {

    public boolean checkGoal(Dungeon d);

    public String toString(Dungeon d);
}
