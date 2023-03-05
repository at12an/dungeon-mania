package dungeonmania.Entities;

import dungeonmania.Enemy;
import dungeonmania.TimeController;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.reactions.*;
import dungeonmania.Entities.Collectables.*;
import dungeonmania.Entities.Weapons.*;

public class OldPlayer extends Player implements Enemy {

    private TimeController moveReference;

    public OldPlayer(String type, Position position, int health, int attackDamage) {
        super(type, position, health, attackDamage);
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    
    @Override
    public Reaction interact(Entity e) {
        if (e instanceof Spider || e instanceof ZombieToast || (e instanceof Mercenary && !((Mercenary)e).isAllied())) {
            if (!isPlayerInvisible()) {
                return new Battle((Enemy) e, this, super.getPosition());
            }
        }
        if ((this instanceof OldPlayer && e instanceof OldPlayer == false)) {
            Player p = (Player) e;
            if (!p.isPlayerInvisible() && p.checkItemCount(SunStone.class) <= 0 && p.checkItemCount(MidnightArmour.class) <= 0) {
                return new Battle(this, p, super.getPosition());
            }
        }
        return null;
    }
    
    public Position getNextPosition() {
        String s = moveReference.getCurrentTimePeriod().getActions().stream().filter(x -> x.contains("move")).findFirst().orElse(null);
        if (s != null) {
            String[] commands = s.split("_");
            return this.getPosition().translateBy(Direction.valueOf(commands[1]));
        }
        return super.getPosition();
    }

    public void changeDirection() {
        return;
    }

    public double getHealth() {
        return super.getHealth();
    }
    
    public int getAttack() {
        return (int) super.getAttackDamage();
    }

    public void setHealth(double health) {
        super.setHealth(health);
    }

    public void setMoveReference(TimeController moveReference) {
        this.moveReference = moveReference;
    }

}
