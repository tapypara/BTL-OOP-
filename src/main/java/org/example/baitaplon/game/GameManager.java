package org.example.baitaplon.game;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import org.example.baitaplon.model.Ball;
import org.example.baitaplon.model.Brick;
import org.example.baitaplon.model.Paddle;
import org.example.baitaplon.powerup.PowerUp;
import org.example.baitaplon.sound.SoundManager;
import org.example.baitaplon.stat.StatManager;

/**
 * Lớp quản lý chính (Controller) cho logic của trò chơi.
 * Chịu trách nhiệm quản lý trạng thái game, cập nhật đối tượng,
 * xử lý va chạm và tải các màn chơi (level).
 */
public class GameManager {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;

    private boolean isGameOver;
    private boolean isPaused = false;
    private boolean isGameWon = false;

    private int currentLevel;
    private static final int MAX_LEVEL = 5; // Số màn chơi tối đa
    private static final int INITIAL_LIVES = 2; // Số mạng ban đầu

    private StatManager statManager; // Lớp quản lý điểm và mạng sống

    public GameManager() {
        paddle = new Paddle(350, 550, 100, 20, 5.0, SCREEN_WIDTH);
        ball = new Ball(0, 0, 10, 2, 0.5, -0.866);
        ball.setPaddleReference(paddle); // Gắn bóng vào thanh đỡ
        ball.setStuck(true);

        bricks = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        currentLevel = 1;
        isGameOver = false;
        isGameWon = false;
        isPaused = false;

        this.statManager = new StatManager(INITIAL_LIVES);

        try {
            loadLevel(currentLevel);
        } catch (Exception e) {
            System.err.println("LỖI NGHIÊM TRỌNG: Không thể tải level " + currentLevel + ": " + e.getMessage());
            isGameOver = true;
        }
    }

