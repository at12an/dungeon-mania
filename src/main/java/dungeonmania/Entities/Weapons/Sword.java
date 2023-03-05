package dungeonmania.Entities.Weapons;

import java.util.List;

import dungeonmania.Entities.Collectable;
import dungeonmania.Entities.Entity;
import dungeonmania.util.Position;

/**
 * @author Max Ovington
 */
public class Sword extends Collectable implements Weapon {

    int durability;
    int damage;

    public Sword(String type, Position position, int durability, int damage) {
        super(type, position);
        this.durability = durability;
        this.damage = damage;
    }

    public void useWeapon(List<Entity> bin) {
        durability--;
        if (durability == 0) bin.add(this);
    };

    public int getDurability() {
        return durability;
    }

    public int getDamage() {
        return damage;
    };
}



