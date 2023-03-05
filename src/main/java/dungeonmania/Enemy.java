package dungeonmania;

import dungeonmania.util.Position;

public interface Enemy {
    
    public Position getNextPosition();

    public void changeDirection();

    public double getHealth();
    
    public int getAttack();

    public void setHealth(double health);
}
