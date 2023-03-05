package dungeonmania.Entities;

import dungeonmania.Enemy;
import dungeonmania.Movement;
import dungeonmania.reactions.BlockEntry;
import dungeonmania.reactions.ChangeDirection;
import dungeonmania.reactions.Move;
import dungeonmania.reactions.Reaction;
import dungeonmania.util.Position;

public class Wall extends Entity {

    public Wall(String type, Position position) {
        super(type, position);
    }

    public Reaction interact(Entity e){
        if (e instanceof Spider) {
            return new Move((Movement) e, this.getPosition());
        } else if (e instanceof Mercenary) {
            return new ChangeDirection((Enemy) e);
        }
        
        return new BlockEntry();
    }
}
