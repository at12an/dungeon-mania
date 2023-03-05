package dungeonmania.reactions;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Switch;
import dungeonmania.Entities.Collectables.Bomb;
import dungeonmania.Entities.Logical.Logical;
import dungeonmania.Entities.Logical.Wire;
import dungeonmania.util.Position;

public class ToggleLogical extends Reaction {
    private static final int priority = 10;
    private Logical logical;
    private String type; 

    public ToggleLogical(Logical logical, String type) {
        this.logical = logical;
        this.type = type;
    }

    @Override
    public void execute(Dungeon d, Collection<Reaction> reactions) {
        if (type.equals("activate")) {
            logical.activate();

            // If it is a bomb, trigger explosion
            if (logical instanceof Bomb) {
                Bomb bomb = (Bomb) logical;
                reactions.add(new Explode(bomb.getPosition(), bomb.getRadius()));
                d.removeEntity(bomb);
            }
        } else {
            logical.deactivate();
        }
        
        // If it is a switch or wire, propogate to adjacent logical entities
        if (logical instanceof Switch || logical instanceof Wire) {
            List<Entity> adjacentLogical = d.getEntities().stream()
                                                      .filter(e -> e instanceof Logical && Position.isAdjacent(((Entity) logical).getPosition(), e.getPosition()))
                                                      .collect(Collectors.toList());
            for (Entity e : adjacentLogical) {
                Logical logical = (Logical) e;
                if (logical.shouldBeActivated(d.getEntities())) {
                    reactions.add(new ToggleLogical(logical, "activate"));
                } else if (logical.shouldBeDeactivated(d.getEntities())) {
                    reactions.add(new ToggleLogical(logical, "deactivate"));
                }
            }
        }
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getType() {
        return "ToggleLogical";
    }
}
