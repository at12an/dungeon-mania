package dungeonmania.Entities.Logical;

import java.util.List;

import dungeonmania.Entities.Door;
import dungeonmania.Entities.Entity;
import dungeonmania.util.Position;

public class SwitchDoor extends Door implements Logical {
    private LogicalStrategy logicalStrategy;
    private boolean activated;
    private boolean wasActivatedThisTick;

    public SwitchDoor(String type, Position position, int keyId) {
        super(type, position, keyId);
    }

    @Override
    public boolean isOpen() {
        return activated || super.isOpen();
    }

    @Override
    public void addLogicStrategy(LogicalStrategy logicalStrategy) {
        this.logicalStrategy = logicalStrategy;
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    @Override
    public boolean wasActivatedThisTick() {
        return wasActivatedThisTick;
    }

    @Override
    public void resetWasActivatedThisTick() {
        this.wasActivatedThisTick = false;
    }

    @Override
    public void activate() {
        this.activated = true;
    }

    @Override
    public void deactivate() {
        this.activated = false;
    }

    @Override
    public boolean shouldBeActivated(List<Entity> allEntities) {
        return !this.isOpen() && logicalStrategy.shouldBeActivated(allEntities, this);
    }

    @Override
    public boolean shouldBeDeactivated(List<Entity> allEntities) {
        return activated && !super.isOpen() && logicalStrategy.shouldBeDeactivated(allEntities, this);
    }
}
