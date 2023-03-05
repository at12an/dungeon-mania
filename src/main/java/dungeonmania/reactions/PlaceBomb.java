package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;
import dungeonmania.Entities.Collectables.*;

public class PlaceBomb extends Reaction {
    private Bomb bomb;
    private Position position;

    private static final int priority = 3;

    public PlaceBomb(Bomb bomb, Position position) {
        this.bomb = bomb;
        this.position = position;
    }

    public int getPriority(){
        return priority; 
    } 

    public void execute(Dungeon d, Collection<Reaction> q){
        bomb.setPlaced(true);
        bomb.setPosition(position);
        d.addEntity(bomb);

        if (bomb.shouldBeActivated(d.getEntities())) {
            q.add(new Explode(bomb.getPosition(), bomb.getRadius()));
            d.removeEntity(bomb);
        }

        d.getPlayer().deleteByType(bomb);
    }

    public String getType(){
        return "PlaceBomb";
    }
}
