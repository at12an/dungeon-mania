package dungeonmania.Entities;

import java.util.Random;

import dungeonmania.Enemy;
import dungeonmania.Movement;
import dungeonmania.PlayerObserver;
import dungeonmania.reactions.Reaction;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import dungeonmania.reactions.*;;

public class ZombieToast extends Entity implements Enemy, Movement, PlayerObserver {
    private double health;
    private int attackDamage;
    private Player player;
    private static Random rand = new Random();
    
    public ZombieToast(String type, Position position, int health, int attackDamage) {
        super(type, position);
        this.health = health;
        this.attackDamage = attackDamage;
    }

    @Override
    public Reaction interact(Entity e) {
        if (e instanceof Player) {
            Player reCasted = (Player) e;
            if (reCasted.isPlayerInvisible()) {
                return null;
            }
            return new Battle(this, reCasted, super.getPosition());
        }
        return null;
    }

    public void move(Position p) {
        this.setPosition(p);
    }

    public Position getNextPosition() {
        
        //TODO: invincible movement

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
            this.getPosition().translateBy(directions[rand.nextInt(3)]); // 3 = size of directions

    }

    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public int getAttack() {
        return attackDamage;
    }

    @Override
    public void changeDirection() {
        // Not required
    }

    @Override
    public void attachPlayer(Player player) {
        this.player = player;
    }
    
}
