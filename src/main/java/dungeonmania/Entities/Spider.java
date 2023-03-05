package dungeonmania.Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.Enemy;
import dungeonmania.Movement;
import dungeonmania.reactions.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends Entity implements Movement, Enemy {
    private List<Direction> clockwiseTrajectory;
    private List<Direction> antiClockwiseTrajectory;
    private boolean isClockwise;
    private int trajectoryPosition;
    private double health;
    private int attackDamage;
    private static Random rand = new Random();

    public Spider(String type, Position position, int health, int attackDamage) {
        super(type, position);
        this.health = health;
        this.attackDamage = attackDamage;

        this.clockwiseTrajectory = getTrajectory(true);
        this.antiClockwiseTrajectory = getTrajectory(false);
        this.trajectoryPosition = -1;
        this.isClockwise = true;
    }

    public Reaction interact(Entity e) {    
        if (e instanceof Player){
            Player p = (Player) e;
            if (!p.isPlayerInvisible()) {
                return new Battle(this, p, super.getPosition());
            }
        }
        return null;
    }

    public double getHealth() {
        return this.health;
    }

    public int getAttack() {
        return this.attackDamage;
    }

    @Override
    public void move(Position p) {
        // adjust the trajectory position since the movement is successful
        if (trajectoryPosition == -1 && !isClockwise) {
            trajectoryPosition = 4;
        } else if (isClockwise) {
            trajectoryPosition++;
        } else {
            trajectoryPosition--;
        }

        // Mod 8 to force between 0 - 7
        trajectoryPosition = (trajectoryPosition + 8) % 8;

        this.setPosition(p);
    }

    public Position getNextPosition() {
        // Start upwards
        if (trajectoryPosition == -1) {
           
            return (isClockwise) ?
                 this.getPosition().translateBy(Direction.UP)
                 :
                 this.getPosition().translateBy(Direction.DOWN);
            //   ^^ Case for spider starting under a boulder ^^

        }
        return (isClockwise) ?
            this.getPosition().translateBy(clockwiseTrajectory.get(trajectoryPosition))
            :
            this.getPosition().translateBy(antiClockwiseTrajectory.get(trajectoryPosition));
    }

    public void changeDirection() {
        this.isClockwise = !isClockwise;
    }

    public static Position getSpawnPosition(List<Entity> allEntities) {
        // Find max x, y
        int maxX = allEntities.stream()
                              .reduce((e1, e2) -> e1.getPosition().getX() > e2.getPosition().getX() ? e1: e2)
                              .get()
                              .getPosition()
                              .getX();

        int maxY = allEntities.stream()
                              .reduce((e1, e2) -> e1.getPosition().getY() > e2.getPosition().getY() ? e1: e2)
                              .get()
                              .getPosition()
                              .getY();
        
        // get random start point and check the entities here are only walls or collectables
        int x = rand.nextInt(maxX <= 0 ? 1: maxX);
        int y = rand.nextInt(maxY <= 0 ? 1: maxY);

        boolean alreadyOccupied = allEntities.stream()
                                             .anyMatch(e -> e.getPosition().equals(new Position(x, y)) && !(e instanceof Wall || e instanceof Collectable));

        // if not, then just spawn on edge of map
        if (alreadyOccupied) {
            new Position(x, maxY + 1);
        }
        
        return new Position(x, y);
    }

    public void setHealth(double health) {
        this.health = health;
    }

    private List<Direction> getTrajectory(Boolean isClockwise) {
        List<Direction> trajectory = new ArrayList<>();
        
        if (isClockwise) {
            trajectory.add(Direction.RIGHT);
            trajectory.add(Direction.DOWN);
            trajectory.add(Direction.DOWN);
            trajectory.add(Direction.LEFT);
            trajectory.add(Direction.LEFT);
            trajectory.add(Direction.UP);
            trajectory.add(Direction.UP);
            trajectory.add(Direction.RIGHT);
        } else {
            trajectory.add(Direction.LEFT);
            trajectory.add(Direction.LEFT);
            trajectory.add(Direction.UP);
            trajectory.add(Direction.UP);
            trajectory.add(Direction.RIGHT);
            trajectory.add(Direction.RIGHT);
            trajectory.add(Direction.DOWN);
            trajectory.add(Direction.DOWN);
        }

        return trajectory;
    }

}
