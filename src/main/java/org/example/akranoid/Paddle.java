package org.example.akranoid;

/**
 * Đại diện cho thanh đỡ (Paddle) do người chơi điều khiển.
 * Paddle có thể di chuyển trái/phải và nhận các hiệu ứng Power-up.
 */
public class Paddle extends MovableObject {

    protected double speed; // tốc độ di chuyển
    protected PowerUp currentPowerUp; // Power-up đang được áp dụng (nếu có)

    public Paddle(double x, double y, double width, double height, double speed) {
        super(x, y, width, height);
        this.speed = speed;
        this.currentPowerUp = null;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public PowerUp getCurrentPowerUp() {
        return currentPowerUp;
    }

    public void setCurrentPowerUp(PowerUp currentPowerUp) {
        this.currentPowerUp = currentPowerUp;
    }

    public void moveLeft() {
        // TODO: di chuyển paddle sang trái
    }

    public void moveRight() {
        // TODO: di chuyển paddle sang phải
    }

    public void applyPowerUp(PowerUp powerUp) {
        // TODO: áp dụng hiệu ứng PowerUp
    }

    @Override
    public void update() {
        // TODO: cập nhật vị trí hoặc trạng thái paddle
    }

    @Override
    public void render() {
        // TODO: vẽ paddle lên màn hình
    }
}

