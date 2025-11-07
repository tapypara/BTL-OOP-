package org.example.baitaplon.ui;

/**
 * Interface (cầu nối) để PauseScreen giao tiếp ngược lại với MainApplication.
 * Báo cho MainApplication biết khi nào người dùng nhấn nút "Resume".
 */
public interface PauseListener {

    /**
     * Được gọi khi người chơi nhấn nút "Resume".
     */
    void onResume();
}