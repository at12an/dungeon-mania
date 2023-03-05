package dungeonmania.Goals;

import com.google.gson.JsonObject;
import dungeonmania.Config;
import com.google.gson.JsonArray;

/**
 * @author Max Ovington
 */
public class GoalFactory {

    //TODO implement this
    public static Goal getEntity(Config config, JsonObject obj) {
        
        //boolean case
        if (obj.get("goal").getAsString().equals("OR") || obj.get("goal").getAsString().equals("AND")) {
            JsonArray subgoals = obj.get("subgoals").getAsJsonArray();
            Goal leftChild = getEntity(config, subgoals.get(0).getAsJsonObject());
            Goal rightChild = getEntity(config, subgoals.get(1).getAsJsonObject());
            return new BooleanGoal(obj.get("goal").getAsString(), leftChild, rightChild);
        } else if (obj.get("goal").getAsString().equals("exit")) {
            return new ExitGoal();
        } else if (obj.get("goal").getAsString().equals("treasure")) {
            return new TreasureGoal(config.getTreasureGoal());
        } else if (obj.get("goal").getAsString().equals("boulders")) {
            return new SwitchGoal();
        } else if (obj.get("goal").getAsString().equals("enemies")) {
            return new EnemyGoal(config.getEnemyGoal());
        }
        return null;
        //otherwise
    }

}
