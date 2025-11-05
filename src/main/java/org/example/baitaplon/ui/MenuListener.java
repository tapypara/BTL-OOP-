package org.example.baitaplon.ui;

/**
 * Interface "callback" để MenuScreen giao tiếp với MainApplication.
 * Định nghĩa các hành động mà MenuScreen có thể kích hoạt.
 */
public interface MenuListener {
    void onStartGame();      // Khi nhấn nút "Start"
    void onExitGame();       // Khi nhấn nút "Exit"
    void onToggleSound();    // Khi nhấn nút "Sound On" hoặc "Sound Off"
}