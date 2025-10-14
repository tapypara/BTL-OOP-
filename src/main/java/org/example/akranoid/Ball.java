package org.example.akranoid;

/**
 * Đại diện cho quả bóng trong game.
 * Bóng có thể di chuyển, va chạm và nảy lại khi gặp vật cản.
 */
public class Ball extends MovableObject {

    protected double speed;       // tốc độ của bóng
    protected double directionX;  // hướng di chuyển theo trục X
    protected double directionY;  // hướng di chuyển theo trục Y

    public Ball(double x, double y, double radius, double speed, double directionX, double directionY) {
        super(x, y, radius * 2, radius * 2);
        this.speed = speed;
        this.directionX = directionX;
        this.directionY = directionY;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDirectionX() {
        return directionX;
    }

    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public double getDirectionY() {
        return directionY;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    public void bounceOff(GameObject other) {
        // TODO: xử lý nảy bóng khi va chạm với vật thể khác
    }

    public boolean checkCollision(GameObject other) {
        // TODO: kiểm tra va chạm giữa bóng và vật thể khác
        return false;
    }

    @Override
    public void update() {
        // TODO: cập nhật vị trí bóng dựa trên dx, dy
    }

    @Override
    public void render() {
        // TODO: vẽ bóng lên màn hình
    }
}
