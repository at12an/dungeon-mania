package dungeonmania.Entities.Collectables;

import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {

    private int duration;

    public InvincibilityPotion(String type, Position position, int duration) {
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
