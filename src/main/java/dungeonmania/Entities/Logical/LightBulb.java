package dungeonmania.Entities.Logical;

import java.util.List;

import dungeonmania.Entities.Entity;
import dungeonmania.reactions.Reaction;
import dungeonmania.util.Position;

public class LightBulb extends Entity implements Logical {
    private LogicalStrategy logicalStrategy;
    private boolean activated;
    private boolean wasActivatedThisTick;

    public LightBulb(String type, Position position) {
        super(type, position);
    }

    @Override
    public Reaction interact(Entity e) {
        // entities can move over lightbulbs
        return null;
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
        return !activated && logicalStrategy.shouldBeActivated(allEntities, this);
    }

    @Override
    public boolean shouldBeDeactivated(List<Entity> allEntities) {
        return activated && logicalStrategy.shouldBeDeactivated(allEntities, this);
    }
    
    @Override
    public String getType() {
        if (activated) return "light_bulb_on";
        return "light_bulb_off";
    }
}
