package org.example.baitaplon;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Đại diện cho thanh đỡ (Paddle) do người chơi điều khiển.
 * Paddle có thể di chuyển trái/phải và nhận các hiệu ứng Power-up.
 */
public class Paddle extends MovableObject {

    protected double speed; // tốc độ di chuyển
    protected PowerUp currentPowerUp; // Power-up đang được áp dụng (nếu có)
    private double gameWidth; // chiều rộng màn hình game
    private Image image;

    public Paddle(double x, double y, double width, double height, double speed, double gameWidth) {
        super(x, y, width, height);
        this.speed = speed;
        this.currentPowerUp = null;
        this.gameWidth = gameWidth;
        try {
            // Đường dẫn bắt đầu từ gốc của 'resources'
            String imagePath = "/assets/paddle.png";
            this.image = new Image(getClass().getResource(imagePath).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Không thể load ảnh paddle: " + e.getMessage());
            // Có thể thêm code để vẽ hình chữ nhật thay thế nếu không load được ảnh
        }
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
        setDx(-speed);
    }

    public void moveRight() {
        setDx(speed);
    }

    public void stop() {
        setDx(0);
    }

    public void applyPowerUp(PowerUp powerUp) {
        // TODO: áp dụng hiệu ứng PowerUp
    }

    @Override
    public void update() {
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
    public void render(GraphicsContext gc) { // Đảm bảo có tham số gc
        if (image != null) {
            // Chỉ vẽ ảnh nếu image không null
            gc.drawImage(image, x, y, width, height);

            // XÓA BỎ bất kỳ dòng code vẽ hình chữ nhật nào khác ở đây
            // Ví dụ: Đảm bảo không còn dòng gc.fillRect(...) nào trong khối if này
        } else {
            // Vẽ hình chữ nhật màu CHỈ KHI image là null
            gc.setFill(javafx.scene.paint.Color.BLUE); // Hoặc màu khác
            gc.fillRect(x, y, width, height);
        }
    }
}


