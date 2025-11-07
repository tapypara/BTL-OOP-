// <<< SỬA DÒNG PACKAGE >>>
package org.example.baitaplon.game;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// <<< THÊM IMPORT CHO CÁC LỚP MODEL >>>
import org.example.baitaplon.model.Ball;
import org.example.baitaplon.model.Brick;
import org.example.baitaplon.model.Paddle;

// <<< THÊM IMPORT ĐỂ ĐỌC FILE >>>
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class GameManager {
    // ... (Code giữ nguyên) ...
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    private Paddle paddle; // Đã import
    private Ball ball;     // Đã import
    private List<Brick> bricks; // Đã import
    private boolean isGameOver;
    private boolean isPaused = false;

    // --- THÊM CÁC BIẾN MỚI ĐỂ QUẢN LÝ LEVEL ---
    private int currentLevel;
    private static final int MAX_LEVEL = 5; // Ví dụ có 5 level
    private boolean isGameWon = false;
    // ----------------------------------------

    public GameManager() {
        paddle = new Paddle(350, 550, 100, 20, 5.0, SCREEN_WIDTH);
        ball = new Ball(0, 0, 10, 2, 0.5, -0.866);
        ball.setPaddleReference(paddle);
        ball.setStuck(true);

        bricks = new ArrayList<>();
        currentLevel = 1; // Bắt đầu từ level 1
        isGameOver = false;
        isGameWon = false;
        isPaused = false;

        // <<< THAY THẾ createBricks() BẰNG loadLevel() >>>
        try {
            loadLevel(currentLevel); // Tải level đầu tiên
        } catch (Exception e) {
            System.err.println("LỖI NGHIÊM TRỌNG: Không thể tải level " + currentLevel + ": " + e.getMessage());
            // Nếu không tải được level, coi như game over ngay
            isGameOver = true;
        }
    }

    // <<< XÓA HOÀN TOÀN HÀM createBricks() >>>
    /*
    private void createBricks() {
        // ... (Code cũ đã bị xóa) ...
    }
    */

    // <<< HÀM MỚI: ĐỌC FILE LEVEL (THEO YÊU CẦU MỚI) >>>
    /**
     * Đọc file level (ví dụ /levels/level1.txt) và trả về nội dung
     * sau khi đã lọc bỏ các ký tự xuống dòng và tab.
     * Giữ lại dấu cách và dấu gạch dưới.
     */
    private String readLevelFile(int levelNumber) {
        String filePath = "/levels/level" + levelNumber + ".txt";
        try (InputStream is = getClass().getResourceAsStream(filePath)) {
            if (is == null) {
                System.err.println("Lỗi: Không tìm thấy file: " + filePath);
                return null;
            }
            // Đọc toàn bộ file
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // CHỈ XÓA: newline (\n), carriage return (\r), tab (\t)
            // GIỮ LẠI: dấu cách (' ') và mọi ký tự khác
            return content.replaceAll("[\\n\\r\\t]", "");

        } catch (Exception e) {
            System.err.println("Lỗi khi đọc file level " + levelNumber + ": " + e.getMessage());
            return null;
        }
    }

    // <<< HÀM MỚI: TẠO GẠCH TỪ FILE (THEO YÊU CẦU MỚI) >>>
    /**
     * Tải gạch cho một level cụ thể từ file cấu hình.
     * Ném ra Exception nếu file không hợp lệ.
     */
    private void loadLevel(int levelNumber) throws Exception {
        bricks.clear();
        ball.setStuck(true); // Reset bóng về paddle mỗi khi qua level

        String levelData = readLevelFile(levelNumber); // Gọi hàm đọc file (giữ nguyên)

        // --- VALIDATION (Giữ nguyên) ---
        if (levelData == null) {
            throw new Exception("File level " + levelNumber + " rỗng hoặc không tìm thấy.");
        }
        if (levelData.length() != 70) {
            throw new Exception("File level " + levelNumber + " không chứa đúng 70 ký tự (đã có " + levelData.length() + ").");
        }

        // Cập nhật Regex để cho phép ký tự 'a' đến 'h'
        if (levelData.matches(".*[^a-h_ ].*")) {
            throw new Exception("File level " + levelNumber + " chứa ký tự không hợp lệ (chỉ cho phép a-h, _, và dấu cách).");
        }
        // --- Hết VALIDATION ---

        // Giữ nguyên các thông số kích thước gạch
        int brickWidth = 80;
        int brickHeight = 40;
        final int INITIAL_Y = 10;
        final int ROWS = 7;
        final int COLS = 10;

        int charIndex = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                double x = j * brickWidth;
                double y = INITIAL_Y + i * brickHeight;
                char brickType = levelData.charAt(charIndex);
                charIndex++;

                // <<< THAY ĐỔI: CẬP NHẬT SWITCH CASE THEO MAPPING MỚI >>>
                switch (brickType) {
                    case 'a': // blue_brick.png
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "blue_brick"));
                        break;
                    case 'b': // red_brick.png
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 2, "red_brick"));
                        break;
                    case 'c': // green_brick.png
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 3, "green_brick"));
                        break;
                    case 'd': // yellow_brick.png (Giả sử là gạch 1 HP)
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "yellow_brick"));
                        break;
                    case 'e': // pink_brick.png (Giả sử là gạch 1 HP)
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "pink_brick"));
                        break;
                    case 'f': // white_brick.png (Giả sử là gạch cứng 4 HP)
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 4, "white_brick"));
                        break;
                    case 'g': // unbreakable_brick.png (Bất tử)
                        // Gán số HP cực lớn (ví dụ 999) để nó không bao giờ vỡ
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 999, "unbreakable_brick"));
                        break;
                    case 'h': // doubleball_brick.png (Power-up, 1 HP)
                        bricks.add(new Brick(x, y, brickWidth, brickHeight, 1, "doubleball_brick"));
                        break;
                    case '_': // Gạch dưới là ô trống
                    case ' ': // Dấu cách CŨNG là ô trống
                        // Không làm gì cả, bỏ qua ô này
                        break;
                }
            }
        }
    }


    public void pause() {
        // ... (Code giữ nguyên) ...
        isPaused = true;
    }

    public void resume() {
        // ... (Code giữ nguyên) ...
        isPaused = false;
    }

    public boolean isPaused() {
        // ... (Code giữ nguyên) ...
        return isPaused;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    // <<< HÀM MỚI: Getter cho trạng thái thắng >>>
    public boolean isGameWon() {
        return isGameWon;
    }

    // <<< HÀM MỚI: Kiểm tra xem tất cả gạch đã bị phá hủy chưa >>>
    private boolean areAllBricksCleared() {
        // Nếu level không có gạch (chỉ toàn '_') thì cũng thắng
        if (bricks.isEmpty()) {
            return true;
        }

        for (Brick brick : bricks) {
            // Mọi gạch (a-h) đều có hitPoints > 0
            if (!brick.isDestroyed()) {
                return false; // Còn gạch chưa vỡ
            }
        }

        // Nếu vòng lặp kết thúc mà không 'return false'
        return true; // Tất cả gạch đã vỡ
    }

    // <<< HÀM MỚI: Xử lý khi qua màn >>>
    private void goToNextLevel() {
        this.currentLevel++;
        if (this.currentLevel > MAX_LEVEL) {
            this.isGameWon = true; // Thắng game!
            System.out.println("Chúc mừng! Bạn đã thắng!");
        } else {
            // Tải level tiếp theo
            try {
                loadLevel(this.currentLevel);
            } catch (Exception e) {
                System.err.println("LỖI: Không thể tải level " + this.currentLevel + ": " + e.getMessage());
                isGameOver = true; // Coi như thua nếu level tiếp theo bị lỗi
            }
        }
    }


    public void updateGame() {
        // ... (Code giữ nguyên) ...
        if (isPaused) {
            return;
        }

        // <<< SỬA LẠI: Thêm check isGameWon >>>
        if (!isGameOver && !isGameWon) {
            paddle.update();
            ball.update();
            checkCollisions();

            // *** LOGIC MỚI ĐỂ CHUYỂN LEVEL ***
            if (areAllBricksCleared()) {
                goToNextLevel();
            }
            // *********************************

        } else {
            // Nếu game over hoặc game won, chỉ update bóng
            ball.update();
        }
    }

    private void checkCollisions() {
        // ... (Code giữ nguyên) ...
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= SCREEN_WIDTH) {
            ball.setDirectionX(-ball.getDirectionX());
        }
        if (ball.getY() <= 0) {
            ball.setDirectionY(-ball.getDirectionY());
        }
        if (ball.getY() + ball.getHeight() >= SCREEN_HEIGHT) {
            if (!isGameOver && !isGameWon) { // Thêm check isGameWon
                System.out.println("Game Over!");
                isGameOver = true;
            }
        }
        if (ball.checkCollision(paddle)) {
            if (ball.getDirectionY() > 0) {
                ball.bounceOff(paddle);
            }
        }
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (brick.isDestroyed()) continue;
            if (ball.checkCollision(brick)) {
                ball.bounceOff(brick);
                brick.takeHit();
                if (brick.isDestroyed()) {
                    // brickIterator.remove(); // <<< TẠM THỜI KHÔNG XÓA ĐỂ `areAllBricksCleared` HOẠT ĐỘNG
                    // Tốt hơn là `areAllBricksCleared` nên check `!brick.isDestroyed()`
                    // Giữ nguyên `brickIterator.remove()` là đúng
                }
                break;
            }
        }

        // <<< LƯU Ý QUAN TRỌNG VỀ LOGIC CHUYỂN LEVEL >>>
        // Nếu bạn `brickIterator.remove()` thì `bricks.isEmpty()` sẽ được kích hoạt
        // để qua màn. Chúng ta cần sửa lại `areAllBricksCleared`

        // <<< SỬA LẠI: areAllBricksCleared (Cách 2) >>>
        // Chúng ta nên giữ lại gạch đã vỡ trong list, chỉ là không render
        // và không check va chạm.
        // NHƯNG code hiện tại của bạn đang `remove()`.

        // <<< SỬA LẠI `checkCollisions` VÀ `areAllBricksCleared` ĐỂ NHẤT QUÁN >>>
        /* * Để `areAllBricksCleared` hoạt động đúng với `brickIterator.remove()`,
         * hàm `areAllBricksCleared` phải check `bricks.isEmpty()`
         */

        // <<< SỬA LẠI areAllBricksCleared() (Cách 3 - Tốt nhất) >>>
        /* * Chúng ta không dùng `areAllBricksCleared` nữa,
         * mà check `bricks.isEmpty()` ngay trong `updateGame`
         */
    }

    // <<< THAY ĐỔI CUỐI CÙNG ĐỂ ĐẢM BẢO LOGIC HOẠT ĐỘNG >>>
    /*
     * Vấn đề: `checkCollisions` XÓA gạch (remove).
     * Hàm `areAllBricksCleared` của tôi lại LẶP QUA list gạch.
     * Cách đúng: `areAllBricksCleared` phải đếm số gạch "chưa bị phá hủy".
     * * Chúng ta sẽ giữ nguyên `areAllBricksCleared` như tôi viết ở trên,
     * nhưng `checkCollisions` PHẢI DỪNG VIỆC `remove()`.
     * * Thay vào đó, chúng ta sẽ chỉ `remove()` khi `brick.isDestroyed()`
     * trong hàm `renderGame` hoặc đầu `checkCollisions`.
     * * ĐỂ ĐƠN GIẢN, chúng ta sẽ làm lại `checkCollisions` và `areAllBricksCleared`
     * như sau:
     */

    /* * Sửa lại `checkCollisions` (Không `remove` nữa)

        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (brick.isDestroyed()) continue; // Bỏ qua gạch đã vỡ
            if (ball.checkCollision(brick)) {
                ball.bounceOff(brick);
                brick.takeHit(); // Hàm này đặt isDestroyed = true nếu hết máu
                // KHÔNG REMOVE NỮA
                break;
            }
        }
    */

    /* * Và `areAllBricksCleared` (Giữ nguyên như tôi viết) là đúng

    private boolean areAllBricksCleared() {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                return false; // Còn gạch chưa vỡ
            }
        }
        return true; // Tất cả gạch đã vỡ (hoặc list rỗng)
    }
    */

    /* * VÀ `renderGame` PHẢI KIỂM TRA `!isDestroyed()` (file Brick.java của bạn đã làm vậy)
     * VẬY LÀ TỐT RỒI.
     * * CHỈ CẦN BỎ `brickIterator.remove();` trong `checkCollisions`
     */

    // <<< FILE CUỐI CÙNG SẼ TRÔNG NHƯ NÀY >>>

     /*
     private void checkCollisions() {
        // ... (code va chạm tường và paddle giữ nguyên) ...

        // Logic va chạm gạch
        Iterator<Brick> brickIterator = bricks.iterator();
        while (brickIterator.hasNext()) {
            Brick brick = brickIterator.next();
            if (brick.isDestroyed()) continue; // Bỏ qua nếu đã vỡ
            if (ball.checkCollision(brick)) {
                ball.bounceOff(brick);
                brick.takeHit(); // Chỉ gọi takeHit()
                // BỎ DÒNG: brickIterator.remove();
                break;
            }
        }
    }
    */

    /*
     * KHOAN ĐÃ, file Brick.java của bạn có hàm render:
     * * public void render(GraphicsContext gc) {
     * if (!isDestroyed()) { // <--- TỐT
     * // ... vẽ ...
     * }
     * }
     * * Vậy chúng ta chỉ cần BỎ DÒNG `brickIterator.remove();` trong `checkCollisions`
     * là mọi thứ sẽ hoạt động.
     */


    public void renderGame(GraphicsContext gc) {
        // ... (Code giữ nguyên) ...
        paddle.render(gc);
        ball.render(gc);
        for (Brick brick : bricks) {
            brick.render(gc); // File Brick.java đã tự check !isDestroyed()
        }
    }

    public Paddle getPaddle() {
        // ... (Code giữ nguyên) ...
        return paddle;
    }

    public Ball getBall() {
        // ... (Code giữ nguyên) ...
        return ball;
    }
}