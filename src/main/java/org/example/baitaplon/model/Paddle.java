package org.example.baitaplon.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Đại diện cho thanh đỡ (Paddle) do người chơi điều khiển.
 * Kế thừa từ MovableObject.
 */
public class Paddle extends MovableObject {

    protected double speed;
    private double gameWidth; // Lưu trữ chiều rộng của màn hình game
    private Image image;

    public Paddle(double x, double y, double width, double height, double speed, double gameWidth) {
        super(x, y, width, height);
        this.speed = speed;
        this.gameWidth = gameWidth;
        try {
            String imagePath = "/assets/paddle.png";
            this.image = new Image(getClass().getResource(imagePath).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Không thể load ảnh paddle: " + e.getMessage());
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Đặt vận tốc di chuyển sang trái.
     */
    public void moveLeft() {
        setDx(-speed);
    }

    /**
     * Đặt vận tốc di chuyển sang phải.
     */
    public void moveRight() {
        setDx(speed);
    }

    /**
     * Dừng di chuyển (đặt vận tốc ngang bằng 0).
     */
    public void stop() {
        setDx(0);
    }

    @Override
    public void update() {
        move(); // Cập nhật vị trí dựa trên dx

        // Giới hạn di chuyển trong lề trái
        if (x < 0) {
            x = 0;
            setDx(0); // Dừng lại khi chạm lề
        }

        // Giới hạn di chuyển trong lề phải
        if (x + width > this.gameWidth) {
            x = this.gameWidth - width;
            setDx(0); // Dừng lại khi chạm lề
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x, y, width, height);
        } else {
            // Hình ảnh thay thế nếu không tải được
            gc.setFill(javafx.scene.paint.Color.BLUE);
            gc.fillRect(x, y, width, height);
        }
    }

    public void setWidth(double v) {
        this.width = v;
    }
}