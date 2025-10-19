package org.example.arkanoid;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private boolean isGameOver;

    public GameManager() {
        paddle = new Paddle(350, 550, 100, 20, 10.0, SCREEN_WIDTH);
        ball = new Ball(0, 0, 10, 2.5, 0.5, -0.866);
        // GÁN THAM CHIẾU VÀ ĐẶT TRẠNG THÁI DÍNH
        ball.setPaddleReference(paddle);
        ball.setStuck(true); // Đảm bảo bóng dính ngay lập tức

        bricks = new ArrayList<>();
        createBricks();
        isGameOver = false;
    }

    private void createBricks() {
        bricks.clear();
        int brickWidth = 60;
        int brickHeight = 20;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                bricks.add(new Brick(j * (brickWidth + 10) + 55, i * (brickHeight + 10) + 50, brickWidth, brickHeight, 1, "blue"));
            }
        }
    }

    public void updateGame() {
        // Nếu game chưa kết thúc, cập nhật tất cả mọi thứ
        if (!isGameOver) {
            paddle.update();
            ball.update();
            checkCollisions();
        } else {
            // Nếu game đã kết thúc, CHỈ cập nhật bóng để nó tiếp tục rơi
            ball.update();
        }
    }

    private void checkCollisions() {
        // 1. Va chạm bóng với tường
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= SCREEN_WIDTH) {
            ball.setDirectionX(-ball.getDirectionX());
        }
        if (ball.getY() <= 0) {
            ball.setDirectionY(-ball.getDirectionY());
        }
        // Va chạm đáy (Game Over)
        if (ball.getY() + ball.getHeight() >= SCREEN_HEIGHT) {
            if (!isGameOver) { // Chỉ thực hiện một lần duy nhất
                System.out.println("Game Over!");
                isGameOver = true;
            }
        }

        // 2. Va chạm bóng với paddle
        if (ball.checkCollision(paddle)) {
            if (ball.getDirectionY() > 0) {
                ball.bounceOff(paddle);
            }
        }

        // 3. Va chạm bóng với gạch
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (brick.isDestroyed()) continue;

            if (ball.checkCollision(brick)) {
                ball.bounceOff(brick);
                brick.takeHit();

                if (brick.isDestroyed()) {
                    brickIterator.remove();
                }
                break;
            }
        }
    }

    public void renderGame(GraphicsContext gc) {
        paddle.render(gc);
        ball.render(gc);
        for (Brick brick : bricks) {
            brick.render(gc);
        }
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }
}

