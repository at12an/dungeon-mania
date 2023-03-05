package dungeonmania.Entities;

import dungeonmania.util.Position;
import dungeonmania.reactions.*;
import dungeonmania.*;
import dungeonmania.util.Direction;
import java.util.*;

public class Assassin extends Entity implements Movement, Enemy, PlayerObserver, BecomeAlly {

    private double health;
    private int attack;
    private int bribeAmount;
    private double bribeFailRate;
    private int reconRadius;
    private boolean isPrimaryDirection;
    private boolean isAlly;
    private Player player;
    private int bribeRadius;
    private int allyAttack;
    private int allyDefence;
    private static Random rand = new Random();
    private Random randFailRate = new Random();
    private boolean controlled;
    private int duration;
    
    public Assassin(String type, Position position, int health, int attack, int bribeAmount, 
        double bribeFailRate, int reconRadius, int bribeRadius, int allyAttack, int allyDefence) {
            super(type, position);
            this.attack = attack;
            this.health = health;
            this.bribeAmount = bribeAmount;
            this.bribeFailRate = bribeFailRate;
            this.reconRadius = reconRadius;
            this.bribeRadius = bribeRadius;
            this.allyAttack = allyAttack;
            this.allyDefence = allyDefence;
            this.isPrimaryDirection = true;
            this.isAlly = false;
    }

    public void attachPlayer(Player player) {
        this.player = player;
    }

    public boolean isAllied() {
        return isAlly;
    }

    @Override
    public int getBribeAmount() {
        return bribeAmount;
    }

    @Override
    public void becomeAlly() {
        this.isAlly = true;
    }

    // dont refactor this doesnt work unless
    public boolean isInReconRadius() {
        if (this.getPosition().isWithinRadius(player.getPosition(), reconRadius)) {
            return true;
        }
        return false;
    }
    
    public boolean successfulBribe() {
        int multiplied = (int)(bribeFailRate * 100.0);
        int check = randFailRate.nextInt(99);
        return (check < multiplied) ? false : true;
    }

    public Reaction interact(Entity e) {
        if (e instanceof Player && !this.isAlly) {
            Player p = (Player) e;
            if (!p.isPlayerInvisible()) {
                return new Battle(this, p, super.getPosition());
            }
        }
        return null;
    }

    public void move(Position p) {
        this.setPosition(p);
    }

    public Position getNextPosition() {
        Position nextPos = null;

        if (isAlly) {
            nextPos = this.player.getPositionForAlly(this);
        } else {
            if (this.player.isPlayerInvisible() && !isInReconRadius()) {
                // move like a zombie
                Direction[] directions = new Direction[]{
                    Direction.UP,
                    Direction.DOWN,
                    Direction.LEFT,
                    Direction.RIGHT
                };
                // 3 size of directions
                nextPos = this.getPosition().translateBy(directions[rand.nextInt(3)]);
            } else if (player.isPlayerInvincible() || !isPrimaryDirection) {
                // for invincibility
                nextPos = this.getPosition().translateBy(
                    Direction.getDirectionFrom(player.getPosition(), getPosition(), true));
            } else {
                // normal assassin movement without potions and in recon radius if player invisible
                nextPos = this.getPosition().translateBy(
                    Direction.getDirectionFrom(getPosition(), player.getPosition(), isPrimaryDirection));
            }

        }
        return nextPos;
    }

    public boolean inBribeableRange(Player p){
        return
            (p.getPosition().getX() <= (this.getPosition().getX() + bribeRadius)) &&
            (p.getPosition().getX() >= (this.getPosition().getX() - bribeRadius)) &&
            (p.getPosition().getY() <= (this.getPosition().getY() + bribeRadius)) &&
            (p.getPosition().getY() >= (this.getPosition().getY() - bribeRadius))
        ;
    }

    public void changeDirection() {
        this.isPrimaryDirection = false;
    }

    public double getHealth() {
        return this.health;
    }
    
    public int getAttack() {
        return this.attack;
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public int getAllyAttack() {
        return allyAttack;
    }

    @Override
    public int getAllyDefence() {
        return allyDefence;
    }

    @Override
    public void becomeEnemy() {
        this.isAlly = false;
        // player now moves the merc
    }

    public void setControlled(boolean controlled) {
        this.controlled = controlled;
    }

    public boolean isControlled() {
        return controlled;
    }

    public void tickDuration() {
        this.duration = this.duration - 1;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public Boolean isInteractable() {
        return (isAlly) ? false : true;
    }
}
