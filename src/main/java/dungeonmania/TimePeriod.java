package dungeonmania;

import java.io.Serializable;
import java.util.List;
import java.util.PriorityQueue;

import dungeonmania.reactions.*;
import dungeonmania.Entities.*;
import dungeonmania.Entities.Collectables.*;
import dungeonmania.util.*;
import dungeonmania.Entities.Weapons.*;

public class TimePeriod implements Serializable {

    private List<String> actions;

    public TimePeriod(List<String> actions) {
        this.actions = actions;
    }

    public List<String> getActions() {
        return actions;
    }

    public void addAction(String action) {
        actions.add(action);
    }

    public PriorityQueue<Reaction> execute(Dungeon d, Config c)  {

        PriorityQueue<Reaction> reactions = new PriorityQueue<>();
        Entity e = d.getEntities().stream().filter(x -> x instanceof OldPlayer == true).findFirst().get();

        for (String a: actions) {
            String[] commands = a.split("_");
            if (a.contains("move")) {
                //movement
                Movement toBeMoved = (Movement) e;
                Reaction r = new Move(toBeMoved, e.getPosition().translateBy(Direction.valueOf(commands[1])));
                if (r != null) reactions.add(r);
            } else if (a.contains("use")) {
                //check player has item
                Reaction r = Use(d, commands[1], e);
                if (r != null) reactions.add(r);
            } else if (a.contains("interact")) {
                //interaction type reactions
                Reaction r = Interact(d, (Player) e, commands[1]);
                if (r != null) reactions.add(r);
            } else if (a.contains("build")) {
                //get buildable string
                Reaction r = build((Player) e, commands[1], c);
                if (r != null) reactions.add(r);
            } else if (a.contains("rewind")) {
                //remove player now
                d.removeEntity(e);
            }
        }

        return reactions;
    }

    public Reaction build(Player p, String command, Config c) {
        Buildable b = (Buildable) BuildableFactory.getEntity(c, command);
            if (command.contains("bow")) {
                return new Build(p, b);
            } else if (command.contains("shield")) {
                return new Build(p, b);
            }
        return null;
    }

    public Reaction Interact(Dungeon d, Player p, String command) {
        //if zombie toast spawner and spawner exists
        for (Entity x : d.getEntities()) {
            if (x.getId().equals(command)) {
                try {
                    if (x instanceof ZombieToastSpawner) {
                        ZombieToastSpawner zt = (ZombieToastSpawner) x;
                        return new KillToasterOldPlayer(p, zt);
                    }
                    else if (x instanceof Mercenary) {
                        Mercenary m = (Mercenary) x;
                        return new BribeMercOldPlayer(p, m);
                    }
                } catch (Exception e) {
                    ;
                }
            }
        }
        return null;
    }

    public Reaction Use(Dungeon d, String command, Entity e) {
        Player p = (Player) e;
        for (Entity x : p.getInventory()) {
            if (x.getId().equals(command)) {
                if (p.checkItemCount(x.getClass()) > 0 && e instanceof Bomb) {
                    return new PlaceBomb((Bomb) x, p.getPosition());
                } else if (p.checkItemCount(x.getClass()) > 0 && (x instanceof InvisibilityPotion || x instanceof InvincibilityPotion)) {
                    return new UsePotion((Potion) x, p);
                }
            }
        }
        return null;
    }

}
