package org.example.baitaplon.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.example.baitaplon.game.GameManager;

/**
 * Lớp này quản lý toàn bộ giao diện và tương tác của menu chính.
 * Là một JavaFX Pane và giao tiếp với MainApplication qua MenuListener.
 */
public class MenuScreen extends Pane {

    private Image startScreenImg;
    private Image startButtonImg;
    private Image exitButtonImg;
    private Image soundOnImg;
    private Image soundOffImg;

    private ImageView soundOnButton;
    private ImageView soundOffButton;

    // Cầu nối để gửi sự kiện (nhấn nút) về MainApplication
    private MenuListener listener;

    public MenuScreen(MenuListener listener, boolean initialSoundState) {
        this.listener = listener;
        loadMenuImages();
        initUI(initialSoundState);
    }

    /**
     * Tải tất cả hình ảnh cần thiết cho menu.
     */
    private void loadMenuImages() {
        try {
            startScreenImg = new Image(
                    getClass().getResource("/assets/start_screen.png").toExternalForm());
            startButtonImg = new Image(
                    getClass().getResource("/assets/start_button.png").toExternalForm());
            exitButtonImg = new Image(
                    getClass().getResource("/assets/exit_button.png").toExternalForm());
            soundOnImg = new Image(
                    getClass().getResource("/assets/soundon_button.png").toExternalForm());
            soundOffImg = new Image(
                    getClass().getResource("/assets/soundoff_button.png").toExternalForm());
        } catch (Exception e) {
            System.err.println("⚠ Lỗi khi load ảnh menu: " + e.getMessage());
        }
    }

    /**
     * Thêm hiệu ứng mờ đi khi di chuột vào (hover) cho một nút ImageView.
     */
    private void addHoverEffect(ImageView btn) {
        btn.setOnMouseEntered(e -> btn.setOpacity(0.8));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
    }

    /**
     * Khởi tạo và sắp xếp các thành phần giao diện (nút bấm, nền).
     */
    private void initUI(boolean isSoundOn) {
        double WIDTH = GameManager.SCREEN_WIDTH;
        double HEIGHT = GameManager.SCREEN_HEIGHT;

        ImageView background = new ImageView(startScreenImg);
        background.setFitWidth(WIDTH);
        background.setFitHeight(HEIGHT);

        ImageView startBtn = new ImageView(startButtonImg);
        startBtn.setLayoutX(WIDTH / 2.0 - startButtonImg.getWidth() / 2); // Căn giữa
        startBtn.setLayoutY(200.0);

        // --- Nút Sound On/Off ---
        soundOnButton = new ImageView(soundOnImg);
        soundOffButton = new ImageView(soundOffImg);

        // Đặt vị trí cho cả hai nút Sound (sau Level, trước Exit)
        soundOnButton.setLayoutX(WIDTH / 2.0 - soundOnImg.getWidth() / 2);
        soundOnButton.setLayoutY(320.0);
        soundOffButton.setLayoutX(WIDTH / 2.0 - soundOffImg.getWidth() / 2);
        soundOffButton.setLayoutY(320);

        ImageView exitBtn = new ImageView(exitButtonImg);
        exitBtn.setLayoutX(WIDTH / 2.0 - exitButtonImg.getWidth() / 2); // Căn giữa
        exitBtn.setLayoutY(440.0); // Sau nút Sound

        // --- Áp dụng hiệu ứng Hover ---
        addHoverEffect(startBtn);
        addHoverEffect(exitBtn);
        addHoverEffect(soundOnButton);
        addHoverEffect(soundOffButton);

        // --- Gán sự kiện Click (gọi đến listener) ---
        startBtn.setOnMouseClicked(e -> listener.onStartGame());
        exitBtn.setOnMouseClicked(e -> listener.onExitGame());

        // Cả 2 nút sound đều gọi cùng một hàm toggle
        soundOnButton.setOnMouseClicked(e -> listener.onToggleSound());
        soundOffButton.setOnMouseClicked(e -> listener.onToggleSound());

        // Cập nhật hiển thị ban đầu của nút Sound
        updateSoundButtons(isSoundOn);

        // Thêm tất cả vào Pane
        getChildren().addAll(background, startBtn, exitBtn, soundOnButton, soundOffButton);
    }

    /**
     * Cập nhật hiển thị (ẩn/hiện) của nút Sound On và Sound Off.
     *
     * @param isSoundOn Trạng thái âm thanh hiện tại.
     */
    public void updateSoundButtons(boolean isSoundOn) {
        if (soundOnButton != null && soundOffButton != null) {
            soundOnButton.setVisible(isSoundOn);
            soundOffButton.setVisible(!isSoundOn);
        }
    }
}