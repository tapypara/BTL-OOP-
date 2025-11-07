package org.example.baitaplon;

import java.util.HashSet;
import java.util.Set;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.baitaplon.game.GameManager;
import org.example.baitaplon.model.Ball;
import org.example.baitaplon.model.Paddle;
import org.example.baitaplon.sound.SoundManager;
import org.example.baitaplon.ui.GameOverListener;
import org.example.baitaplon.ui.GameOverScreen;
import org.example.baitaplon.ui.MenuListener;
import org.example.baitaplon.ui.MenuScreen;
import org.example.baitaplon.ui.PauseListener;
import org.example.baitaplon.ui.PauseScreen;

/**
 * Lớp Application chính (điểm vào của JavaFX), đóng vai trò là "Controller" trung tâm.
 * Lớp này quản lý các màn hình (Scene), vòng lặp game chính (AnimationTimer)
 * và xử lý giao tiếp giữa các thành phần UI (View) và logic game (Model/GameManager).
 *
 * Lớp này implement 3 Listener để nhận "callback" từ các màn hình UI.
 */
public class MainApplication extends Application implements PauseListener, MenuListener,
        GameOverListener {

    // --- Hằng số (Lấy từ GameManager) ---
    private static final int WIDTH = GameManager.SCREEN_WIDTH;
    private static final int HEIGHT = GameManager.SCREEN_HEIGHT;

    // --- Quản lý cửa sổ & Scene ---
    private Stage stage;        // Cửa sổ chính của ứng dụng
    private Scene menuScene;    // Màn hình Menu
    private Scene gameScene;    // Màn hình chơi game
    private StackPane gameRoot; // Pane gốc của gameScene (chứa Canvas và các UI đè lên)

    // --- Các thành phần UI (View) ---
    private MenuScreen menuScreen;
    private PauseScreen pauseScreen;
    private GameOverScreen gameOverScreen;

    // --- Logic Game ---
    private GameManager gameManager;    // Trái tim logic của game
    private AnimationTimer gameLoop;  // Vòng lặp chính (60 FPS)
    private GraphicsContext gc;         // Bút vẽ cho Canvas
    private final Set<KeyCode> activeKeys = new HashSet<>(); // Giữ phím đang nhấn

    // --- Trạng thái Game ---
    private boolean gameOver = false;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("Arkanoid Game");
        stage.setResizable(false);

        // --- Khởi tạo Menu ---
        // Lấy trạng thái âm thanh ban đầu từ SoundManager
        menuScreen = new MenuScreen(this, SoundManager.getInstance().isMusicOn());
        menuScene = new Scene(menuScreen, WIDTH, HEIGHT);

        // Hiển thị MenuScene ban đầu
        stage.setScene(menuScene);
        stage.show();

        // Yêu cầu SoundManager phát nhạc (nếu đang bật)
        SoundManager.getInstance().playMusic();
    }

    /**
     * Bắt đầu màn chơi (được gọi bởi MenuScreen thông qua onStartGame()).
     */
    private void startGame() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        // Dùng StackPane để có thể xếp chồng PauseScreen/GameOverScreen lên Canvas
        gameRoot = new StackPane(canvas);
        gameScene = new Scene(gameRoot, WIDTH, HEIGHT);

        setupKeyListeners(); // Gán sự kiện phím cho gameScene

        stage.setScene(gameScene);
        Platform.runLater(canvas::requestFocus); // Đảm bảo Canvas nhận được input phím

        gameManager = new GameManager(); // Tạo một phiên game mới
        gameOver = false; // Reset trạng thái game over

        startGameLoop(); // Bắt đầu vòng lặp game
    }

    /**
     * Cài đặt lắng nghe sự kiện nhấn/thả phím cho màn hình chơi game.
     */
    private void setupKeyListeners() {
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePause(); // Xử lý Pause riêng
            } else {
                activeKeys.add(event.getCode()); // Thêm phím vào bộ đệm
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
                if (gameOver) {
                    return;
                }

                // 1. Xử lý Input
                handleGameInput();

                // 2. Cập nhật Logic
                if (gameManager != null) {
                    gameManager.updateGame();
                }

                // 3. Vẽ (Render)
                renderGame();

                // 4. Kiểm tra trạng thái Game Over
                if (gameManager != null && gameManager.isGameOver()) {
                    gameOver = true;
                    showGameOverScreen();
                }
            }
        };
        gameLoop.start();
    }

    /**
     * Xử lý input của người chơi (được gọi trong vòng lặp game).
     */
    private void handleGameInput() {
        if (gameManager == null) {
            return;
        }
        Paddle paddle = gameManager.getPaddle();
        Ball ball = gameManager.getBall();
        if (paddle == null || ball == null) {
            return;
        }

        // Di chuyển thanh đỡ
        if (activeKeys.contains(KeyCode.LEFT)) {
            paddle.moveLeft();
        } else if (activeKeys.contains(KeyCode.RIGHT)) {
            paddle.moveRight();
        } else {
            paddle.stop();
        }

        // Bắn bóng
        if (ball.isStuck() && activeKeys.contains(KeyCode.SPACE)) {
            ball.setDirectionY(-Math.abs(ball.getDirectionY()));
            ball.setStuck(false);
        }
    }

    /**
     * Render game lên Canvas (được gọi trong vòng lặp game).
     */
    private void renderGame() {
        // Xóa màn hình (vẽ nền)
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Yêu cầu GameManager vẽ các đối tượng
        if (gameManager != null) {
            gameManager.renderGame(gc);
        }
    }

    /**
     * Bật/Tắt trạng thái Tạm dừng (Pause).
     */
    private void togglePause() {
        if (gameOver || gameManager == null) {
            return; // Không thể pause khi đã game over
        }
        if (gameManager.isPaused()) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    /**
     * Kích hoạt trạng thái Tạm dừng.
     */
    private void pauseGame() {
        gameManager.pause();
        gameLoop.stop(); // Dừng vòng lặp game
        if (pauseScreen == null) {
            pauseScreen = new PauseScreen(this); // 'this' là PauseListener
        }
        gameRoot.getChildren().add(pauseScreen); // Thêm màn hình pause lên trên Canvas

        SoundManager.getInstance().pauseMusic(); // Tạm dừng nhạc
    }

    /**
     * Thoát khỏi trạng thái Tạm dừng.
     */
    private void resumeGame() {
        if (gameManager.isPaused()) {
            gameManager.resume();
            gameRoot.getChildren().remove(pauseScreen); // Gỡ màn hình pause
            gameLoop.start(); // Tiếp tục vòng lặp game

            SoundManager.getInstance().playMusic(); // Phát tiếp nhạc
        }
    }

    /**
     * Dừng game và hiển thị màn hình Game Over.
     */
    private void showGameOverScreen() {
        gameLoop.stop(); // Dừng vòng lặp game
        if (gameOverScreen == null) {
            gameOverScreen = new GameOverScreen(this); // 'this' là GameOverListener
        }
        gameRoot.getChildren().add(gameOverScreen); // Thêm màn hình game over lên trên Canvas
    }

    // --- TRIỂN KHAI CÁC HÀM TỪ INTERFACE (LISTENER) ---

    /**
     * (Từ PauseListener) Được gọi khi nhấn nút "Resume" trên PauseScreen.
     */
    @Override
    public void onResume() {
        resumeGame();
    }

    /**
     * (Từ MenuListener) Được gọi khi nhấn nút "Start" trên MenuScreen.
     */
    @Override
    public void onStartGame() {
        startGame();
    }

    /**
     * (Từ MenuListener) Được gọi khi nhấn nút "Exit" trên MenuScreen.
     */
    @Override
    public void onExitGame() {
        stage.close();
    }

    /**
     * (Từ MenuListener) Được gọi khi nhấn nút "Sound" trên MenuScreen.
     */
    @Override
    public void onToggleSound() {
        // Yêu cầu SoundManager xử lý việc bật/tắt
        SoundManager.getInstance().toggleSound();

        // Báo cho MenuScreen cập nhật lại hình ảnh nút sound on/off
        if (menuScreen != null) {
            menuScreen.updateSoundButtons(SoundManager.getInstance().isMusicOn());
        }
    }

    /**
     * (Từ GameOverListener) Được gọi khi nhấn nút "Play Again" trên GameOverScreen.
     */
    @Override
    public void onPlayAgain() {
        // Dọn dẹp màn hình game over
        if (gameOverScreen != null) {
            gameRoot.getChildren().remove(gameOverScreen);
        }
        // Reset toàn bộ trạng thái game
        gameManager = null;
        gameLoop = null;
        gameRoot = null;
        gameScene = null;
        gameOverScreen = null;
        pauseScreen = null;
        gameOver = false;

        // Quay trở lại màn hình Menu
        stage.setScene(menuScene);
    }

    // --- HÀM MAIN (Điểm vào) ---
    public static void main(String[] args) {
        launch(args);
    }
}