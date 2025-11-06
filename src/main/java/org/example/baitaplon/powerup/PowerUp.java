package org.example.baitaplon.powerup;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.baitaplon.game.GameManager;
import org.example.baitaplon.model.Ball;
import org.example.baitaplon.model.Brick;
import org.example.baitaplon.model.GameObject;
import org.example.baitaplon.model.Paddle;
import org.example.baitaplon.stat.StatManager;


public class PowerUp extends GameObject {
    private String type;
    private Image image;
    private boolean active;
    private boolean isAlive; // <--- Thêm biến này;
    final static double speedY = 1;

    public PowerUp(double x, double y,String type) {
        super(x, y, 10,10);
        this.type = type;
        loadImageByType(type);
        this.active = false;
        this.isAlive = true;
    }

    private void loadImageByType(String typeName) {
        String imagePath = "/assets/" + typeName + ".png";
        try {
            this.image = new Image(getClass().getResource(imagePath).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("⚠ Lỗi: Không tìm thấy ảnh gạch: " + imagePath);
        }
    }

    @Override
    public void update() {
        y += speedY;
        if (y > 600) {
            isAlive = false;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // Chỉ làm bất cứ điều gì nếu PowerUp còn "sống"
        if (isAlive) {
            if (image != null) {
                // Nếu có ảnh, vẽ ảnh
                gc.drawImage(image, x, y, width, height);
            } else {
                // Nếu không có ảnh, vẽ hình vuông màu xanh
                gc.setFill(javafx.scene.paint.Color.BLUE);
                gc.fillRect(x, y, width, height);
            }
        }
        // Không làm gì cả nếu isAlive = false
    }
    public boolean checkCollision(Paddle paddle) {
        return (x < paddle.getX() + paddle.getWidth() &&
                x + width > paddle.getX() &&
                y < paddle.getY() + paddle.getHeight() &&
                y + height > paddle.getY());
    }

    public void activate(GameManager gameManager) {
        if (active) return;
        active = true;
        Paddle paddle = gameManager.getPaddle();
        Ball ball = gameManager.getBall();
        StatManager stat = gameManager.getStatManager();
        isAlive = false;

        switch (type) {
            case "upsidepaddle_brick":
                paddle.setWidth(paddle.getWidth() * 1.1);
                break;
            case "speedup_brick":
                ball.setSpeed(ball.getSpeed() * 3);
                break;
            case "speeddown_brick":
                ball.setSpeed(ball.getSpeed() * 0.5);
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
