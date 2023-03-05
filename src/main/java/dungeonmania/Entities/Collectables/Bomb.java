package dungeonmania.Entities.Collectables;

import dungeonmania.util.Position;
import dungeonmania.Entities.Logical.Logical;
import dungeonmania.Entities.Logical.LogicalStrategy;
import dungeonmania.reactions.*;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entities.*;


/**
 * @author Max Ovington
 */
public class Bomb extends Collectable implements Logical {
    private int radius;
    private boolean placed = false;
    private LogicalStrategy logicalStrategy;
    private boolean activated = false;

    public Bomb(String type, Position position, int radius) {
        super(type, position);
        this.radius = radius;
        this.logicalStrategy = null;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    @Override
    public Reaction interact(Entity e) {
        if (!placed) {
            return super.interact(e);
        }
        return new BlockEntry();
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void addLogicStrategy(LogicalStrategy logicalStrategy) {
        this.logicalStrategy = logicalStrategy; 
    }

    @Override
    public boolean isActivated() {
        // if a bomb is active, then it will explode so this is always false
        return false;
    }

    @Override
    public void activate() {
        this.activated = true;
    }

    @Override
    public boolean shouldBeActivated(List<Entity> allEntities) {
        if (logicalStrategy == null) {
            
            boolean shouldActivate = false;
            List<Entity> adjacentSwitches = allEntities.stream()
                                                        .filter(e -> e instanceof Switch && Position.isAdjacent(e.getPosition(), getPosition()))
                                                        .collect(Collectors.toList());

            for (Entity e : adjacentSwitches) {
                if (((Switch) e).isActivated()) {
                    shouldActivate = true;
                    break;
                }
            }
            
            return isPlaced() && shouldActivate;
        }
        return isPlaced() && logicalStrategy.shouldBeActivated(allEntities, this);
    }

    @Override
    public void deactivate() {
        this.activated = false;
    }

    @Override
    public boolean shouldBeDeactivated(List<Entity> allEntities) {
        // if a bomb is activated it explodes so this will always be false
        return false;
    }

    @Override
    public boolean wasActivatedThisTick() {
        // always false since bomb explodes once activated
        return false;
    }

    @Override
    public void resetWasActivatedThisTick() {
    }
}
