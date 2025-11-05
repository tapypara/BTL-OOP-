package org.example.baitaplon.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button; // Dùng làm fallback
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.baitaplon.game.GameManager; // Để lấy kích thước màn hình

/**
 * Màn hình hiển thị khi game kết thúc.
 * Sử dụng StackPane để xếp nút Play Again lên trên ảnh nền.
 */
public class GameOverScreen extends StackPane {

    private Image backgroundImage;
    private Image playAgainButtonImage;
    private GameOverListener listener;

    public GameOverScreen(GameOverListener listener) {
        this.listener = listener;
        setAlignment(Pos.CENTER);

        // --- 1. Load hình ảnh ---
        try {
            // Tải ảnh nền game over
            String bgPath = "/assets/gameover_screen.png";
            backgroundImage = new Image(getClass().getResource(bgPath).toExternalForm());

            // Tải ảnh nút play again
            String btnPath = "/assets/playagain_button.png"; // <<< Đảm bảo bạn có file ảnh này
            playAgainButtonImage = new Image(getClass().getResource(btnPath).toExternalForm());

        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("⚠ Lỗi khi load ảnh màn hình game over: " + e.getMessage());
        }

        // --- 2. Tạo ảnh nền ---
        if (backgroundImage != null) {
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(GameManager.SCREEN_WIDTH);
            backgroundView.setFitHeight(GameManager.SCREEN_HEIGHT);
            getChildren().add(backgroundView); // Thêm nền vào trước
        } else {
            // Fallback nếu không load được ảnh nền
            setStyle("-fx-background-color: rgba(0, 0, 0, 0.9);");
        }

        // --- 3. Tạo nút Play Again ---
        if (playAgainButtonImage != null) {
            ImageView playAgainButtonView = new ImageView(playAgainButtonImage);

            // Thêm hiệu ứng hover (y hệt PauseScreen)
            // <<< THAY ĐỔI: Thêm hiệu ứng hover (giống hệt MenuScreen) >>>
            playAgainButtonView.setOnMouseEntered(e -> playAgainButtonView.setOpacity(0.8));
            playAgainButtonView.setOnMouseExited(e -> playAgainButtonView.setOpacity(1.0));

            // Gán hành động khi click: Gọi listener!
            playAgainButtonView.setOnMouseClicked(e -> listener.onPlayAgain());

            // (Tùy chỉnh) Đặt vị trí nút, ví dụ: 150px bên dưới tâm
            playAgainButtonView.setTranslateY(150);

            getChildren().add(playAgainButtonView); // Thêm nút lên trên nền

        } else {
            // Fallback nếu không load được ảnh nút:
            System.err.println("⚠ Không load được ảnh nút Play Again, dùng nút chữ thay thế.");
            Button fallbackButton = new Button("Play Again");
            fallbackButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
            fallbackButton.setStyle("-fx-text-fill: white; -fx-background-color: darkgreen; -fx-border-color: white;");
            fallbackButton.setOnAction(e -> listener.onPlayAgain());
            getChildren().add(fallbackButton);
        }
    }
}