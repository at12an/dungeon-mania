package dungeonmania.Entities.Collectables;

import dungeonmania.Entities.*;
import dungeonmania.util.*;

public abstract class Potion extends Collectable {
    public Potion(String type, Position position) {
        super(type, position);
    }

    public abstract void lessDuration();
    public abstract boolean hasExpired();
}   
