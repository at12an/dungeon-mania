package dungeonmania.Entities.Logical;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Switch;
import dungeonmania.reactions.Reaction;
import dungeonmania.util.Position;

public class Wire extends Entity implements Logical {
    private LogicalStrategy logicalStrategy = new OrStrategy();
    private boolean activated;
    private boolean wasActivatedThisTick;

    public Wire(String type, Position position) {
        super(type, position);
    }

    @Override
    public Reaction interact(Entity e) {
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
        this.wasActivatedThisTick = true;
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
        return activated && !pathToActiveSwitch(allEntities, new ArrayList<>(), this);
    }

    private static boolean pathToActiveSwitch(List<Entity> allEntities, List<Entity> visited, Wire currentWire) {
        
        List<Entity> adjacentLogical = allEntities.stream()
                                                    .filter(e -> e instanceof Logical && 
                                                            Position.isAdjacent(currentWire.getPosition(), e.getPosition()) &&
                                                            ((Logical) e).isActivated())
                                                    .collect(Collectors.toList());

        // if there is an adjacent active switch, then path is found
        boolean adjacentActiveSwitch = adjacentLogical.stream().anyMatch(e -> e instanceof Switch);

        if (adjacentActiveSwitch) {
            return true;
        }

        // if there is an adjacent active wire that hasn't been visted, explore that (if true, return true)
        visited.add(currentWire);
        List<Entity> adjacentWires = adjacentLogical.stream().filter(e -> e instanceof Wire && !visited.contains(e)).collect(Collectors.toList());
        for (Entity e : adjacentWires) {
            if (pathToActiveSwitch(allEntities, visited, (Wire) e)) {
                return true;
            }
        }

        // no path found, return false

        return false;
    }
}
