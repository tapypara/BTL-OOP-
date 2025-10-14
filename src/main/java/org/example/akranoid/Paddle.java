package org.example.akranoid;

/**
 * Đại diện cho thanh đỡ (Paddle) do người chơi điều khiển.
 * Paddle có thể di chuyển trái/phải và nhận các hiệu ứng Power-up.
 */
public class Paddle extends MovableObject {

    protected double speed; // tốc độ di chuyển
    protected PowerUp currentPowerUp; // Power-up đang được áp dụng (nếu có)
    private double gameWidth; // chiều rộng màn hình game

    public Paddle(double x, double y, double width, double height, double speed, double gameWidth) {
        super(x, y, width, height);
        this.speed = speed;
        this.currentPowerUp = null;
        this.gameWidth = width;
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
        setDX(-speed);
    }

    public void moveRight() {
        setDX(speed);
    }

    public void stop() {
        setDX(0);
    }

    public void applyPowerUp(PowerUp powerUp) {
        // TODO: áp dụng hiệu ứng PowerUp
    }

    @Override
    public void update() {
        move();
        if (x < 0) {
            x = 0;
        }

        if (x + width > this.gameWidth) {
            x = this.Gamewidth - width;
        }
    }

    @Override
    public void render() {
        // TODO: vẽ paddle lên màn hình
    }
}

