package dungeonmania.Goals;
import dungeonmania.Dungeon;
import dungeonmania.Entities.ZombieToastSpawner;
import java.util.stream.Collectors;

/**
 * @author Max Ovington
 */
public class EnemyGoal implements Goal {

    public EnemyGoal(int count) {
        this.count = count;
    }

    int count;

    public boolean checkGoal(Dungeon d) {
        return d.getEnemiesKilled() >= count && d.getEntities().stream().filter(e -> e instanceof ZombieToastSpawner).collect(Collectors.toList()).size() <= 0;
    }

    public String toString(Dungeon d) {
        if (!checkGoal(d)) {
            return ":enemies(" + d.getEnemiesKilled() + "/" + count + ")";
        }
        return "";
    }
}
