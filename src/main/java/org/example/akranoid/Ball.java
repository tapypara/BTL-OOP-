package org.example.baitaplon;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Đại diện cho quả bóng trong game.
 * Bóng có thể di chuyển, va chạm và nảy lại khi gặp vật cản.
 */
public class Ball extends MovableObject {

    protected double speed;       // Tổng tốc độ (magnitude)
    protected double directionX;  // Thành phần hướng X (chuẩn hóa)
    protected double directionY;  // Thành phần hướng Y (chuẩn hóa)
    private Image image;
    // Biến trạng thái
    private boolean isStuck;
    private Paddle paddleReference;

    public Ball(double x, double y, double radius, double speed, double directionX, double directionY) {
        super(x, y, radius * 2, radius * 2);
        this.speed = speed;
        this.directionX = directionX;
        this.directionY = directionY;
        setDx(speed * directionX);
        setDy(speed * directionY);
        try {
            String imagePath = "/assets/ball.png";
            this.image = new Image(getClass().getResource(imagePath).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Không thể load ảnh ball: " + e.getMessage());
        }
        this.isStuck = true;
        this.paddleReference = null;
    }

    // ... (Getters/Setters cho speed, directionX, directionY)
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDirectionX() {
        return directionX;
    }

    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public double getDirectionY() {
        return directionY;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    public boolean isStuck() {
        return isStuck;
    }

    public void setStuck(boolean stuck) {
        isStuck = stuck;
        if (!stuck) {
            // Nếu bóng được bắn ra, đặt hướng ban đầu (ví dụ: lên trên)
            // Dùng lại hướng đã khởi tạo, nhưng đảm bảo dy != 0
            setDx(speed * directionX);
            setDy(-speed * Math.abs(directionY));
        } else {
            // Nếu dính, dừng vận tốc
            setDx(0);
            setDy(0);
        }
    }


    public void setPaddleReference(Paddle paddleReference) {
        this.paddleReference = paddleReference;
    }

    /**
     * Kiểm tra va chạm hộp bao (AABB)
     */
    public boolean checkCollision(GameObject other) {
        // Logic AABB của bạn là đúng
        return this.x < other.getX() + other.getWidth() &&
                this.x + this.width > other.getX() &&
                this.y < other.getY() + other.getHeight() &&
                this.y + this.height > other.getY();
    }

    /**
     * Triển khai update() (Kế thừa từ MovableObject): Cập nhật vị trí bóng.
     */
    @Override
    public void update() {
        if (isStuck && paddleReference != null) {
            // Cập nhật vị trí bóng theo vị trí của paddle
            // Đặt bóng ở trung tâm chiều rộng của paddle, và ở ngay phía trên paddle
            x = paddleReference.getX() + paddleReference.getWidth() / 2 - width / 2;
            y = paddleReference.getY() - height;
        } else {
            // Nếu không dính, cập nhật vị trí theo vận tốc
            // Cập nhật dx, dy dựa trên speed và direction
            setDx(speed * directionX);
            setDy(speed * directionY);
            move();
        }
    }

    /**
     * Xử lý nảy bóng (Cải tiến logic nảy)
     *
     * @param other Vật thể va chạm
     */
    public void bounceOff(GameObject other) {
        // --- Nảy bóng khi va chạm với Brick hoặc Tường (Logic cơ bản) ---

        // Tính toán hướng va chạm bằng cách kiểm tra vị trí cũ
        double prevX = x - dx;
        double prevY = y - dy;

        // Va chạm theo phương ngang (Bóng chạm bên trái/phải của vật thể)
        if (prevX + width <= other.getX() || prevX >= other.getX() + other.getWidth()) {
            directionX *= -1;
        }
        // Va chạm theo phương dọc (Bóng chạm trên/dưới của vật thể)
        if (prevY + height <= other.getY() || prevY >= other.getY() + other.getHeight()) {
            directionY *= -1;
        }

        // --- Nảy bóng đặc biệt khi va chạm với PADDLE ---
        if (other instanceof Paddle) {
            double centerBall = x + width / 2;
            double centerPaddle = other.getX() + other.getWidth() / 2;
            double relativeIntersectX = (centerBall - centerPaddle) / (other.getWidth() / 2);

            // Đặt hướng X mới (tăng độ nảy ở rìa)
            directionX = relativeIntersectX * 0.8;

            // Đảm bảo bóng luôn nảy lên (DirectionY phải âm)
            directionY = -Math.abs(directionY);

            // Chuẩn hóa vector hướng để tốc độ không đổi
            double length = Math.sqrt(directionX * directionX + directionY * directionY);
            directionX /= length;
            directionY /= length;
        }
    }


        @Override
        public void render (GraphicsContext gc){
            if (image != null) {
                gc.drawImage(image, x, y, width, height);
            } else {
                // (Tùy chọn) Vẽ hình tròn màu nếu không load được ảnh
                gc.setFill(javafx.scene.paint.Color.RED);
                gc.fillOval(x, y, width, height);
            }
        }
}
