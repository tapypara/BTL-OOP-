package org.example.baitaplon.stat;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;

public class StatManager {
    private int score;
    private int currentLives;
    private final int initialLives;

    private Image heartImage; // Thêm biến cho ảnh trái tim
    private static final int HEART_SIZE = 25; // Kích thước trái tim
    private static final int HEART_SPACING = 5; // Khoảng cách giữa các trái tim

    public StatManager(int initialLives) {
        this.initialLives = initialLives;
        reset();
        loadHeartImage(); // Tải ảnh trái tim khi khởi tạo
    }

    private void loadHeartImage() {
        URL imageUrl = getClass().getResource("/assets/heart.png");
        if (imageUrl != null) {
            heartImage = new Image(imageUrl.toExternalForm());
        } else {
            System.err.println("Error: heart.png not found at /assets/heart.png");
            // Fallback: nếu không tìm thấy ảnh, sẽ không vẽ tim
        }
    }

    public void reset() {
        score = 0;
        currentLives = initialLives;
    }

    public void addScore(int points) {
        score += points;
    }

    public void loseLife() {
        if (currentLives > 0) {
            currentLives--;
        }
    }

    // New method to add a life (optional, but good for power-ups)
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

    public void render(GraphicsContext gc, double screenWidth, double screenHeight) {
        // Render Score
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Score: " + score, 10, 25); // Top-left

        // Render Lives as Heart Images
        if (heartImage != null) {
            // Vị trí bắt đầu vẽ trái tim (ví dụ: góc trên bên phải)
            double startX = screenWidth - (currentLives * (HEART_SIZE + HEART_SPACING));
            double y = 5; // Vị trí Y cố định

            for (int i = 0; i < currentLives; i++) {
                gc.drawImage(heartImage, startX + (i * (HEART_SIZE + HEART_SPACING)), y, HEART_SIZE, HEART_SIZE);
            }
        } else {
            // Fallback nếu không tải được ảnh trái tim
            gc.fillText("Lives: " + currentLives, screenWidth - 100, 25); // Top-right
        }
    }
}