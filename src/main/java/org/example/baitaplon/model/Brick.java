// <<< SỬA DÒNG PACKAGE (Đã đúng) >>>
package org.example.baitaplon.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Đại diện cho viên gạch trong game.
 * Lớp này giờ đã được nâng cấp để tải bất kỳ ảnh nào.
 */
// <<< GameObject ở cùng package (Đã đúng) >>>
public class Brick extends GameObject {

    // ... (Code này đã chuẩn, giữ nguyên) ...
    protected int hitPoints;
    protected String type; // 'type' giờ sẽ là "blue_brick", "red_brick"...
    private Image image;

    /**
     * Hàm constructor (Đã chuẩn, giữ nguyên)
     * Nó nhận 'type' là tên file ảnh (ví dụ: "blue_brick")
     */
    public Brick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
        loadImageByType(this.type); // Gọi hàm đã được nâng cấp
    }

    // <<< THAY ĐỔI CHÍNH NẰM Ở ĐÂY >>>
    /**
     * HÀM ĐƯỢC NÂNG CẤP:
     * Tải ảnh gạch dựa trên "type" (tên file không có .png)
     * @param typeName Ví dụ: "blue_brick", "red_brick", "unbreakable_brick"
     */
    private void loadImageByType(String typeName) {
        // Đường dẫn giờ sẽ được tạo động từ 'typeName'
        String imagePath = "/assets/" + typeName + ".png";

        try {
            this.image = new Image(getClass().getResource(imagePath).toExternalForm());
        } catch (NullPointerException e) {
            // In ra lỗi rõ ràng hơn để bạn biết đang thiếu ảnh nào
            System.err.println("⚠ Lỗi: Không tìm thấy ảnh gạch: " + imagePath);
        }
    }
    // <<< KẾT THÚC THAY ĐỔI >>>


    // --- CÁC PHẦN SAU ĐÃ CHUẨN, GIỮ NGUYÊN ---

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
     * Hàm takeHit() (Đã chuẩn, giữ nguyên)
     * Gạch "unbreakable" (HP 999) sẽ không bao giờ bị phá hủy.
     */
    public void takeHit() {
        if (this.hitPoints > 0) {
            this.hitPoints--; // Giảm HP

            // Nếu là gạch "unbreakable" (HP giờ là 998), set lại 999
            if (this.type.equals("unbreakable_brick")) {
                this.hitPoints = 999;
            }
        }
    }

    /**
     * Hàm isDestroyed() (Đã chuẩn, giữ nguyên)
     */
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    /**
     * Hàm update() (Đã chuẩn, giữ nguyên)
     */
    @Override
    public void update() {
        // Gạch không cần di chuyển
    }

    /**
     * Hàm render() (Đã chuẩn, giữ nguyên)
     * Nó tự động kiểm tra !isDestroyed() nên gạch đã vỡ sẽ tàng hình.
     */
    @Override
    public void render(GraphicsContext gc) {
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