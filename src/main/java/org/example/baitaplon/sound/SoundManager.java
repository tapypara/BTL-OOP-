package org.example.baitaplon.sound;

import javafx.scene.media.AudioClip;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp này quản lý tất cả các hiệu ứng âm thanh (SFX).
 * Nó tải trước các âm thanh và phát khi được yêu cầu.
 */
public class SoundManager {

    // Một bản đồ (Map) để lưu trữ các âm thanh đã được tải
    private Map<String, AudioClip> soundMap;
    private boolean soundEnabled = true; // Mặc định là bật

    public SoundManager() {
        soundMap = new HashMap<>();
        loadSounds();
    }

    /**
     * Tải tất cả các hiệu ứng âm thanh cần thiết vào bộ nhớ.
     */
    private void loadSounds() {
        // Bạn có thể thêm nhiều âm thanh khác vào đây
        loadSound("paddle_hit", "/assets/paddle_hit.wav");
        loadSound("brick_hit", "/assets/brick_hit.wav");
    }

    /**
     * Hàm tiện ích để tải một file âm thanh và lưu vào Map.
     * @param name Tên định danh (key) để gọi âm thanh.
     * @param filePath Đường dẫn trong thư mục /resources.
     */
    private void loadSound(String name, String filePath) {
        try {
            AudioClip clip = new AudioClip(getClass().getResource(filePath).toExternalForm());
            soundMap.put(name, clip);
        } catch (Exception e) {
            System.err.println("⚠ Lỗi khi tải file âm thanh (SFX) '" + name + "': " + e.getMessage());
        }
    }

    /**
     * Phát một âm thanh dựa theo tên của nó.
     * Sẽ không phát gì nếu âm thanh đang tắt (soundEnabled = false)
     * hoặc nếu tên âm thanh không tồn tại.
     * * @param name Tên của âm thanh (ví dụ: "paddle_hit").
     */
    public void play(String name) {
        // Chỉ phát nếu âm thanh đang bật
        if (!soundEnabled) {
            return;
        }

        AudioClip clip = soundMap.get(name);
        if (clip != null) {
            clip.play();
        } else {
            System.err.println("⚠ Cảnh báo: Cố gắng phát âm thanh không tồn tại: " + name);
        }
    }

    /**
     * Bật hoặc tắt tất cả các hiệu ứng âm thanh (SFX).
     * @param enabled true để bật, false để tắt.
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }
}