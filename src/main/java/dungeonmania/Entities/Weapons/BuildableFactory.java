package dungeonmania.Entities.Weapons;
import dungeonmania.Entities.*;
import dungeonmania.Entities.Collectables.Sceptre;
import dungeonmania.Config;

public class BuildableFactory {
    public static Entity getEntity(Config config, String type) {
        if (type.equals("bow")) {
            return new Bow(type, null, 2, config.getBowDurability());
        } else if (type.equals("shield")) {
            return new Shield(type, null, config.getShieldDurability(), config.getShieldDefence());
        } else if (type.equals("sceptre")) {
            return new Sceptre(type, null, config.getMindControlDuration());
        } else if (type.equals("midnight_armour")) {
            return new MidnightArmour(type, null, config.getMidnightArmourDefence(), config.getMidnightArmourAttack());
        }
        return null;
    }
}
