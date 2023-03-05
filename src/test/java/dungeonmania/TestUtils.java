package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.FileLoader;

public class TestUtils {
    public static Stream<EntityResponse> getEntitiesStream(DungeonResponse res, String type) {
        if (type.equals("zombie_toast")){
            return res.getEntities().stream()
                    .filter(it -> it.getType().startsWith(type))
                    .filter(it -> !it.getType().startsWith("zombie_toast_spawner"));
        }
        return res.getEntities().stream().filter(it -> it.getType().startsWith(type));
    }

    public static int countEntityOfType(DungeonResponse res, String type) {
        return getEntities(res, type).size();       
    }
    
    public static Optional<EntityResponse> getPlayer(DungeonResponse res) {
        return getEntitiesStream(res, "player").findFirst();
    }

    public static List<EntityResponse> getEntities(DungeonResponse res, String type) {
        return getEntitiesStream(res, type).collect(Collectors.toList());
    }

    public static List<ItemResponse> getInventory(DungeonResponse res, String type) {
        return res.getInventory().stream()
                                 .filter(it -> it.getType().startsWith(type))
                                 .collect(Collectors.toList());
    }

    public static String getGoals(DungeonResponse dr) {
        String goals = dr.getGoals();
        return goals != null ? goals : "";
    }

    public static String getValueFromConfigFile(String fieldName, String configFilePath) {
        try {
            JSONObject config = new JSONObject(FileLoader.loadResourceFile("/configs/" + configFilePath + ".json"));
            
            if (!config.isNull(fieldName)) {
                return config.get(fieldName).toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        return null;
    }

    public static void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(-(enemyAttack / 10), round.getDeltaCharacterHealth(), 0.001);
            assertEquals(-(playerAttack / 5), round.getDeltaEnemyHealth(), 0.001);
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }
}
