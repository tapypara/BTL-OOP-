package org.example.baitaplon.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Màn hình hiển thị khi game tạm dừng, sử dụng hình ảnh.
 * Là một StackPane được thêm vào gameRoot trong MainApplication.
 */
public class PauseScreen extends StackPane {

    private Image backgroundImage;
    private Image continueButtonImage;

    public PauseScreen(PauseListener listener) {
        setAlignment(Pos.CENTER);

        // 1. Tải hình ảnh
        try {
            String bgPath = "/assets/pause_screen.png";
            backgroundImage = new Image(getClass().getResource(bgPath).toExternalForm());
            String btnPath = "/assets/continue_button.png";
            continueButtonImage = new Image(getClass().getResource(btnPath).toExternalForm());
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("⚠ Lỗi khi load ảnh màn hình pause: " + e.getMessage());
            // Nếu lỗi, dùng nền đen mờ
            setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        }

        // 2. Thêm ảnh nền (nếu tải thành công)
        ImageView backgroundView = null;
        if (backgroundImage != null) {
            backgroundView = new ImageView(backgroundImage);
            getChildren().add(backgroundView);
        }

        // 3. Xóa các node cũ (nếu có, đề phòng)
        getChildren().removeIf(node -> node instanceof Label || node instanceof Button);

        // 4. Thêm nút Continue (hoặc nút thay thế)
        ImageView continueButtonView = null;
        if (continueButtonImage != null) {
            continueButtonView = new ImageView(continueButtonImage);

            final ImageView finalContinueButtonView = continueButtonView;

            // Thêm hiệu ứng hover
            finalContinueButtonView.setOnMouseEntered(e -> finalContinueButtonView.setOpacity(0.8));
            finalContinueButtonView.setOnMouseExited(e -> finalContinueButtonView.setOpacity(1.0));

            // Gán sự kiện click
            finalContinueButtonView.setOnMouseClicked(e -> listener.onResume());
            finalContinueButtonView.setTranslateY(100); // Đẩy nút xuống dưới tâm một chút
            getChildren().add(finalContinueButtonView);

        } else {
            // Nếu không tải được ảnh nút, dùng Button text thay thế
            System.err.println("⚠ Không load được ảnh nút Continue, dùng nút chữ thay thế.");
            Button resumeButtonFallback = new Button("Resume");
            resumeButtonFallback.setStyle(
                    "-fx-text-fill: white; -fx-background-color: darkred; -fx-border-color: white;");
            resumeButtonFallback.setOnAction(e -> listener.onResume());
            getChildren().add(resumeButtonFallback);
        }
    }
}