package org.example.baitaplon.stat;

import java.net.URL;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Quản lý các thông số của người chơi, bao gồm điểm số và mạng sống.
 * Lớp này cũng chịu trách nhiệm render các thông số này lên màn hình.
 */
public class StatManager {

    private int score;
    private int currentLives;
    private final int initialLives; // Số mạng ban đầu khi bắt đầu game/level

    private Image heartImage; // Ảnh trái tim để hiển thị mạng sống
    private static final int HEART_SIZE = 25;
    private static final int HEART_SPACING = 5;

    public StatManager(int initialLives) {
        this.initialLives = initialLives;
        reset(); // Reset điểm và mạng về ban đầu
        loadHeartImage();
    }

    /**
     * Tải hình ảnh trái tim từ thư mục /assets.
     */
    private void loadHeartImage() {
        URL imageUrl = getClass().getResource("/assets/heart.png");
        if (imageUrl != null) {
            heartImage = new Image(imageUrl.toExternalForm());
        } else {
            System.err.println("Lỗi: Không tìm thấy file heart.png tại /assets/heart.png");
        }
    }

    /**
     * Reset toàn bộ trạng thái (dùng khi bắt đầu game mới).
     * Đặt lại cả điểm và mạng sống.
     */
    public void reset() {
        score = 0;
        currentLives = initialLives;
    }

    /**
     * Chỉ reset số mạng về giá trị ban đầu (dùng khi qua level mới).
     * KHÔNG reset điểm.
     */
    public void resetLives() {
        this.currentLives = this.initialLives;
    }

    public void addScore(int points) {
        score += points;
    }

    public void loseLife() {
        if (currentLives > 0) {
            currentLives--;
        }
    }

    /**
     * Thêm một mạng (dùng cho power-up).
     */
    public void addLife() {
        currentLives++;
    }

    public boolean isOutOfLives() {
        return currentLives <= 0;
    }

    public int getScore() {
        return score;
    }

    public int getCurrentLives() {
        return currentLives;
    }

    /**
     * Vẽ điểm số và mạng sống lên Canvas.
     *
     * @param gc          GraphicsContext để vẽ.
     * @param screenWidth Chiều rộng màn hình (để căn lề phải).
     * @param screenHeight Không dùng trong code này, nhưng có thể hữu ích.
     */
    public void render(GraphicsContext gc, double screenWidth, double screenHeight) {
        // 1. Vẽ điểm số (góc trên bên trái)
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Score: " + score, 10, 25);

        // 2. Vẽ mạng sống (góc trên bên phải)
        if (heartImage != null) {
            // Vị trí bắt đầu vẽ trái tim (căn lề phải)
            double startX = screenWidth - (currentLives * (HEART_SIZE + HEART_SPACING));
            double y = 5; // Vị trí Y cố định

            for (int i = 0; i < currentLives; i++) {
                gc.drawImage(heartImage, startX + (i * (HEART_SIZE + HEART_SPACING)), y, HEART_SIZE,
                        HEART_SIZE);
            }
        } else {
            // Thay thế bằng chữ nếu không tải được ảnh trái tim
            gc.fillText("Lives: " + currentLives, screenWidth - 100, 25);
        }
    }
}