package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.Entities.*;
import dungeonmania.Entities.Collectables.SunStone;
import dungeonmania.Movement;
/**
 * @author Andrew Tran
 */
public class OpenDoor extends Reaction { 

    // TODO: Check priority

    private static final int priority = 7;

    private Entity opener;
    private Door door;

    public OpenDoor(Entity opener, Door door) {
        this.opener = opener;
        this.door = door;
    }

    public int getPriority() {
        return priority;
    }
    
    // TODO: check interactions with other entities

    public void execute(Dungeon d, Collection<Reaction> q) {

        Player p = (Player) opener;
        //System.out.println("has key" + p.hasKey(door.getKeyId()));
        //System.out.println(door.isOpen());
        // Check if player has key and door is locked
        boolean door_openable = p.hasKey(door.getKeyId()) && !door.isOpen();
        if (p.checkItemCount(SunStone.class) >= 1) {
            door.setOpen(true);
            q.add(new Move((Movement) opener, door.getPosition()));
        } else if (door_openable) {
            //System.out.println(3);
            // Open door
            door.setOpen(true);

            // Remove key
            p.useKey();

            // Move player
            q.add(new Move((Movement) opener, door.getPosition()));
            
            // p.move(door.getPosition());

        // If door open move player
        } else if (door.isOpen()) {
            q.add(new Move((Movement) opener, door.getPosition()));

        // If door closed and no key
        } else {
            // Block entry
            q.add(new BlockEntry());
        }
    }

    public String getType() {
        return "OpenDoor";
    }

}
