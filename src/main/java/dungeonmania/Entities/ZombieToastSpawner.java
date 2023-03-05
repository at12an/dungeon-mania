package dungeonmania.Entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.reactions.Reaction;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends Entity {

    public ZombieToastSpawner(String type, Position position) {
        super(type, position);
    }

    @Override
    public Reaction interact(Entity e) {
        return null;
    }

    public Position getSpawnPosition(List<Entity> currentEntities) {
        List<Direction> directions = new ArrayList<>();
        directions.add(Direction.UP);
        directions.add(Direction.DOWN);
        directions.add(Direction.LEFT);
        directions.add(Direction.RIGHT);

        for (Direction d : directions) {
            Position position = getPosition().translateBy(d);

            // check if the tile is open or not 
            boolean tileOccupied = currentEntities.stream()
                                                  .anyMatch(entity -> entity.getPosition().equals(position) && (entity instanceof Wall || entity instanceof Boulder));

            if (!tileOccupied) {
                return position;
            }
        }

        return null;
    }

    @Override
    public Boolean isInteractable() {
        return true;
    }

}
