package dungeonmania.reactions;

import dungeonmania.Dungeon;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entities.*;
import dungeonmania.util.Position;

public class Explode extends Reaction {
    Position position;
    int radius;

    public Explode(Position position, int radius) {
        this.position = position;
        this.radius = radius;
    }

    public void execute(Dungeon d, Collection<Reaction> reactions) {
        List<Entity> entitiesInRadius = d.getEntities()
                                         .stream()
                                         .filter(e -> !(e instanceof Player) && position.isWithinRadius(e.getPosition(), radius))
                                         .collect(Collectors.toList());

        for (Entity e : entitiesInRadius) {
            d.removeEntity(e);
        }
    }

    public int getPriority() {
        return 11;
    }

    public String getType() {
        return "Explode";
    }
}

