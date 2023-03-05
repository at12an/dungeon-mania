package dungeonmania.Entities.Collectables;

import dungeonmania.Entities.*;
import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion {

    private int duration;

    public InvisibilityPotion(String type, Position position, int duration) {
        super(type, position);
        this.duration = duration;
    }

    @Override
    public boolean hasExpired() {
        return (duration > 0) ? false : true;
    }

    @Override
    public void lessDuration() {
        this.duration -= 1;
    }
}
