package org.example.baitaplon.ui;

/**
 * Interface (cầu nối) để MenuScreen giao tiếp ngược lại với MainApplication.
 * Định nghĩa các hành động mà MenuScreen có thể kích hoạt.
 */
public interface MenuListener {

    /**
     * Được gọi khi người chơi nhấn nút "Start".
     */
    void onStartGame();

    /**
     * Được gọi khi người chơi nhấn nút "Exit".
     */
    void onExitGame();

    /**
     * Được gọi khi người chơi nhấn nút "Sound On" hoặc "Sound Off".
     */
    void onToggleSound();
}