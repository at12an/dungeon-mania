package dungeonmania.Entities;

import dungeonmania.*;
import dungeonmania.reactions.Battle;
import dungeonmania.reactions.Reaction;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.*;

public class Mercenary extends Entity implements Movement, Enemy, PlayerObserver, BecomeAlly {

    private double health;
    private int attack;
    private int bribeAmount;
    private int bribeRadius;
    private Player player;
    private boolean isPrimaryDirection;
    private static Random rand = new Random();

    private boolean isAlly;
    private int allyAttack;
    private int allyDefence;
    private boolean controlled;
    private int duration;

    public Mercenary(String type, Position position, int health, int attack, int bribeAmount, int bribeRadius,
                    int allyAttack, int allyDefence) {
        super(type, position);
        this.health = health;
        this.attack = attack;
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.isPrimaryDirection = true;
        this.isAlly = false;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public boolean isAllied(){
        return isAlly;
    }
    
    @Override
    public int getAllyAttack() {
        return allyAttack;
    }

    @Override
    public int getAllyDefence() {
        return allyDefence;
    }

    public Position getNextPosition() {

        Position rtn = null;

        if (isAlly){
           rtn = this.player.getPositionForAlly(this);
        } else {
            // If player invisible, mercenary has zombie movement
            if (player.isPlayerInvisible()) {
                Direction[] directions = new Direction[]{
                    Direction.UP,
                    Direction.DOWN,
                    Direction.LEFT,
                    Direction.RIGHT
                };
                // 3 size of directions
                rtn = this.getPosition().translateBy(directions[rand.nextInt(3)]);

            } else if (player.isPlayerInvincible() || !isPrimaryDirection){
                rtn = this.getPosition().translateBy(
                    Direction.getDirectionFrom(player.getPosition(), getPosition(), true));
            } else {
                rtn = this.getPosition().translateBy(
                    Direction.getDirectionFrom(getPosition(), player.getPosition(), isPrimaryDirection));
            }
            isPrimaryDirection = true;
        }
        return rtn;
    }

    public Reaction interact(Entity e) {
        if (e instanceof Player  && !this.isAlly) {
            Player p = (Player) e;
            if (!p.isPlayerInvisible()) {
                return new Battle(this, p, super.getPosition());
            }
        }
        return null;
    }
    
    public void move(Position p){
        this.setPosition(p);
    }

    @Override
    public int getBribeAmount(){
        return this.bribeAmount;
    }

    public boolean inBribeableRange(Player p){
        return
            (p.getPosition().getX() <= (this.getPosition().getX() + bribeRadius)) &&
            (p.getPosition().getX() >= (this.getPosition().getX() - bribeRadius)) &&
            (p.getPosition().getY() <= (this.getPosition().getY() + bribeRadius)) &&
            (p.getPosition().getY() >= (this.getPosition().getY() - bribeRadius))
        ;
    }

    @Override
    public void becomeAlly() {
        this.isAlly = true;
        // player now moves the merc
    }

    @Override
    public void becomeEnemy() {
        this.isAlly = false;
        // player now moves the merc
    }

    @Override
    public void attachPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void changeDirection() {
        this.isPrimaryDirection = false;
    }

    @Override
    public Boolean isInteractable() {
        return (isAlly) ? false : true;
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
    
    
}
