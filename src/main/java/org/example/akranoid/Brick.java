package org.example.baitaplon;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Đại diện cho viên gạch trong game.
 * Mỗi viên gạch có thể có độ bền và loại khác nhau.
 */
public class Brick extends GameObject {

    protected int hitPoints;
    protected String type;
    private Image image;

    public Brick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
        loadImageByType(type);
    }

    private void loadImageByType(String brickType) {
        String imagePath = "/assets/blue_brick.png"; // Ảnh mặc định
        // Ví dụ: chọn ảnh dựa vào giá trị của 'type'
        if ("red".equalsIgnoreCase(brickType)) {
            imagePath = "/assets/red_brick.png";
        } else if ("green".equalsIgnoreCase(brickType)) {
            imagePath = "/assets/green_brick.png";
        } // Thêm các màu khác nếu có...

        try {
            this.image = new Image(getClass().getResource(imagePath).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Không thể load ảnh brick type '" + brickType + "': " + e.getMessage());
        }
    }

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

    public void takeHit() {
        if (this.hitPoints > 0) {
            this.hitPoints--;
        }
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    @Override
    public void update() {
        // TODO: logic cập nhật (hiện tại gạch không di chuyển)
    }

    @Override
    public void render(GraphicsContext gc) { // Đảm bảo có tham số gc
        // 3. Vẽ ảnh nếu gạch chưa bị phá hủy
        if (!isDestroyed()) {
            if (image != null) {
                gc.drawImage(image, x, y, width, height);
            } else {
                // (Tùy chọn) Vẽ hình chữ nhật màu nếu không load được ảnh
                gc.setFill(javafx.scene.paint.Color.GRAY); // Màu mặc định
                gc.fillRect(x, y, width, height);
                gc.setStroke(javafx.scene.paint.Color.WHITE); // Thêm viền
                gc.strokeRect(x, y, width, height);
            }
        }
    }
}
