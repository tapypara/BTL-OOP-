package org.example.baitaplon.ui;

/**
 * Interface "callback" để GameOverScreen giao tiếp với MainApplication.
 * Định nghĩa hành động khi người dùng muốn chơi lại.
 */
public interface GameOverListener {
    void onPlayAgain(); // Sẽ được gọi khi nhấn nút "Play Again"
}