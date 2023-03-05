package dungeonmania.Entities;

import dungeonmania.Enemy;
import dungeonmania.Movement;
import dungeonmania.reactions.*;
import dungeonmania.util.*;;

public class Boulder extends Entity implements Movement{
    
    public Boulder(String type, Position position) {
        super(type, position);
    }

    public Reaction interact(Entity e) {
        // entity still has it's old position, not the new one
        if (e instanceof Player) {
            return new Push(e, this);
        } else if (e instanceof Spider || e instanceof Mercenary) {
            return new ChangeDirection((Enemy) e);
        }
        
        // else
        return new BlockEntry();
    }

    /**
     * moves entity, performs no logic, assumes input will not place 
     * entity in illegal position.
     * @param p
     */
    public void move(Position p){
        this.setPosition(p);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Position pl = new Position(this.getPosition().getX(), this.getPosition().getY());        
        return new Boulder(this.getType(), pl);
    }
}
