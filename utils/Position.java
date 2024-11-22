package rpg.utils;

public class Position {
    private float x;
    private float y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    // Met à jour la position
    public void update(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    // Calcule la distance entre cette position et une autre position
    public double distanceTo(Position other) {
        float dx = other.getX() - this.x;
        float dy = other.getY() - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
