package org.example.baitaplon.powerup;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.baitaplon.game.GameManager;
import org.example.baitaplon.model.Ball;
import org.example.baitaplon.model.GameObject;
import org.example.baitaplon.model.Paddle;
import org.example.baitaplon.stat.StatManager;

/**
 * Đại diện cho một vật phẩm (Power-Up) rơi ra từ gạch.
 */
public class PowerUp extends GameObject {

    private String type;    // Loại power-up, dùng để load ảnh và kích hoạt
    private Image image;
    private boolean active;  // Đã kích hoạt chưa
    private boolean isAlive; // Vật phẩm còn tồn tại trên màn hình không
    final static double SPEED_Y = 1.0; // Tốc độ rơi cố định

    public PowerUp(double x, double y, String type) {
        // Kích thước vật phẩm (sửa lại cho đúng K&R style)
        super(x, y, 10, 10);
        this.type = type;
        loadImageByType(type);
        this.active = false;
        this.isAlive = true;
    }

    /**
     * Tải ảnh cho power-up.
     * Lưu ý: Tên ảnh dựa trên type của gạch (ví dụ: "addlife_brick.png").
     */
    private void loadImageByType(String typeName) {
        String imagePath = "/assets/" + typeName + ".png";
        try {
            this.image = new Image(getClass().getResource(imagePath).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("⚠ Lỗi: Không tìm thấy ảnh power-up: " + imagePath);
        }
    }

    @Override
    public void update() {
        // Di chuyển vật phẩm rơi xuống
        y += SPEED_Y;

        // Tự hủy nếu rơi ra khỏi màn hình
        if (y > 600) {
            isAlive = false;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // Chỉ vẽ nếu vật phẩm còn "sống"
        if (isAlive) {
            if (image != null) {
                gc.drawImage(image, x, y, width, height);
            } else {
                // Hình ảnh thay thế nếu không tải được
                gc.setFill(javafx.scene.paint.Color.BLUE);
                gc.fillRect(x, y, width, height);
            }
        }
    }

    /**
     * Kiểm tra va chạm giữa vật phẩm và thanh đỡ.
     */
    public boolean checkCollision(Paddle paddle) {
        return (x < paddle.getX() + paddle.getWidth() &&
                x + width > paddle.getX() &&
                y < paddle.getY() + paddle.getHeight() &&
                y + height > paddle.getY());
    }

    /**
     * Kích hoạt hiệu ứng của power-up khi va chạm với thanh đỡ.
     *
     * @param gameManager Dùng để truy cập Paddle, Ball, và StatManager.
     */
    public void activate(GameManager gameManager) {
        if (active) {
            return; // Chỉ kích hoạt một lần
        }
        active = true;
        isAlive = false; // Biến mất ngay khi kích hoạt

        Paddle paddle = gameManager.getPaddle();
        Ball ball = gameManager.getBall();
        StatManager stat = gameManager.getStatManager();

        switch (type) {
            case "upsidepaddle_brick":
                paddle.setWidth(paddle.getWidth() * 1.1);
                break;
            case "speedup_brick":
                ball.setSpeed(ball.getSpeed() * 1.5);
                ball.setWidth(ball.getWidth() * 0.7);
                ball.setHeight(ball.getHeight() * 0.7);
                break;
            case "speeddown_brick":
                ball.setSpeed(ball.getSpeed() * 0.7);
                ball.setWidth(ball.getWidth() * 1.3);
                ball.setHeight(ball.getHeight() * 1.3);
                break;
            case "addlife_brick":
                stat.addLife();
                break;
            default:
                System.out.println("Unknown power-up: " + type);
                break;
        }
    }

    public boolean isActive() {
        return active;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public String getType() {
        return type;
    }
}