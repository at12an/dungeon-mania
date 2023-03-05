package dungeonmania.Entities.Logical;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Switch;
import dungeonmania.util.Position;

public class AndStrategy implements LogicalStrategy {
    @Override
    public boolean shouldBeActivated(List<Entity> allEntities, Entity entity) {
        List<Entity> adjacentEntities = allEntities.stream()
                                                    .filter(e -> (e instanceof Switch || e instanceof Wire) &&
                                                            Position.isAdjacent(entity.getPosition(), e.getPosition()))
                                                    .collect(Collectors.toList());
        
        if (adjacentEntities.size() < 2) {
            return false;
        }                   

        for (Entity e : adjacentEntities) {
            if (!((Logical) e).isActivated()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean shouldBeDeactivated(List<Entity> allEntities, Entity entity) {
        return !shouldBeActivated(allEntities, entity);
    }
}
