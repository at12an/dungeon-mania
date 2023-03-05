package dungeonmania.reactions;

import java.util.Collection;
import java.util.PriorityQueue;

import dungeonmania.Dungeon;
import dungeonmania.Enemy;
import dungeonmania.Entities.Entity;

public class ChangeDirection extends Reaction {
    private static final int priority = 7;

    private Enemy enemy;

    public ChangeDirection(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void execute(Dungeon d, Collection<Reaction> reactions) {

        enemy.changeDirection();
        // clear the original movement reaction
        reactions.clear();
        // add a new movement reaction with changed direction
        PriorityQueue<Reaction> subsequentReactions = d.step((Entity) enemy, enemy.getNextPosition());

        if (!subsequentReactions.stream().anyMatch(e -> e.getClass().equals(ChangeDirection.class))){
            reactions.addAll(subsequentReactions);
        } // else reactions is already cleared.
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getType() {
        return "ChangeDirection";
    }
}
