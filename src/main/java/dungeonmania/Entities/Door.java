package dungeonmania.Entities;

import dungeonmania.reactions.*;
import dungeonmania.util.*;
import dungeonmania.Movement;

/**
 * @author Andrew Tran
 */
public class Door extends Entity {
    private boolean open = false;
    private int keyId;

    public Door(String type, Position position, int keyId) {
        super(type, position);
        this.keyId = keyId;
    }

    public Reaction interact(Entity e) {
        // entity still has it's old position, not the new one
        // Check if door is closed 
        if (e instanceof Player) {
            return new OpenDoor(e, this);
        } else if (e instanceof Boulder) {
            return new BlockEntry();
        } else if (open || e instanceof Spider) {
            return new Move((Movement)e, this.getPosition());
        }
        return new BlockEntry();
    }

    public int getKeyId() {
        return keyId;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
    
    
}
