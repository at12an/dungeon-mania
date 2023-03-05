package dungeonmania.reactions;

import java.util.Collection;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Movement;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.OldPlayer;
import dungeonmania.Entities.Portal;
import dungeonmania.Entities.TimeTravellingPortal;
import dungeonmania.util.Position;

public class Teleport extends Reaction{

    private static final int priority = 7;
    
    private Entity player;
    private Portal portal;
    
    public Teleport(Entity player, Portal portal) {
        this.player = player;
        this.portal = portal;
    }

    public void execute(Dungeon d, Collection<Reaction> q){
        
        //if time travelling portal move to linked portal
        if (portal instanceof TimeTravellingPortal) {
            if (player instanceof OldPlayer) {
                d.removeEntity(player);
                return;
            }
            if (d.getTimecontroller().getCurrentTime() - 30 >= 0) {
                q.add(new Move((Movement) player, portal.getLinkedPortal().getPosition()));
            }
            d.SetTimePortal(true);
            return;
        }

        // if portal surrounded
        List<Position> adjPos = portal.getLinkedPortal().getPosition().getAdjacentPositions();
        for (int i = 1; i < adjPos.size(); i += 2){
            // find adjacent block without something player cannot teleport onto
            if (!d.step(player, adjPos.get(i)).stream().anyMatch(o -> o.getClass().equals(BlockEntry.class))){
                q.add(new Move((Movement) player, adjPos.get(i)));
                return;
            }
        }
        q.add(new BlockEntry());        
    }

    public String getType(){
        return "Teleport";
    }
    public int getPriority(){
        return priority;
    }
}
