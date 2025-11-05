// <<< FILE MỚI - ĐỊNH NGHĨA PACKAGE >>>
package org.example.baitaplon.ui;

/**
 * Interface "callback" để PauseScreen giao tiếp với MainApplication.
 * Nó báo cho MainApplication biết khi nào người dùng nhấn nút "Resume".
 */
public interface PauseListener {
    void onResume();
}