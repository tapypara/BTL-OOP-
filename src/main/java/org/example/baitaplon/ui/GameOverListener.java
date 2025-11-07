package org.example.baitaplon.ui;

/**
 * Interface (cầu nối) để GameOverScreen giao tiếp ngược lại với MainApplication.
 * Định nghĩa hành động khi người dùng muốn chơi lại.
 */
public interface GameOverListener {

    /**
     * Được gọi khi người chơi nhấn nút "Play Again".
     */
    void onPlayAgain();
}