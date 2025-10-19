package org.example.baitaplon;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class MainApplication extends Application {

    // Sử dụng hằng số từ GameManager để thống nhất kích thước
    private static final int WIDTH = GameManager.SCREEN_WIDTH;
    private static final int HEIGHT = GameManager.SCREEN_HEIGHT;

    private GameManager gameManager;
    private GraphicsContext gc;

    // Set để lưu trữ các phím đang được nhấn, giúp di chuyển mượt hơn
    private final Set<KeyCode> activeKeys = new HashSet<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Arkanoid Game");
        primaryStage.setResizable(false);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // --- Xử lý sự kiện nhấn và nhả phím ---
        scene.setOnKeyPressed(event -> activeKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));

        primaryStage.setScene(scene);
        primaryStage.show();

        // --- Khởi tạo GameManager và bắt đầu game ---
        gameManager = new GameManager();
        startGameLoop();
    }

    private void startGameLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // 1. Xử lý input từ người chơi
                handleInput();

                // 2. Cập nhật trạng thái tất cả các đối tượng game (thông qua GameManager)
                gameManager.updateGame();

                // 3. Xóa màn hình và vẽ lại tất cả mọi thứ (thông qua GameManager)
                renderGame();
            }
        }.start();
    }

    private void handleInput() {
        // Lấy paddle từ GameManager để điều khiển
        Paddle paddle = gameManager.getPaddle();
        if (paddle == null) return; // Đảm bảo paddle đã được tạo

        if (activeKeys.contains(KeyCode.LEFT) || activeKeys.contains(KeyCode.A)) {
            paddle.moveLeft();
        } else if (activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.D)) {
            paddle.moveRight();
        } else {
            // Nếu không có phím di chuyển nào được nhấn thì dừng paddle
            paddle.stop();
        }
    }

    private void renderGame() {
        // Xóa toàn bộ Canvas với màu nền đen
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // **ĐIỂM SỬA QUAN TRỌNG NHẤT:**
        // Ủy thác việc vẽ cho GameManager, GameManager sẽ gọi render() của từng đối tượng
        gameManager.renderGame(gc);
    }
}
