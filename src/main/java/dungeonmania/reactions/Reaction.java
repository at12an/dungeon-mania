package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;

public abstract class Reaction implements Comparable<Reaction>{
    
    public abstract void execute(Dungeon d, Collection<Reaction> reactions);
    
    public abstract int getPriority();

    public abstract String getType();

    @Override
    public int compareTo(Reaction r){
        if (this.getPriority() <  r.getPriority()) return -1;
        if (this.getPriority() == r.getPriority()) return 0;
        else return 1;
    }

    @Override
    public String toString() {
        return String.format("%s", this.getType());
    }
}
