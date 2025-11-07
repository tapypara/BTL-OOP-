package org.example.baitaplon.model;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.baitaplon.game.GameManager;
import org.example.baitaplon.powerup.PowerUp;

/**
 * Đại diện cho một viên gạch trong game.
 * Xử lý va chạm, tính máu, và logic nổ/power-up.
 */
public class Brick extends GameObject {

    protected int hitPoints; // Số mạng của gạch
    protected String type;   // Loại gạch (dùng để load ảnh và xử lý logic)
    private Image image;

    public Brick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
        loadImageByType(this.type);
    }

    /**
     * Tải ảnh gạch dựa trên "type" (tên file không có .png).
     */
    private void loadImageByType(String typeName) {
        String imagePath = "/assets/" + typeName + ".png";
        try {
            this.image = new Image(getClass().getResource(imagePath).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("⚠ Lỗi: Không tìm thấy ảnh gạch: " + imagePath);
        }
    }

    // --- Các hàm Getters/Setters cơ bản ---

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Xử lý logic khi gạch bị bóng va chạm.
     *
     * @param gm GameManager để truy cập danh sách gạch và power-up.
     * @return Số điểm nhận được (10 điểm nếu trúng, 0 nếu là gạch không thể vỡ).
     */
    public int takeHit(GameManager gm) {
        List<Brick> bricks = gm.getBricks();

        // Gạch không thể phá hủy
        if ("unbreakable_brick".equals(this.type)) {
            return 0;
        }

        if (this.hitPoints > 0) {
            this.hitPoints--;
        }

        // 1. Kiểm tra xem gạch đã vỡ chưa
        if (isDestroyed()) {
            // 2. NẾU VỠ: Kiểm tra và sinh power-up
            spawnPU(gm);

            // 3. NẾU VỠ: Xử lý logic gạch bom (nổ lan)
            if (this.type.equals("bomb_brick")) {
                double bombCenterX = this.getX() + 40; // Tọa độ X tâm gạch bom
                double bombCenterY = this.getY() + 20; // Tọa độ Y tâm gạch bom

                // Duyệt qua tất cả các gạch khác
                for (Brick brick : bricks) {
                    double otherCenterX = brick.getX() + 40;
                    double otherCenterY = brick.getY() + 20;

                    // Kiểm tra xem gạch có nằm trong vùng 3x3 (kể cả gạch bom) không
                    if (Math.abs(otherCenterX - bombCenterX) <= 80 && // 1 gạch bên trái/phải
                            Math.abs(otherCenterY - bombCenterY) <= 40 && // 1 gạch trên/dưới
                            brick.hitPoints > 0) { // Chỉ ảnh hưởng gạch còn sống

                        String tmp = brick.getType();
                        // Gạch thường (nhiều màu) sẽ vỡ ngay lập tức
                        if (tmp.equals("yellow_brick") || tmp.equals("red_brick") ||
                                tmp.equals("green_brick") || tmp.equals("blue_brick")) {
                            brick.hitPoints = 0;
                        } else {
                            // Gạch đặc biệt (gạch power-up, gạch bom khác) sẽ bị trừ 1 máu
                            // (Dùng đệ quy takeHit để xử lý nếu gạch bom nổ trúng gạch bom khác)
                            brick.takeHit(gm);
                        }

                        // Nếu gạch bị nổ vỡ, nó cũng có thể sinh power-up
                        if (brick.isDestroyed()) {
                            brick.spawnPU(gm);
                        }
                    }
                }
            }

            return 10; // 4. Trả điểm sau khi xử lý vỡ gạch
        }

        // 5. NẾU CHƯA VỠ: Xử lý hạ cấp (đổi màu) gạch thường
        switch (this.type) {
            case "yellow_brick":
                this.type = "green_brick";
                loadImageByType(this.type);
                break;
            case "green_brick":
                this.type = "red_brick";
                loadImageByType(this.type);
                break;
            case "red_brick":
                this.type = "blue_brick";
                loadImageByType(this.type);
                break;
        }
        return 10; // Trả điểm (vì va chạm nhưng chưa vỡ)
    }

    /**
     * Kiểm tra và sinh Power-Up (PU) nếu đây là gạch loại đặc biệt.
     *
     * @param gm GameManager để thêm Power-Up vào danh sách.
     */
    public void spawnPU(GameManager gm) {
        List<PowerUp> powerUps = gm.getPowerUps();
        String puType = this.type;

        // Chỉ các loại gạch này mới sinh power-up
        if (puType.equals("upsidepaddle_brick") || puType.equals("speedup_brick") ||
                puType.equals("speeddown_brick") || puType.equals("addlife_brick") ||
                puType.equals("downsidepaddle_brick")) { // (Loại này chưa được dùng trong loadLevel)

            // Tạo một PowerUp mới tại vị trí của viên gạch
            powerUps.add(new PowerUp(this.x, this.y, puType));
        }
        // Các loại gạch khác (blue, red, bomb...) sẽ không sinh power-up
    }

    /**
     * Kiểm tra xem gạch đã bị phá hủy chưa (hết máu).
     */
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    @Override
    public void update() {
        // Gạch là vật thể tĩnh, không cần cập nhật vị trí.
    }

    @Override
    public void render(GraphicsContext gc) {
        // Chỉ vẽ gạch nếu nó chưa bị phá hủy
        if (!isDestroyed()) {
            if (image != null) {
                gc.drawImage(image, x, y, width, height);
            } else {
                // Hình ảnh thay thế nếu không tải được
                gc.setFill(javafx.scene.paint.Color.GRAY);
                gc.fillRect(x, y, width, height);
                gc.setStroke(javafx.scene.paint.Color.WHITE);
                gc.strokeRect(x, y, width, height);
            }
        }
    }
}