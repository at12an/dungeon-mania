package dungeonmania.Entities;

import dungeonmania.reactions.ExitReached;
import dungeonmania.reactions.Reaction;
import dungeonmania.util.Position;

public class Exit extends Entity {
    public Exit(String type, Position position) {
        super(type, position);
    }

    public Reaction interact(Entity e){
        if (e instanceof Player) {
            Player p = (Player) e;
            return new ExitReached(super.getPosition(), p);
        }
        return null;
    }
    
}
