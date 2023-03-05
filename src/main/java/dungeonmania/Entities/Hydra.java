package dungeonmania.Entities;

import dungeonmania.util.*;

import java.util.Random;

import dungeonmania.Enemy;
import dungeonmania.Movement;
import dungeonmania.PlayerObserver;
import dungeonmania.reactions.*;

public class Hydra extends Entity implements Movement, Enemy, PlayerObserver {
    
    private int attack;
    private double health;
    private int increaseAmount;
    private double increaseRate;
    private Random rand = new Random();
    private Player player;

    public Hydra(String type, Position position, int attack, int health, int increaseAmount, double increaseRate) {
        super(type, position);
        this.attack = attack;
        this.health = health;
        this.increaseAmount = increaseAmount;
        this.increaseRate = increaseRate;
    }

    public void attachPlayer(Player player) {
        this.player = player;
    }

    public Reaction interact(Entity e) {
        if (e instanceof Player) {
            Player p = (Player) e;
            if (!p.isPlayerInvisible()) {
                return new Battle(this, p, super.getPosition());
            }
        }
        return null;
    } 

    public int getIncreaseAmount() {
        return this.increaseAmount;
    }

    public boolean isIncreased() {
        int multiplied = (int)(increaseRate * 100.0);
        int check = rand.nextInt(99);
        return (check < multiplied) ? true : false;
    }

    @Override
    public void move(Position p) {
        this.setPosition(p);
    }

    @Override
    public Position getNextPosition() {
        Direction[] directions = new Direction[]{
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
        };
        return (player.isPlayerInvincible()) ?
            this.getPosition().translateBy( // move away from player.
                Direction.getDirectionFrom(player.getPosition(), getPosition(), true)
            )
            :   
            this.getPosition().translateBy(directions[rand.nextInt(3)]);
    }

    @Override
    public void changeDirection() {
        // not applicable
    }

    @Override
    public double getHealth() {
        return this.health;
    }
    
    @Override
    public int getAttack() {
        return this.attack;
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }
}
