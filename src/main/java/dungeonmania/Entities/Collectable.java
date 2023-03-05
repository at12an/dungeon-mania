package dungeonmania.Entities;

import dungeonmania.reactions.*;
import dungeonmania.util.*;

/**
 * @author Max Ovington
 */
public abstract class Collectable extends Entity {

    public Collectable(String type, Position position) {
        super(type, position);
    }
    
    @Override
    public Reaction interact(Entity e) {
        // entity still has it's old position, not the new one
        if (e instanceof Player) {
            //collect item
            Player p = (Player) e;
            return new Collect(this, p, super.getPosition());
        } 
        return null;
    }

}