    /**
     * Đọc nội dung từ file level .txt trong thư mục /resources/levels.
     *
     * @param levelNumber Số thứ tự của màn chơi.
     * @return Chuỗi String chứa 70 ký tự (hoặc null nếu lỗi).
     */
    private String readLevelFile(int levelNumber) {
        String filePath = "/levels/level" + levelNumber + ".txt";
        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            if (is == null) {
                System.err.println("Lỗi: Không tìm thấy file: " + filePath);
                return null;
            }
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            // Xóa tất cả các ký tự xuống dòng, tab...
            return content.replaceAll("[\\n\\r\\t]", "");
        } catch (Exception e) {
            System.err.println("Lỗi khi đọc file level " + levelNumber + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Tải và xây dựng màn chơi dựa trên dữ liệu từ file level.
     *
     * @param levelNumber Số thứ tự màn chơi.
     * @throws Exception Nếu file level không hợp lệ.
     */
    private void loadLevel(int levelNumber) throws Exception {
        bricks.clear();
        this.powerUps.clear();
        ball.setStuck(true); // Gắn lại bóng vào thanh đỡ khi bắt đầu màn mới

        String levelData = readLevelFile(levelNumber);

        // --- Xác thực dữ liệu level ---
        if (levelData == null) {
            throw new Exception("File level " + levelNumber + " rỗng hoặc không tìm thấy.");
        }
        if (levelData.length() != 70) {
            throw new Exception(
                    "File level " + levelNumber + " không chứa đúng 70 ký tự (đã có " + levelData.length()
                            + ").");
        }
        if (levelData.matches(".*[^a-j_ ].*")) {
            throw new Exception("File level " + levelNumber + " chứa ký tự không hợp lệ.");
        }

        // --- Cấu hình gạch ---
        int brickWidth = 80;  // 800 / 10 cột
        int brickHeight = 40; // Chiều cao cố định
        final int INITIAL_Y = 50; // Vị trí Y bắt đầu vẽ hàng gạch đầu tiên
        final int ROWS = 7;
        final int COLS = 10;

        int charIndex = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                double x = j * brickWidth;
                double y = INITIAL_Y + i * brickHeight;
                char brickType = levelData.charAt(charIndex);
                charIndex++;

                // Dựa vào ký tự để tạo loại gạch tương ứng
                switch (brickType) {
                    case 'a': // Gạch 1 máu
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "blue_brick"));
                        break;
                    case 'b': // Gạch 2 máu
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 2, "red_brick"));
                        break;
                    case 'c': // Gạch 3 máu
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 3, "green_brick"));
                        break;
                    case 'd': // Gạch 4 máu
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 4, "yellow_brick"));
                        break;
                    case 'e': // Power-up: Thêm mạng
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "addlife_brick"));
                        break;
                    case 'f': // Power-up: Giảm tốc bóng
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "speeddown_brick"));
                        break;
                    case 'h': // Power-up: Tăng kích thước thanh đỡ
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "upsidepaddle_brick"));
                        break;
                    case 'g': // Gạch không thể phá hủy
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 999, "unbreakable_brick"));
                        break;
                    case 'i': // Gạch bom
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "bomb_brick"));
                        break;
                    case 'j': // Power-up: Tăng tốc bóng
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "speedup_brick"));
                        break;
                    case '_':
                    case ' ':
                        // Bỏ qua, không tạo gạch
                        break;
                }
            }
        }
    }

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

    /**
     * Kiểm tra xem tất cả gạch (trừ gạch "unbreakable") đã bị phá hủy chưa.
     *
     * @return true nếu đã phá hết, false nếu còn.
     */
    private boolean areAllBricksCleared() {
        for (Brick brick : bricks) {
            // Nếu tìm thấy một gạch còn sống VÀ không phải là gạch không thể vỡ
            if (!brick.isDestroyed() && !brick.getType().equals("unbreakable_brick")) {
                return false;
            }
        }
        return true; // Không tìm thấy gạch nào còn sống
    }

    /**
     * Chuyển sang màn chơi tiếp theo hoặc kết thúc game nếu đã hoàn thành màn cuối.
     */
    private void goToNextLevel() {
        this.currentLevel++;
        if (this.currentLevel > MAX_LEVEL) {
            this.isGameWon = true; // Thắng game
            System.out.println("Chúc mừng! Bạn đã thắng!");
        } else {
            // Qua màn: Reset lại số mạng về ban đầu (nhưng giữ nguyên điểm)
            this.statManager.resetLives();

            try {
                loadLevel(this.currentLevel); // Tải màn chơi mới
            } catch (Exception e) {
                System.err.println("LỖI: Không thể tải level " + this.currentLevel + ": " + e.getMessage());
                isGameOver = true; // Kết thúc game nếu không tải được màn
            }
        }
    }

    /**
     * Hàm cập nhật chính, được gọi liên tục bởi AnimationTimer trong MainApplication.
     * Dùng để cập nhật logic của tất cả các đối tượng trong game.
     */
    public void updateGame() {
        if (isPaused) {
            return; // Dừng cập nhật nếu game đang tạm dừng
        }

        if (!isGameOver && !isGameWon) {
            paddle.update();
            ball.update();

            // Cập nhật các Power-up
            // (Dùng vòng lặp for-i để tránh ConcurrentModificationException khi xóa)
            for (int i = 0; i < powerUps.size(); i++) {
                PowerUp powerUp = powerUps.get(i);
                powerUp.update(); // Cho power-up di chuyển (rơi xuống)

                // Nếu va chạm với thanh đỡ, kích hoạt
                if (powerUp.isAlive() && powerUp.checkCollision(paddle)) {
                    powerUp.activate(this);
                }

                // Nếu power-up không còn "sống" (đã kích hoạt hoặc rơi ra ngoài), xóa nó
                if (!powerUp.isAlive()) {
                    powerUps.remove(i);
                    i--;
                }
            }

            checkCollisions(); // Kiểm tra va chạm (Bóng-Tường, Bóng-Gạch...)

            // Kiểm tra điều kiện thắng màn
            if (areAllBricksCleared()) {
                goToNextLevel();
            }
        }
    }

    /**
     * Xử lý logic va chạm của bóng với tường, thanh đỡ và gạch.
     */
    private void checkCollisions() {
        // 1. Va chạm tường (Trái, Phải, Trên)
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= SCREEN_WIDTH) {
            ball.setDirectionX(-ball.getDirectionX());
        }
        if (ball.getY() <= 0) {
            ball.setDirectionY(-ball.getDirectionY());
        }

        // 2. Va chạm đáy (Rớt bóng)
        if (ball.getY() + ball.getHeight() >= SCREEN_HEIGHT) {
            if (!isGameOver && !isGameWon) {
                this.statManager.loseLife();
                if (this.statManager.isOutOfLives()) {
                    System.out.println("Game Over!");
                    isGameOver = true;
                } else {
                    ball.setStuck(true); // Reset bóng về thanh đỡ
                }
            }
        }

        // 3. Va chạm với Thanh đỡ (Paddle)
        if (ball.checkCollision(paddle)) {
            // Chỉ nảy lên nếu bóng đang đi xuống (tránh lỗi kẹt bóng)
            if (ball.getDirectionY() > 0) {
                ball.bounceOff(paddle);
                SoundManager.getInstance().playPaddleHit(); // Phát âm thanh
            }
        }

        // 4. Va chạm với Gạch (Bricks)
        for (Brick brick : bricks) {
            if (brick.isDestroyed()) {
                continue; // Bỏ qua gạch đã vỡ
            }

            if (ball.checkCollision(brick)) {
                ball.bounceOff(brick); // Bóng nảy ra

                // Gạch nhận sát thương và xử lý logic (trả về điểm)
                int points = brick.takeHit(this);
                this.statManager.addScore(points);

                // Phát âm thanh va chạm gạch (nếu có)
                if (brick.getType().equals("bomb_brick")) {
                    SoundManager.getInstance().playExplosion();
                } else {
                    // Chỉ phát tiếng nếu gạch bị ảnh hưởng (trừ gạch unbreakable)
                    if (points > 0 || !brick.getType().equals("unbreakable_brick")) {
                        SoundManager.getInstance().playBrickHit();
                    }
                }
                break; // Dừng vòng lặp (giả định bóng chỉ va chạm 1 gạch/khung hình)
            }
        }
    }

    /**
     * Vẽ (render) tất cả các đối tượng game lên Canvas.
     *
     * @param gc GraphicsContext để vẽ.
     */
    public void renderGame(GraphicsContext gc) {
        // Vẽ các thông số (điểm, mạng)
        this.statManager.render(gc, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Vẽ các đối tượng
        paddle.render(gc);
        ball.render(gc);
        for (Brick brick : bricks) {
            brick.render(gc); // (Gạch đã vỡ sẽ tự động không vẽ)
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.render(gc); // (Power-up đã kích hoạt sẽ tự động không vẽ)
        }
    }

    // --- Các hàm Getters để các lớp khác truy cập ---

    public Paddle getPaddle() {
        return paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public int getScore() {
        return this.statManager.getScore();
    }

    public StatManager getStatManager() {
        return statManager;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }
}