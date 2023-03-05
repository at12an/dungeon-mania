package dungeonmania.Entities.Collectables;

import dungeonmania.Entities.*;

public class ItemsToUseFactory {
    public static boolean canBeUsed(Entity c) {
        if (c instanceof InvisibilityPotion) {
            return true;
        } else if (c instanceof InvincibilityPotion) {
            return true;
        } else if (c instanceof Bomb) {
            return true;
        }
        return false;
    }
}
