package org.example.baitaplon;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

// --- THÊM CÁC IMPORT CHO CÁC PACKAGE MỚI ---
import org.example.baitaplon.game.GameManager;
import org.example.baitaplon.model.Ball;
import org.example.baitaplon.model.Paddle;
import org.example.baitaplon.ui.MenuListener;
import org.example.baitaplon.ui.MenuScreen;
import org.example.baitaplon.ui.PauseListener;
import org.example.baitaplon.ui.PauseScreen;
// <<< THAY ĐỔI 1: THÊM IMPORT MỚI >>>
import org.example.baitaplon.ui.GameOverListener;
import org.example.baitaplon.ui.GameOverScreen;

/**
 * Lớp Application chính, đóng vai trò là "Controller".
 * Implement cả 3 Listener.
 */
// <<< THAY ĐỔI 2: IMPLEMENT THÊM GameOverListener >>>
public class MainApplication extends Application implements PauseListener, MenuListener, GameOverListener {

    // --- Hằng số ---
    private static final int WIDTH = GameManager.SCREEN_WIDTH;
    private static final int HEIGHT = GameManager.SCREEN_HEIGHT;

    // --- Quản lý cửa sổ & Scene ---
    private Stage stage;
    private Scene menuScene;
    private Scene gameScene;
    private StackPane gameRoot;

    // --- Các thành phần UI (được tách riêng) ---
    private MenuScreen menuScreen;
    private PauseScreen pauseScreen;
    private GameOverScreen gameOverScreen; // <<< THÊM BIẾN MỚI

    // --- Logic Game ---
    private GameManager gameManager;
    private AnimationTimer gameLoop;
    private GraphicsContext gc;
    private final Set<KeyCode> activeKeys = new HashSet<>();

    // --- Trạng thái Game ---
    private boolean gameOver = false;
    // <<< ĐÃ XÓA BIẾN gameOverImage (không cần nữa) >>>

    // --- Logic Âm thanh (ĐÃ BỎ PHẦN NHẠC) ---
    private boolean isSoundOn = true;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Arkanoid Game");
        stage.setResizable(false);

        // (Không tải nhạc)

        // --- Logic khởi tạo Menu ---
        menuScreen = new MenuScreen(this, isSoundOn);
        menuScene = new Scene(menuScreen, WIDTH, HEIGHT);

