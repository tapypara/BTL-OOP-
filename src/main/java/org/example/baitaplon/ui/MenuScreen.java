package org.example.baitaplon.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.example.baitaplon.game.GameManager;

/**
 * Lớp này thay thế hoàn toàn cho initStartMenu() trong MainApplication.
 * Nó quản lý toàn bộ giao diện và tương tác của menu chính.
 */
public class MenuScreen extends Pane {

    private Image startScreenImg;
    private Image startButtonImg;
    private Image levelButtonImg;
    private Image exitButtonImg;

    private Image soundOnImg;
    private Image soundOffImg;
    private ImageView soundOnButton;
    private ImageView soundOffButton;

    private MenuListener listener;

    public MenuScreen(MenuListener listener, boolean initialSoundState) {
        this.listener = listener;
        loadMenuImages();
        initUI(initialSoundState);
    }

    private void loadMenuImages() {
        try {
            startScreenImg = new Image(getClass().getResource("/assets/start_screen.png").toExternalForm());
            startButtonImg = new Image(getClass().getResource("/assets/start_button.png").toExternalForm());
            exitButtonImg = new Image(getClass().getResource("/assets/exit_button.png").toExternalForm());

            soundOnImg = new Image(getClass().getResource("/assets/soundon_button.png").toExternalForm());
            soundOffImg = new Image(getClass().getResource("/assets/soundoff_button.png").toExternalForm());

        } catch (Exception e) {
            System.err.println("⚠ Lỗi khi load ảnh menu: " + e.getMessage());
        }
    }

    private void addHoverEffect(ImageView btn) {
        btn.setOnMouseEntered(e -> btn.setOpacity(0.8));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
    }

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

        // --- THÊM HIỆU ỨNG HOVER CHO NÚT SOUND ---
        addHoverEffect(soundOnButton);
        addHoverEffect(soundOffButton);

        ImageView exitBtn = new ImageView(exitButtonImg);
        exitBtn.setLayoutX(WIDTH / 2.0 - exitButtonImg.getWidth() / 2); // Căn giữa
        exitBtn.setLayoutY(440.0); // Sau nút Sound

        // --- ÁP DỤNG HIỆU ỨNG HOVER CHO CÁC NÚT KHÁC (GIỮ NGUYÊN) ---
        addHoverEffect(startBtn);
        addHoverEffect(exitBtn);

        // --- GÁN SỰ KIỆN CLICK (GIỮ NGUYÊN) ---
        startBtn.setOnMouseClicked(e -> listener.onStartGame());
        exitBtn.setOnMouseClicked(e -> listener.onExitGame());

        // Gán sự kiện cho cả 2 nút Sound (cùng gọi một hàm listener)
        soundOnButton.setOnMouseClicked(e -> listener.onToggleSound());
        soundOffButton.setOnMouseClicked(e -> listener.onToggleSound());

        // Cập nhật hiển thị ban đầu của nút Sound
        updateSoundButtons(isSoundOn);

        // Thêm tất cả vào Pane
        getChildren().addAll(background, startBtn, exitBtn, soundOnButton, soundOffButton);
    }

    public void updateSoundButtons(boolean isSoundOn) {
        if (soundOnButton != null && soundOffButton != null) {
            soundOnButton.setVisible(isSoundOn);
            soundOffButton.setVisible(!isSoundOn);
        }
    }
}