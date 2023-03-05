package dungeonmania.Entities.Logical;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Switch;
import dungeonmania.util.Position;

public class XorStrategy implements LogicalStrategy {

    @Override
    public boolean shouldBeActivated(List<Entity> allEntities, Entity entity) {
        // the entity will be activated if there is 1 and only 1 cardinally adjacent activated entity

        List<Entity> adjacentEntities = allEntities.stream()
                                                    .filter(e -> (e instanceof Switch || e instanceof Wire) &&
                                                            Position.isAdjacent(entity.getPosition(), e.getPosition()) &&
                                                            ((Logical) e).isActivated())
                                                    .collect(Collectors.toList());                  
        
        return adjacentEntities.size() == 1;
    }

    @Override
    public boolean shouldBeDeactivated(List<Entity> allEntities, Entity entity) {
        return !shouldBeActivated(allEntities, entity);
    }
}
