package org.example.baitaplon.stat;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.baitaplon.game.GameManager;

/**
 * Lớp này quản lý tất cả các chỉ số của game (Điểm, Mạng).
 * Nó gộp logic của ScoreManager và LivesManagement.
 */
public class StatManager {

    // --- Biến quản lý điểm ---
    private int score;
    private double scoreX = 10;  // Tọa độ X để vẽ điểm
    private double scoreY = 35;  // Tọa độ Y để vẽ điểm (trên hàng gạch Y=50)

    // --- Biến quản lý mạng sống ---
    private int initialLives;
    private int currentLives;
    private double livesX = GameManager.SCREEN_WIDTH - 100; // Tọa độ X (góc phải)
    private double livesY = 35; // Tọa độ Y

    /**
     * Khởi tạo trình quản lý chỉ số.
     * @param initialLives Số mạng bắt đầu.
     */
    public StatManager(int initialLives) {
        this.initialLives = initialLives;
        reset(); // Cài đặt giá trị ban đầu
    }

    /**
     * Vẽ tất cả chỉ số (Điểm và Mạng) lên Canvas.
     */
    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        // 1. Vẽ Score
        gc.fillText("SCORE: " + this.score, this.scoreX, this.scoreY);

        // 2. Vẽ Lives
        gc.fillText("LIVES: " + this.currentLives, this.livesX, this.livesY);
    }

    /**
     * Đặt lại cả điểm và mạng sống về ban đầu (khi bắt đầu game mới).
     */
    public void reset() {
        this.score = 0;
        this.currentLives = this.initialLives;
    }

    // --- Các hàm xử lý ---

    public void addScore(int points) {
        this.score += points;
    }

    public int getScore() {
        return this.score;
    }

    public void loseLife() {
        if (this.currentLives > 0) {
            this.currentLives--;
        }
    }

    public boolean isOutOfLives() {
        return this.currentLives <= 0;
    }

    public int getLives() {
        return this.currentLives;
    }
}