package org.example.baitaplon.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.example.baitaplon.game.GameManager;
import org.example.baitaplon.powerup.PowerUp;

import java.util.List;

/**
 * Đại diện cho viên gạch trong game.
 * Đã nâng cấp hàm takeHit() để xử lý việc chuyển loại gạch (degradation).
 */
public class Brick extends GameObject {

    protected int hitPoints;
    protected String type;
    private Image image;

    public Brick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
        loadImageByType(this.type);
    }

    /**
     * Tải ảnh gạch dựa trên "type" (tên file không có .png)
     */
    private void loadImageByType(String typeName) {
        String imagePath = "/assets/" + typeName + ".png";
        try {
            this.image = new Image(getClass().getResource(imagePath).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("⚠ Lỗi: Không tìm thấy ảnh gạch: " + imagePath);
        }
    }

    // --- CÁC HÀM GETTER/SETTER (Giữ nguyên) ---
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

    // <<< HÀM NÀY ĐÃ ĐƯỢC VIẾT LẠI HOÀN TOÀN >>>
    /**
     * Xử lý khi gạch bị va chạm.
     * Giảm HP và chuyển loại (thay đổi ảnh) nếu là gạch thường.
     * Vỡ ngay lập tức nếu là gạch power-up.
     */
    public int takeHit(GameManager gm) {
        List<Brick>  bricks = gm.getBricks();
        List<PowerUp> powerUps = gm.getPowerUps();

        // Gạch không thể phá vỡ thì bỏ qua luôn
        if ("unbreakable_brick".equals(this.type)) {
            hitPoints = 999; // Đảm bảo HP luôn cao
            return 10; // Vẫn tính điểm va chạm
        }

        // Giảm HP
        if (this.hitPoints > 0) {
            this.hitPoints--;
        }

        // KIỂM TRA: Gạch đã bị phá hủy sau cú va chạm này chưa?
        if (isDestroyed()) {
            // NẾU GẠCH VỠ: Xử lý logic "khi vỡ" (spawn PU, nổ bom)
            switch (this.type) {
                case "upsidepaddle_brick":
                case "downsidepaddle_brick": // Bạn cần thêm case này vào hàm spawnPU
                case "speedup_brick":
                case "speeddown_brick":
                case "addlife_brick":
                    this.spawnPU(gm); // <- Bây giờ code sẽ chạy tới đây
                    break;
                // ... (các case power-up khác)

                case "bomb_brick":
                    // 1. Lấy TÂM của quả bom
                    double bombCenterX = this.getX() + 40; // Giả sử gạch rộng 80
                    double bombCenterY = this.getY() + 20; // Giả sử gạch cao 40

                    for(Brick brick : bricks) {

                        // 2. Lấy TÂM của gạch lân cận
                        double otherCenterX = brick.getX() + 40;
                        double otherCenterY = brick.getY() + 20;

                        // 3. So sánh TÂM VỚI TÂM
                        // Kiểm tra xem gạch này có nằm trong lưới 3x3 xung quanh quả bom không
                        if (Math.abs(otherCenterX - bombCenterX) <= 80 && // Cách biệt 0 (chính nó) hoặc 80 (trái/phải)
                                Math.abs(otherCenterY - bombCenterY) <= 40 && // Cách biệt 0 (chính nó) hoặc 40 (trên/dưới)
                                brick.hitPoints > 0) { // Chỉ ảnh hưởng gạch còn sống (tránh tự nổ)

                            // --- Logic bên trong giữ nguyên như của bạn ---
                            String tmp =  brick.getType();
                            if(tmp.equals("yellow_brick") || tmp.equals("red_brick") ||
                                    tmp.equals("green_brick") || tmp.equals("blue_brick")) {
                                brick.hitPoints = 0; // Phá hủy gạch thường
                            }
                            else {
                                // Kích hoạt gạch đặc biệt khác (ví dụ: bom nổ dây chuyền)
                                brick.takeHit(gm);
                            }

                            // Đảm bảo gạch power-up bị nổ vẫn rớt đồ
                            if(brick.isDestroyed()) {
                                brick.spawnPU(gm);
                            }
                        }
                    }
                    break; // Kết thúc case "bomb_brick"

                // ... (các case khác)
                // Gạch thường khi vỡ (blue_brick) không cần làm gì thêm ở đây
            }
            return 10; // Trả điểm khi gạch vỡ

        } else {
            // NẾU GẠCH CHƯA VỠ: Xử lý logic "đổi màu"
            switch (this.type) {
                case "yellow_brick": // (HP từ 4 -> 3)
                    this.type = "green_brick";
                    loadImageByType(this.type);
                    break;
                case "green_brick": // (HP từ 3 -> 2)
                    this.type = "red_brick";
                    loadImageByType(this.type);
                    break;
                case "red_brick": // (HP từ 2 -> 1)
                    this.type = "blue_brick";
                    loadImageByType(this.type);
                    break;
            }
            return 10; // Trả điểm khi va chạm (chưa vỡ)
        }
    }
    public void spawnPU(GameManager gm) {
        List<PowerUp> powerUps = gm.getPowerUps();
        String Type = this.type;
        // THÊM "downsidepaddle_brick" VÀO ĐÂY
        if(type.equals("upsidepaddle_brick") || type.equals("speedup_brick")
                || type.equals("speeddown_brick") || type.equals("addlife_brick")
                || type.equals("downsidepaddle_brick")) { // <-- THÊM VÀO ĐÂY
            powerUps.add(new PowerUp(this.x, this.y, Type));
        } else {
            return;
        }
    }

    // <<< KẾT THÚC HÀM VIẾT LẠI >>>

    /**
     * Hàm isDestroyed() (Giữ nguyên)
     */
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    /**
     * Hàm update() (Giữ nguyên)
     */
    @Override
    public void update() {
        // Gạch không cần di chuyển
    }

    /**
     * Hàm render() (Giữ nguyên)
     */
    @Override
    public void render(GraphicsContext gc) {
        // Nó tự động kiểm tra !isDestroyed() nên gạch đã vỡ sẽ tàng hình.
        if (!isDestroyed()) {
            if (image != null) {
                gc.drawImage(image, x, y, width, height);
            } else {
                // Fallback: Vẽ hình chữ nhật nếu không load được ảnh
                gc.setFill(javafx.scene.paint.Color.GRAY);
                gc.fillRect(x, y, width, height);
                gc.setStroke(javafx.scene.paint.Color.WHITE);
                gc.strokeRect(x, y, width, height);
            }
        }
    }
}
