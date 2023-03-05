package dungeonmania.Entities.Logical;

import java.util.List;

import dungeonmania.Entities.Entity;

public interface Logical {
    public void addLogicStrategy(LogicalStrategy logicalStrategy);
    public boolean isActivated();
    public boolean wasActivatedThisTick();
    public void resetWasActivatedThisTick();
    public void activate();
    public void deactivate();
    public boolean shouldBeActivated(List<Entity> allEntities);
    public boolean shouldBeDeactivated(List<Entity> allEntities);
}
