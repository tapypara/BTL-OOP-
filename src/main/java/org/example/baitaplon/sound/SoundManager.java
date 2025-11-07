package org.example.baitaplon.sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Lớp quản lý toàn bộ âm thanh (Nhạc nền và SFX) của game.
 * Sử dụng mẫu thiết kế Singleton để đảm bảo chỉ có một thể hiện duy nhất.
 */
public class SoundManager {

    // --- Singleton Instance ---
    private static SoundManager instance;

    // --- Audio Assets ---
    private MediaPlayer backgroundMusicPlayer;
    private AudioClip paddleHitSound;
    private AudioClip brickHitSound;
    private AudioClip explosionSound;

    // --- State ---
    private boolean isMusicOn = true; // Biến này chỉ kiểm soát nhạc nền

    /**
     * Constructor private để ngăn việc tạo instance từ bên ngoài.
     */
    private SoundManager() {
        loadMusic();
        loadSounds();
    }

    /**
     * Phương thức public, tĩnh để lấy thể hiện (instance) duy nhất của SoundManager.
     * Đây là cách duy nhất để truy cập lớp này.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    // --- Tải Âm Thanh (Internal) ---

    /**
     * Tải file nhạc nền (thường là file .mp3 dài).
     */
    private void loadMusic() {
        try {
            String musicFile = "/assets/background_music.mp3";
            Media backgroundMusic = new Media(getClass().getResource(musicFile).toExternalForm());
            backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp lại vô hạn
        } catch (Exception e) {
            System.err.println("⚠ Lỗi khi tải file nhạc: " + e.getMessage());
        }
    }

    /**
     * Tải các file hiệu ứng âm thanh (SFX - thường là file .wav ngắn).
     */
    private void loadSounds() {
        try {
            String paddleSoundPath = "/assets/paddle_hit.wav";
            String brickSoundPath = "/assets/brick_hit.wav";
            String explosionSoundPath = "/assets/explosion.wav";

            paddleHitSound = new AudioClip(getClass().getResource(paddleSoundPath).toExternalForm());
            brickHitSound = new AudioClip(getClass().getResource(brickSoundPath).toExternalForm());
            explosionSound = new AudioClip(getClass().getResource(explosionSoundPath).toExternalForm());
        } catch (Exception e) {
            System.err.println("⚠ Lỗi khi tải file âm thanh (SFX): " + e.getMessage());
        }
    }

    // --- Điều Khiển (Public) ---

    /**
     * Phát nhạc nền nếu isMusicOn là true.
     */
    public void playMusic() {
        if (isMusicOn && backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    /**
     * Tạm dừng nhạc nền (luôn thực hiện, bất kể isMusicOn).
     */
    public void pauseMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.pause();
        }
    }

    /**
     * Phát hiệu ứng âm thanh khi bóng chạm thanh đỡ.
     * (Luôn phát, không bị ảnh hưởng bởi isMusicOn).
     */
    public void playPaddleHit() {
        if (paddleHitSound != null) {
            paddleHitSound.play();
        }
    }

    /**
     * Phát hiệu ứng âm thanh khi bóng chạm gạch.
     * (Luôn phát, không bị ảnh hưởng bởi isMusicOn).
     */
    public void playBrickHit() {
        if (brickHitSound != null) {
            brickHitSound.play();
        }
    }

    /**
     * Phát hiệu ứng âm thanh khi gạch bom nổ.
     * (Luôn phát, không bị ảnh hưởng bởi isMusicOn).
     */
    public void playExplosion() {
        if (explosionSound != null) {
            explosionSound.play();
        }
    }

    /**
     * Bật/Tắt CHỈ nhạc nền.
     */
    public void toggleSound() {
        isMusicOn = !isMusicOn;
        if (isMusicOn) {
            playMusic();
        } else {
            pauseMusic();
        }
    }

    /**
     * Trả về trạng thái của nhạc nền (đang bật hay tắt).
     */
    public boolean isMusicOn() {
        return isMusicOn;
    }
}