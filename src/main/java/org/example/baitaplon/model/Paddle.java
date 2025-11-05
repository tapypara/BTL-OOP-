// <<< SỬA DÒNG PACKAGE >>>
package org.example.baitaplon.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

// <<< THÊM IMPORT CHO POWERUP (package con) >>>
import org.example.baitaplon.powerup.PowerUp;

/**
 * Đại diện cho thanh đỡ (Paddle) do người chơi điều khiển.
 * ...
 */
// <<< MovableObject ở cùng package nên KHÔNG CẦN IMPORT >>>
public class Paddle extends MovableObject {

    protected double speed;
    protected PowerUp currentPowerUp; // Đã import
    private double gameWidth;
    private Image image;

    public Paddle(double x, double y, double width, double height, double speed, double gameWidth) {
        // ... (Code giữ nguyên) ...
        super(x, y, width, height);
        this.speed = speed;
        this.currentPowerUp = null;
        this.gameWidth = gameWidth;
        try {
            String imagePath = "/assets/paddle.png";
            this.image = new Image(getClass().getResource(imagePath).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Không thể load ảnh paddle: " + e.getMessage());
        }
    }
    public double getSpeed() {
        // ... (Code giữ nguyên) ...
        return speed;
    }

    public void setSpeed(double speed) {
        // ... (Code giữ nguyên) ...
        this.speed = speed;
    }

    public PowerUp getCurrentPowerUp() {
        // ... (Code giữ nguyên) ...
        return currentPowerUp;
    }

    public void setCurrentPowerUp(PowerUp currentPowerUp) {
        // ... (Code giữ nguyên) ...
        this.currentPowerUp = currentPowerUp;
    }

    public void moveLeft() {
        // ... (Code giữ nguyên) ...
        setDx(-speed);
    }

    public void moveRight() {
        // ... (Code giữ nguyên) ...
        setDx(speed);
    }

    public void stop() {
        // ... (Code giữ nguyên) ...
        setDx(0);
    }

    public void applyPowerUp(PowerUp powerUp) {
        // ... (Code giữ nguyên) ...
    }

    @Override
    public void update() {
        // ... (Code giữ nguyên) ...
        move();
        if (x < 0) {
            x = 0;
            setDx(0);
        }

        if (x + width > this.gameWidth) {
            x = this.gameWidth - width;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // ... (Code giữ nguyên) ...
        if (image != null) {
            gc.drawImage(image, x, y, width, height);
        } else {
            gc.setFill(javafx.scene.paint.Color.BLUE);
            gc.fillRect(x, y, width, height);
        }
    }
}