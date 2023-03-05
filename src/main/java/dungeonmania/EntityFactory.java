package dungeonmania;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dungeonmania.Entities.Assassin;
import dungeonmania.Entities.Boulder;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Exit;
import dungeonmania.Entities.Hydra;
import dungeonmania.Entities.Mercenary;
import dungeonmania.Entities.OldPlayer;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.Spider;
import dungeonmania.Entities.Portal;
import dungeonmania.Entities.Switch;
import dungeonmania.Entities.TimeTravellingPortal;
import dungeonmania.Entities.Wall;
import dungeonmania.Entities.ZombieToast;
import dungeonmania.Entities.ZombieToastSpawner;
import dungeonmania.Entities.Door;
import dungeonmania.Entities.Collectables.Arrow;
import dungeonmania.Entities.Collectables.Bomb;
import dungeonmania.Entities.Collectables.InvincibilityPotion;
import dungeonmania.Entities.Collectables.InvisibilityPotion;
import dungeonmania.Entities.Collectables.Key;
import dungeonmania.Entities.Collectables.TimeTurner;
import dungeonmania.Entities.Collectables.Treasure;
import dungeonmania.Entities.Collectables.Wood;
import dungeonmania.Entities.Collectables.SunStone;
import dungeonmania.Entities.Logical.*;
import dungeonmania.Entities.Weapons.Sword;
import dungeonmania.util.Position;

public class EntityFactory {
    public static Entity getEntity(Config config, JsonObject jo, boolean timetravel) {

        Entity entity = null;
        String type = jo.get("type").getAsString();
        Position position = new Position(
            jo.get("x").getAsInt(), 
            jo.get("y").getAsInt()
        );

        if (type.startsWith("player") && !timetravel) {
            entity = new Player(type, position, config.getPlayerHealth(), config.getPlayerAttack());
        } else if (type.startsWith("player") && timetravel) {
            entity = new OldPlayer(type, position, config.getPlayerHealth(), config.getPlayerAttack());
        } else if (type.startsWith("wall")) {
            entity = new Wall(type, position);
        } else if (type.startsWith("exit")) {
            entity = new Exit(type, position);
        } else if (type.startsWith("boulder")) {
            entity = new Boulder(type, position);
        } else if (type.startsWith("switch_door")) {
            entity = new SwitchDoor(type, position, jo.get("key").getAsInt());
            addLogicStrategy(entity, jo.get("logic"));
        } else if (type.startsWith("switch")){
            entity = new Switch(type, position);
            addLogicStrategy(entity, jo.get("logic"));
        } else if (type.startsWith("spider")) {
            entity = new Spider(type, position, config.getSpiderHealth(), config.getSpiderAttack());
        } else if (type.startsWith("portal")){
            entity = new Portal(type, position, jo.get("colour").getAsString());
        } else if (type.startsWith("time_travelling_portal")) {
            entity = new TimeTravellingPortal("time_travelling_portal", position, jo.get("colour").getAsString());
        } else if (type.startsWith("wood")) {
            entity = new Wood(type, position);
        } else if (type.startsWith("arrow")) {
            entity = new Arrow(type, position);
        } else if (type.startsWith("treasure")) {
            entity = new Treasure(type, position);
        } else if (type.startsWith("bomb")) {
            entity = new Bomb(type, position, config.getBombRadius());
            addLogicStrategy(entity, jo.get("logic"));
        } else if (type.startsWith("key")) {
            entity = new Key(type, position, jo.get("key").getAsInt());
        } else if (type.startsWith("sword")) {
            entity = new Sword(type, position, config.getSwordDurability(), config.getSwordAttack());
        } else if (type.startsWith("invincibility_potion")) {
            entity = new InvincibilityPotion(type, position, config.getInvincibilityPotionDuration());
        } else if (type.startsWith("invisibility_potion")) {
            entity = new InvisibilityPotion(type, position, config.getInvisibilityPotionDuration());
        } else if (type.startsWith("zombie_toast_spawner")) {
            entity = new ZombieToastSpawner(type, position);
        } else if (type.startsWith("zombie_toast")) {
            entity = new ZombieToast(type, position, config.getZombieHealth(), config.getZombieAttack());
        } else if (type.startsWith("door")) {
            entity = new Door(type, position, jo.get("key").getAsInt());
        } else if (type.startsWith("time_turner")) {
            entity = new TimeTurner(type, position);
        } else if (type.startsWith("mercenary")) {
            entity = new Mercenary(type, position, config.getMercenaryHealth(), config.getMercenaryAttack(),
                                                config.getBribeAmount(), config.getBribeRadius(), 
                                                config.getAlly_attack(), config.getAlly_defence());
        } else if (type.startsWith("sun_stone")) {
            entity = new SunStone(type, position);
        } else if (type.startsWith("assassin")) {
            entity = new Assassin(type, position, config.getAssassinHealth(), config.getAssassinAttack(), 
                                                config.getAssassinBribeAmount(), config.getAssassinBribeFailRate(),
                                                config.getAssassinReconRadius(), config.getBribeRadius(), config.getAlly_attack(),
                                                config.getAlly_defence());
        } else if (type.startsWith("hydra")) {
            entity = new Hydra(type, position, config.getHydraAttack(), config.getHydraHealth(), 
                                                config.getHydraHealthIncreaseAmount(), config.getHydraHealthIncreaseRate());
        } else if (type.startsWith("light_bulb_off")) {
            entity = new LightBulb(type, position);
            addLogicStrategy(entity, jo.get("logic"));
        } else if (type.startsWith("wire")) {
            entity = new Wire(type, position);
        } 
        return entity;
    }

    public static void spawnEntities(Config config, List<Entity> currentEntities, int tick) {
        if (config.getSpiderSpawnRate() != 0 && tick % config.getSpiderSpawnRate() == 0) {
            Position position = Spider.getSpawnPosition(currentEntities);
            currentEntities.add(new Spider("spider", position, config.getSpiderHealth(), config.getSpiderAttack()));
        }

        if (config.getZombieSpawnRate() != 0 && tick % config.getZombieSpawnRate() == 0) {
            List<Entity> zombieSpawners = currentEntities.stream()
                                                         .filter(entity -> (entity instanceof ZombieToastSpawner))
                                                         .collect(Collectors.toList());  
            for (Entity zombieSpawner: zombieSpawners) {
                Position position = ((ZombieToastSpawner) zombieSpawner).getSpawnPosition(currentEntities);
                if (position != null) {
                    ZombieToast z = new ZombieToast("zombie_toast", position, config.getZombieHealth(), config.getZombieAttack());
                    z.attachPlayer((Player) currentEntities.stream().filter(e -> e instanceof Player).findFirst().get());
                    currentEntities.add(z);
                }
            }      
        }
    }

    private static void addLogicStrategy(Entity entity, JsonElement logic) {
        if (logic == null) {
            return;
        }

        if (logic.getAsString().equals("or")) {
            ((Logical) entity).addLogicStrategy(new OrStrategy());
        } else if (logic.getAsString().equals("and")) {
            ((Logical) entity).addLogicStrategy(new AndStrategy());
        } else if (logic.getAsString().equals("xor")) {
            ((Logical) entity).addLogicStrategy(new XorStrategy());
        } else if (logic.getAsString().equals("co_and")) {
            ((Logical) entity).addLogicStrategy(new CoAndStrategy());
        }
    }
}
