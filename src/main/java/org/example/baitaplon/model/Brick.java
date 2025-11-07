package org.example.baitaplon.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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
    public int takeHit() {
        // 1. Xử lý gạch bất tử (loại 'g')
        if (this.type.equals("unbreakable_brick")) {
            this.hitPoints = 999; // Luôn giữ 999
            return 0; // Không làm gì thêm
        }

        // 2. Xử lý gạch Power-up (loại e, f, h) -> Vỡ ngay
        if (this.type.equals("pink_brick") ||
                this.type.equals("white_brick") ||
                this.type.equals("doubleball_brick")) {

            this.hitPoints = 0; // Vỡ ngay lập tức
            return 50; // Không làm gì thêm
        }

        // 3. Xử lý gạch thường (a, b, c, d)
        // Chỉ giảm HP nếu lớn hơn 0
        if (this.hitPoints > 0) {
            this.hitPoints--;
        }

        // 4. Kiểm tra xem gạch đã vỡ chưa
        if (isDestroyed()) {
            return 10; // Vỡ rồi, không cần chuyển loại
        }

        // 5. Logic chuyển loại gạch (d -> c -> b -> a)
        // Nếu gạch chưa vỡ, nó sẽ đổi ảnh
        switch (this.type) {
            case "yellow_brick": // 'd' (HP từ 4 -> 3)
                this.type = "green_brick"; // Chuyển thành 'c'
                loadImageByType(this.type); // Tải ảnh mới (green)
                break;
            case "green_brick": // 'c' (HP từ 3 -> 2)
                this.type = "red_brick"; // Chuyển thành 'b'
                loadImageByType(this.type); // Tải ảnh mới (red)
                break;
            case "red_brick": // 'b' (HP từ 2 -> 1)
                this.type = "blue_brick"; // Chuyển thành 'a'
                loadImageByType(this.type); // Tải ảnh mới (blue)
                break;
            case "blue_brick": // 'a' (HP từ 1 -> 0)
                // Đã vỡ, không cần làm gì
                break;
        }
        return 10;
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