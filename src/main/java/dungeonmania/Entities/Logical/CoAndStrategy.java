package dungeonmania.Entities.Logical;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Switch;
import dungeonmania.util.Position;

public class CoAndStrategy implements LogicalStrategy {

    @Override
    public boolean shouldBeActivated(List<Entity> allEntities, Entity entity) {
        List<Entity> adjacentEntities = allEntities.stream()
                                                    .filter(e -> (e instanceof Switch || e instanceof Wire) &&
                                                            Position.isAdjacent(entity.getPosition(), e.getPosition()) &&
                                                            ((Logical) e).wasActivatedThisTick())
                                                    .collect(Collectors.toList());
        
        return adjacentEntities.size() >= 2;
    }

    @Override
    public boolean shouldBeDeactivated(List<Entity> allEntities, Entity entity) {
        // Co-and deactivation is undefined, just return false
        return false;
    }
    
}
