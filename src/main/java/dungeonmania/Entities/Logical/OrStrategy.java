package dungeonmania.Entities.Logical;

import java.util.List;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Switch;
import dungeonmania.util.Position;

public class OrStrategy implements LogicalStrategy {   
    @Override
    public boolean shouldBeActivated(List<Entity> allEntities, Entity entity) {
        // if there is one adjacent logical entity that is active then this entity should be activated
        boolean shouldBeActivated = allEntities.stream()
                                               .anyMatch(e -> (e instanceof Switch || e instanceof Wire) && 
                                                         Position.isAdjacent(entity.getPosition(), e.getPosition()) &&
                                                         ((Logical) e).isActivated());
        
        return shouldBeActivated;
    }

    @Override
    public boolean shouldBeDeactivated(List<Entity> allEntities, Entity entity) {
        return !shouldBeActivated(allEntities, entity);
    }
}