        // Hiển thị MenuScene ban đầu
        stage.setScene(menuScene);
        stage.show();
    }

    /**
     * Bắt đầu màn chơi (được gọi bởi MenuScreen).
     */
    private void startGame() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        gameRoot = new StackPane(canvas);
        gameScene = new Scene(gameRoot, WIDTH, HEIGHT);

        setupKeyListeners();

        stage.setScene(gameScene);
        Platform.runLater(canvas::requestFocus);

        gameManager = new GameManager();
        gameOver = false; // Reset trạng thái game over

        // <<< ĐÃ XÓA LOGIC TẢI gameOverImage TẠI ĐÂY >>>

        startGameLoop();
    }

    private void setupKeyListeners() {
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePause();
            } else {
                activeKeys.add(event.getCode());
            }
        });
        gameScene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));
    }

    /**
     * Khởi tạo và bắt đầu vòng lặp game chính (AnimationTimer).
     */
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Nếu game đã over, không làm gì cả (chờ người dùng click)
                if (gameOver) return;

                // Xử lý input
                handleGameInput();

                // Cập nhật logic game
                if (gameManager != null) {
                    gameManager.updateGame();
                }

                // Vẽ game
                renderGame();

                // <<< THAY ĐỔI 3: KIỂM TRA GAME OVER SAU KHI VẼ XONG >>>
                if (gameManager != null && gameManager.isGameOver()) {
                    gameOver = true; // Đặt cờ
                    showGameOverScreen(); // Hiển thị màn hình Game Over
                }
            }
        };
        gameLoop.start();
    }

    /**
     * Xử lý input của người chơi trong vòng lặp game.
     */
    private void handleGameInput() {
        // ... (Code này giữ nguyên) ...
        if (gameManager == null) return;
        Paddle paddle = gameManager.getPaddle();
        Ball ball = gameManager.getBall();
        if (paddle == null || ball == null) return;
        if (activeKeys.contains(KeyCode.LEFT)) paddle.moveLeft();
        else if (activeKeys.contains(KeyCode.RIGHT)) paddle.moveRight();
        else paddle.stop();
        if (ball.isStuck() && activeKeys.contains(KeyCode.SPACE)) {
            ball.setDirectionY(-Math.abs(ball.getDirectionY()));
            ball.setStuck(false);
        }
    }

    /**
     * Vẽ game lên Canvas.
     */
    private void renderGame() {
        // <<< ĐÃ XÓA KHỐI `if (gameOver)` Ở ĐẦU HÀM NÀY >>>

        // 1. Xóa màn hình (vẽ nền)
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // 2. Yêu cầu GameManager vẽ các đối tượng
        if (gameManager != null) {
            gameManager.renderGame(gc);
        }
    }

    // --- LOGIC PAUSE (Không thay đổi) ---
    private void togglePause() {
        if (gameOver || gameManager == null) return;
        if (gameManager.isPaused()) resumeGame();
        else pauseGame();
    }
    private void pauseGame() {
        gameManager.pause();
        gameLoop.stop();
        if (pauseScreen == null) {
            pauseScreen = new PauseScreen(this);
        }
        gameRoot.getChildren().add(pauseScreen);
    }
    private void resumeGame() {
        if (gameManager.isPaused()) {
            gameManager.resume();
            gameRoot.getChildren().remove(pauseScreen);
            gameLoop.start();
        }
    }

    // --- THÊM HÀM MỚI: HIỂN THỊ GAME OVER ---
    /**
     * Dừng game và hiển thị màn hình Game Over.
     */
    private void showGameOverScreen() {
        gameLoop.stop(); // Dừng vòng lặp game
        if (gameOverScreen == null) {
            gameOverScreen = new GameOverScreen(this); // 'this' là GameOverListener
        }
        gameRoot.getChildren().add(gameOverScreen); // Thêm màn hình lên trên
    }

    // --- IMPLEMENTS CÁC HÀM LISTENER ---

    @Override
    public void onResume() { // (Từ PauseListener)
        resumeGame();
    }

    @Override
    public void onStartGame() { // (Từ MenuListener)
        startGame();
    }

    @Override
    public void onExitGame() { // (Từ MenuListener)
        stage.close();
    }

    @Override
    public void onToggleSound() { // (Từ MenuListener)
        setSoundState(!isSoundOn);
    }

    // <<< THAY ĐỔI 4: IMPLEMENT HÀM MỚI TỪ GameOverListener >>>
    @Override
    public void onPlayAgain() {
        // 1. Dọn dẹp màn hình game over
        if (gameOverScreen != null) {
            gameRoot.getChildren().remove(gameOverScreen);
        }

        // 2. Reset lại toàn bộ trạng thái game
        gameManager = null;
        gameLoop = null; // Đã stop, nhưng đặt là null để GC dọn
        gameRoot = null;
        gameScene = null;
        gameOverScreen = null; // Tạo mới lần sau
        pauseScreen = null; // Tạo mới lần sau
        gameOver = false; // QUAN TRỌNG: Reset cờ

        // 3. Hiển thị lại màn hình Menu
        stage.setScene(menuScene);
    }

    /**
     * Cập nhật trạng thái âm thanh (CHỈ CẬP NHẬT UI).
     */
    private void setSoundState(boolean on) {
        isSoundOn = on;
        if (menuScreen != null) {
            menuScreen.updateSoundButtons(isSoundOn);
        }
    }

    // --- HÀM MAIN (Không thay đổi) ---
    public static void main(String[] args) {
        launch(args);
    }
}
