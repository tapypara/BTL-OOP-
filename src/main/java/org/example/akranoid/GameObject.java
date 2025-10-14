package org.example.akranoid;

/**
 * Lớp trừu tượng đại diện cho mọi đối tượng trong game.
 * Mọi vật thể (Ball, Paddle, Brick, ...) đều có vị trí, kích thước,
 * và khả năng cập nhật hoặc hiển thị.
 */
public abstract class GameObject {

    protected double x;        // Tọa độ X
    protected double y;        // Tọa độ Y
    protected double width;    // Chiều rộng
    protected double height;   // Chiều cao

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }


    /**
     * update object's state
     */
    public abstract void update();

    /**
     * draw.
     */
    public abstract void render();
}


