package dungeonmania.Entities;

import dungeonmania.reactions.*;
import dungeonmania.util.Position;

public class TimeTravellingPortal extends Portal {

    public TimeTravellingPortal(String type, Position position, String colour) {
        super(type, position, colour);
    }

    @Override
    public Reaction interact(Entity e){
        if (e instanceof Player) {
            return new Teleport(e, this);
        }
        return null;
    }


}
