// <<< SỬA DÒNG PACKAGE >>>
package org.example.baitaplon.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

// <<< THÊM IMPORT CHO INTERFACE MỚI >>>
import org.example.baitaplon.ui.PauseListener;

/**
 * Màn hình hiển thị khi game tạm dừng, sử dụng hình ảnh.
 */
public class PauseScreen extends StackPane {

    // <<< XÓA INTERFACE Ở ĐÂY >>>
    // public interface PauseListener { ... } // (Đã bị xóa và chuyển ra file riêng)


    private Image backgroundImage;
    private Image continueButtonImage;

    // <<< Tham số (PauseListener listener) giờ sẽ import từ file riêng >>>
    public PauseScreen(PauseListener listener) {
        // ... (Toàn bộ code bên trong hàm khởi tạo giữ nguyên) ...
        setAlignment(Pos.CENTER);

        try {
            String bgPath = "/assets/pause_screen.png";
            backgroundImage = new Image(getClass().getResource(bgPath).toExternalForm());
            String btnPath = "/assets/continue_button.png";
            continueButtonImage = new Image(getClass().getResource(btnPath).toExternalForm());
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("⚠ Lỗi khi load ảnh màn hình pause: " + e.getMessage());
            setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        }

        ImageView backgroundView = null;
        if (backgroundImage != null) {
            backgroundView = new ImageView(backgroundImage);
            getChildren().add(backgroundView);
        } else {
            if (getStyle() == null || getStyle().isEmpty()) {
                setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
            }
        }

        getChildren().removeIf(node -> node instanceof Label || node instanceof Button);

        ImageView continueButtonView = null;
        if (continueButtonImage != null) {
            continueButtonView = new ImageView(continueButtonImage);

            final ImageView finalContinueButtonView = continueButtonView;

            // <<< THAY ĐỔI: Đồng bộ hiệu ứng hover giống MenuScreen >>>
            finalContinueButtonView.setOnMouseEntered(e -> finalContinueButtonView.setOpacity(0.8));
            finalContinueButtonView.setOnMouseExited(e -> finalContinueButtonView.setOpacity(1.0));

            finalContinueButtonView.setOnMouseClicked(e -> listener.onResume());
            finalContinueButtonView.setTranslateY(100);
            getChildren().add(finalContinueButtonView);

        } else {
            System.err.println("⚠ Không load được ảnh nút Continue, dùng nút chữ thay thế.");
            Button resumeButtonFallback = new Button("Resume");
            resumeButtonFallback.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
            resumeButtonFallback.setStyle("-fx-text-fill: white; -fx-background-color: darkred; -fx-border-color: white;");
            resumeButtonFallback.setOnAction(e -> listener.onResume());
            getChildren().add(resumeButtonFallback);
        }
    }
}