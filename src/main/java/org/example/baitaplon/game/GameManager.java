// <<< SỬA DÒNG PACKAGE >>>
package org.example.baitaplon.game;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// <<< THÊM IMPORT CHO CÁC LỚP MODEL >>>
import org.example.baitaplon.model.Ball;
import org.example.baitaplon.model.Brick;
import org.example.baitaplon.model.Paddle;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javafx.scene.media.AudioClip;

import org.example.baitaplon.stat.StatManager;


public class GameManager {
    // ... (Code giữ nguyên) ...
    private boolean soundEnabled = false; // Mặc định là tắt
    private AudioClip paddleHitSound;
    private AudioClip brickHitSound;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    private Paddle paddle; // Đã import
    private Ball ball;     // Đã import
    private List<Brick> bricks; // Đã import
    private boolean isGameOver;
    private boolean isPaused = false;

    private int currentLevel;
    private static final int MAX_LEVEL = 5; // Ví dụ có 5 level
    private boolean isGameWon = false;

    // <<< THÊM HẰNG SỐ MẠNG SỐNG >>>
    private static final int INITIAL_LIVES = 2;

    private StatManager statManager;

    public GameManager() {
        paddle = new Paddle(350, 550, 100, 20, 5.0, SCREEN_WIDTH);
        ball = new Ball(0, 0, 10, 2, 0.5, -0.866);
        ball.setPaddleReference(paddle);
        ball.setStuck(true);

        bricks = new ArrayList<>();
        currentLevel = 1; // Bắt đầu từ level 1
        isGameOver = false;
        isGameWon = false;
        isPaused = false;

        // <<< THAY THẾ KHỞI TẠO >>>
        this.statManager = new StatManager(INITIAL_LIVES);

        loadSounds();
        // <<< THAY THẾ createBricks() BẰNG loadLevel() >>>
        try {
            loadLevel(currentLevel); // Tải level đầu tiên
        } catch (Exception e) {
            System.err.println("LỖI NGHIÊM TRỌNG: Không thể tải level " + currentLevel + ": " + e.getMessage());
            // Nếu không tải được level, coi như game over ngay
            isGameOver = true;
        }
    }

