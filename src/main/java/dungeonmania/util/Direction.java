package dungeonmania.util;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final Position offset;

    private Direction(Position offset) {
        this.offset = offset;
    }

    private Direction(int x, int y) {
        this.offset = new Position(x, y);
    }

    public Position getOffset() {
        return this.offset;
    }
    /**
     * 
     * @param a
     * @param b
     * @return direction to b from a.
     */
    public static Direction getDirectionOf(Position a, Position b){

        int x = (b.getX() - a.getX()) % 2;
        int y = (b.getY() - a.getY()) % 2;

        if (x == 0  && y == -1) return UP;
        if (x == 0  && y ==  1) return DOWN;
        if (x == -1 && y ==  0) return LEFT;
        return RIGHT;
    } 

    public static Direction getDirectionFrom(Position a, Position b, boolean isPrimaryDirection) {
        int xDiff = b.getX() - a.getX();
        int yDiff = b.getY() - a.getY();
        
        Direction direction = null;

        if (isPrimaryDirection) {
            if (Math.abs(xDiff) >= Math.abs(yDiff)) {
                // x is more significant, go LEFT or RIGHT
                direction = xDiff <= 0 ? LEFT : RIGHT;
            } else {
                // y is more significant, go UP or DOWN
                direction = yDiff <= 0 ? UP : DOWN;
            }
        } else {
            if (Math.abs(xDiff) >= Math.abs(yDiff)) {
                // x is more significant, but we want secondary direction (UP/DOWN)
                direction = yDiff <= 0 ? UP : DOWN;  
            } else {
                // y is more significant, but we want secondary direction (L/R)
                direction = xDiff <= 0 ? LEFT : RIGHT;
            }
        }
        
        return direction;
    }
}
