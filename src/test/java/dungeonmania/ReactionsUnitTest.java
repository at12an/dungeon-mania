package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dungeonmania.Entities.*;
import dungeonmania.Entities.Collectables.Arrow;
import dungeonmania.Entities.Collectables.Bomb;
import dungeonmania.Entities.Collectables.InvincibilityPotion;
import dungeonmania.Entities.Collectables.InvisibilityPotion;
import dungeonmania.Entities.Collectables.Sceptre;
import dungeonmania.Entities.Collectables.Treasure;
import dungeonmania.Entities.Collectables.Wood;
import dungeonmania.Entities.Weapons.Bow;
import dungeonmania.Entities.Weapons.MidnightArmour;
import dungeonmania.Entities.Weapons.Shield;
import dungeonmania.Entities.Weapons.Sword;
import dungeonmania.reactions.Move;

public class ReactionsUnitTest {
    @Test
    @DisplayName("Reaction unit tests for null case")
    public void ReactionTests() {

        //player
        Player p = new Player("player", null, 10, 10);
        assertEquals(p.interact(new Wall("wall", null)), null);

        //zombie toast
        ZombieToast z = new ZombieToast("zombie_toast", null, 10, 10);
        assertEquals(z.interact(new Wall("spider", null)), null);

        //spider
        Spider s = new Spider("spider", null, 10, 10);
        assertEquals(s.interact(new Wall("zombie", null)), null);

        //portal
        Portal pl = new Portal("portal", null, "red");
        assertEquals(pl.interact(new Wall("treasure", null)), null);

        //zombie toast spawner
        ZombieToastSpawner zt = new ZombieToastSpawner("zombie_toast_spawner", null);
        assertEquals(zt.interact(new Wall("treasure", null)), null);

        //Switch
        Switch sw = new Switch("switch", null);
        assertEquals(sw.interact(new Wall("treasure", null)), null);

        //exit
        Exit e = new Exit("exit", null);
        assertEquals(e.interact(new Wall("treasure", null)), null);

        //Collectable
        Treasure t = new Treasure("treasure", null);
        assertEquals(t.interact(new Wall("treasure", null)), null);

        //wood
        Wood wo = new Wood("wood", null);
        assertEquals(wo.interact(new Wall("treasure", null)), null);

        //arrow
        Arrow ar = new Arrow("arrow", null);
        assertEquals(ar.interact(new Wall("treasure", null)), null);

        //bomb
        Bomb b = new Bomb("bomb", null, 2);
        assertEquals(b.interact(new Wall("treasure", null)), null);

        //sword
        Sword swo = new Sword("sword", null, 10, 10);
        assertEquals(swo.interact(new Wall("treasure", null)), null);

        //invisibility
        InvisibilityPotion iv = new InvisibilityPotion("invisibility_potion", null, 5);
        assertEquals(iv.interact(new Wall("treasure", null)), null);

        //invincibility
        InvincibilityPotion in = new InvincibilityPotion("invisibility_potion", null, 5);
        assertEquals(in.interact(new Wall("treasure", null)), null);

        //bow
        Bow bw = new Bow("bow", null, 10, 10);
        assertEquals(bw.interact(new Wall("treasure", null)), null);
        assertEquals(bw.isBuildable(p), false);

        //Shield
        Shield sh = new Shield("shield", null, 10, 10);
        assertEquals(sh.interact(new Wall("treasure", null)), null);
        assertEquals(sh.isBuildable(p), false);

        //door
        Door d = new Door("door", null, 2);
        assertTrue(d.interact(new Spider("spider", null, 10, 19)) instanceof Move);

        //sceptre
        Sceptre sc = new Sceptre("sceptre", null, 10);
        assertEquals(sc.interact(new Wall("treasure", null)), null);
        assertEquals(sc.isBuildable(p), false);

        //midnight armour
        MidnightArmour ma = new MidnightArmour("midnight_armour", null, 10, 10);
        assertEquals(ma.interact(new Wall("treasure", null)), null);
        assertEquals(ma.isBuildable(p), false);
    }
}