    // ... (Các hàm readLevelFile và loadLevel giữ nguyên y hệt như file bạn gửi) ...
    private String readLevelFile(int levelNumber) {
        String filePath = "/levels/level" + levelNumber + ".txt";
        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            if (is == null) {
                System.err.println("Lỗi: Không tìm thấy file: " + filePath);
                return null;
            }
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return content.replaceAll("[\\n\\r\\t]", "");

        } catch (Exception e) {
            System.err.println("Lỗi khi đọc file level " + levelNumber + ": " + e.getMessage());
            return null;
        }
    }

    private void loadLevel(int levelNumber) throws Exception {
        bricks.clear();
        ball.setStuck(true);

        String levelData = readLevelFile(levelNumber);

        // --- VALIDATION ---
        if (levelData == null) {
            throw new Exception("File level " + levelNumber + " rỗng hoặc không tìm thấy.");
        }
        if (levelData.length() != 70) {
            throw new Exception("File level " + levelNumber + " không chứa đúng 70 ký tự (đã có " + levelData.length() + ").");
        }
        if (levelData.matches(".*[^a-h_ ].*")) {
            throw new Exception("File level " + levelNumber + " chứa ký tự không hợp lệ (chỉ cho phép a-h, _, và dấu cách).");
        }
        // --- Hết VALIDATION ---

        int brickWidth = 80;
        int brickHeight = 40;
        final int INITIAL_Y = 50;
        final int ROWS = 7;
        final int COLS = 10;

        int charIndex = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                double x = j * brickWidth;
                double y = INITIAL_Y + i * brickHeight;
                char brickType = levelData.charAt(charIndex);
                charIndex++;

                switch (brickType) {
                    case 'a': // blue_brick.png (1 HP)
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "blue_brick"));
                        break;
                    case 'b': // red_brick.png (2 HP)
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 2, "red_brick"));
                        break;
                    case 'c': // green_brick.png (3 HP)
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 3, "green_brick"));
                        break;
                    case 'd': // yellow_brick.png (4 HP)
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 4, "yellow_brick"));
                        break;
                    case 'e': // pink_brick.png
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "pink_brick"));
                        break;
                    case 'f': // white_brick.png
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "white_brick"));
                        break;
                    case 'h': // doubleball_brick.png
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "doubleball_brick"));
                        break;
                    case 'g': // unbreakable_brick.png (Bất tử)
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 999, "unbreakable_brick"));
                        break;
                    case '_':
                    case ' ':
                        break;
                }
            }
        }
    }
    // ... (Kết thúc các hàm load level) ...


    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    private boolean areAllBricksCleared() {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                return false; // Còn gạch chưa vỡ
            }
        }
        return true; // Tất cả gạch đã vỡ
    }

    private void goToNextLevel() {
        this.currentLevel++;
        if (this.currentLevel > MAX_LEVEL) {
            this.isGameWon = true; // Thắng game!
            System.out.println("Chúc mừng! Bạn đã thắng!");
        } else {
            // Tải level tiếp theo
            try {
                loadLevel(this.currentLevel);
            } catch (Exception e) {
                System.err.println("LỖI: Không thể tải level " + this.currentLevel + ": " + e.getMessage());
                isGameOver = true;
            }
        }
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public void updateGame() {
        if (isPaused) {
            return;
        }

        if (!isGameOver && !isGameWon) {
            paddle.update();
            ball.update();
            checkCollisions(); // Logic Game Over/Trừ mạng đã được chuyển vào đây

            if (areAllBricksCleared()) {
                goToNextLevel();
            }
        } else {
            // Không làm gì nếu game đã kết thúc
        }
    }

    private void loadSounds() {
        try {
            String paddleSoundPath = "/assets/paddle_hit.wav";
            String brickSoundPath = "/assets/brick_hit.wav";
            paddleHitSound = new AudioClip(getClass().getResource(paddleSoundPath).toExternalForm());
            brickHitSound = new AudioClip(getClass().getResource(brickSoundPath).toExternalForm());
        } catch (Exception e) {
            System.err.println("⚠ Lỗi khi tải file âm thanh (SFX): " + e.getMessage());
            paddleHitSound = null;
            brickHitSound = null;
        }
    }

    private void checkCollisions() {
        // Va chạm tường
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= SCREEN_WIDTH) {
            ball.setDirectionX(-ball.getDirectionX());
        }
        if (ball.getY() <= 0) {
            ball.setDirectionY(-ball.getDirectionY());
        }

        // <<< THAY ĐỔI: LOGIC KHI BÓNG RƠI XUỐNG ĐÁY >>>
        if (ball.getY() + ball.getHeight() >= SCREEN_HEIGHT) {
            if (!isGameOver && !isGameWon) { // Thêm check isGameWon

                // 1. Trừ 1 mạng
                this.statManager.loseLife();

                // 2. Kiểm tra xem hết mạng chưa
                if (this.statManager.isOutOfLives()) {
                    System.out.println("Game Over!");
                    isGameOver = true; // Hết mạng -> Chính thức Game Over
                } else {
                    // 3. Nếu còn mạng, reset bóng
                    ball.setStuck(true);
                }
            }
        }
        // <<< KẾT THÚC THAY ĐỔI >>>

        // Va chạm Paddle
        if (ball.checkCollision(paddle)) {
            if (ball.getDirectionY() > 0) {
                ball.bounceOff(paddle);
                if (soundEnabled && paddleHitSound != null) {
                    paddleHitSound.play();
                }
            }
        }

        // Va chạm Gạch
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();

            // Bỏ qua gạch đã vỡ
            if (brick.isDestroyed()) {
                continue;
            }

            if (ball.checkCollision(brick)) {
                ball.bounceOff(brick);

                // <<< THAY ĐỔI: Gọi hàm addScore của StatManagement >>>
                int points = brick.takeHit();
                this.statManager.addScore(points);
                if ( brickHitSound != null) {
                    brickHitSound.play();
                }

                if (brick.isDestroyed()) {
                    // (Bạn sẽ thêm logic spawn power-up ở đây)
                }

                // Logic đã chuẩn: Không xóa gạch, chỉ break
                break;
            }
        }
    }

    public void renderGame(GraphicsContext gc) {
        // <<< THAY ĐỔI: Yêu cầu StatManager tự vẽ (cả điểm và mạng) >>>
        this.statManager.render(gc);

        paddle.render(gc);
        ball.render(gc);
        for (Brick brick : bricks) {
            brick.render(gc); // File Brick.java đã tự check !isDestroyed()
        }
        // (Đây là nơi bạn sẽ vẽ PowerUp)
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }

    // <<< THAY ĐỔI: getScore() giờ gọi từ statManager >>>
    public int getScore() {
        return this.statManager.getScore();
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }
}