package dungeonmania.Goals;
import dungeonmania.Dungeon;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.Collectables.Treasure;

/**
 * @author Max Ovington
 */
public class TreasureGoal implements Goal {

    private int treasure;

    public TreasureGoal(int treasure) {
        this.treasure = treasure;
    }

    //TODO: implement this! --> exit conditions
    public boolean checkGoal(Dungeon d) {
        Player p = d.getPlayer();
        if (p == null) {
            return false;
        }
        return p.checkItemCount(Treasure.class) >= treasure;
    }
    
    //TODO: add more advanced statements for treasure
    public String toString(Dungeon d) {
        if (!checkGoal(d)) {
            return ":treasure(" + treasure + ")";
        }
        return "";
    }
}
