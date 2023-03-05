package dungeonmania.Entities;

import java.util.List;

import dungeonmania.Entities.Logical.Logical;
import dungeonmania.Entities.Logical.LogicalStrategy;
import dungeonmania.reactions.*;
import dungeonmania.util.*;;

public class Switch extends Entity implements Logical {
    private boolean activated;
    private boolean boulderOnSwitch;
    private LogicalStrategy logicalStrategy;
    private boolean wasActivatedThisTick;

    public Switch(String type, Position position) {
        super(type, position);
        this.logicalStrategy = null;
    }

    public Reaction interact(Entity e) {
        // entity still has it's old position, not the new one
        if (e instanceof Player) {
            boulderOnSwitch = false;
            return new ToggleLogical(this, "deactivate");
        } else if (e instanceof Boulder) {
            boulderOnSwitch = true;
            Boulder b = (Boulder) e;
            return new ActivateSwitch(this, b);
        }
        return null;
    }

    public boolean getSwitchStatus() {
        return boulderOnSwitch;
    }

    @Override
    public void addLogicStrategy(LogicalStrategy logicalStrategy) {
        this.logicalStrategy = logicalStrategy;
    }

    @Override
    public boolean isActivated() {
        return boulderOnSwitch || activated;
    }

    @Override
    public void activate() {
        this.activated = true;
        this.wasActivatedThisTick = true;
    }

    @Override
    public boolean shouldBeActivated(List<Entity> allEntities) {
        if (logicalStrategy == null) {
            return false;
        }

        return !boulderOnSwitch && logicalStrategy.shouldBeActivated(allEntities, this);
    }

    @Override
    public void deactivate() {
        this.activated = false;
    }

    @Override
    public boolean shouldBeDeactivated(List<Entity> allEntities) {
        if (logicalStrategy == null) {
            return !boulderOnSwitch && activated;
        }
        
        return !boulderOnSwitch && activated && logicalStrategy.shouldBeDeactivated(allEntities, this);
    }

    @Override
    public boolean wasActivatedThisTick() {
        return wasActivatedThisTick;
    }

    @Override
    public void resetWasActivatedThisTick() {
        this.wasActivatedThisTick = false;
    } 
}
