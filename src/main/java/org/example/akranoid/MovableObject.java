package org.example.akranoid;

/**
 * Lớp trừu tượng mở rộng từ GameObject,
 * đại diện cho các đối tượng có thể di chuyển (Ball, Paddle, ...).
 */
public abstract class MovableObject extends GameObject {

    protected double dx; // vận tốc theo trục X
    protected double dy; // vận tốc theo trục Y

    public MovableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.dx = 0;
        this.dy = 0;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    /**
     * Cập nhật vị trí theo vận tốc.
     * Mọi đối tượng di chuyển đều nên gọi phương thức này trong update().
     */
    protected void move() {
        x += dx;
        y += dy;
    }

    /**
     * Mỗi đối tượng di chuyển cần tự định nghĩa cách cập nhật riêng.
     */
    @Override
    public abstract void update();

    /**
     * Vẽ đối tượng lên màn hình.
     */
    @Override
    public abstract void render();
}
