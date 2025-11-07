package org.example.baitaplon.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Đại diện cho quả bóng trong game.
 * Bóng có thể di chuyển, va chạm và nảy lại khi gặp vật cản.
 */
public class Ball extends MovableObject {

    protected double speed;       // Tốc độ di chuyển tổng thể
    protected double directionX;  // Hướng di chuyển X (đã chuẩn hóa)
    protected double directionY;  // Hướng di chuyển Y (đã chuẩn hóa)
    private Image image;

    // Trạng thái của bóng
    private boolean isStuck; // Bóng có đang dính vào thanh đỡ không?
    private Paddle paddleReference; // Tham chiếu đến thanh đỡ để biết vị trí khi bị dính

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

    // --- Các hàm Getters/Setters cơ bản ---

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

    /**
     * Đặt trạng thái dính/không dính của bóng.
     *
     * @param stuck Trạng thái dính mới
     */
    public void setStuck(boolean stuck) {
        isStuck = stuck;
        if (stuck) {
            // Nếu dính, dừng vận tốc
            setDx(0);
            setDy(0);
        } else {
            // Nếu bóng được bắn ra, đặt hướng ban đầu
            setDx(speed * directionX);
            // Đảm bảo bóng bay lên trên (directionY là số âm)
            setDy(-speed * Math.abs(directionY));
        }
    }

    public void setPaddleReference(Paddle paddleReference) {
        this.paddleReference = paddleReference;
    }

    /**
     * Kiểm tra va chạm (AABB - Hộp bao) với một vật thể khác.
     */
    public boolean checkCollision(GameObject other) {
        return this.x < other.getX() + other.getWidth() &&
                this.x + this.width > other.getX() &&
                this.y < other.getY() + other.getHeight() &&
                this.y + this.height > other.getY();
    }

    @Override
    public void update() {
        if (isStuck && paddleReference != null) {
            // Nếu bóng đang dính, cập nhật vị trí theo thanh đỡ
            // Đặt bóng ở trung tâm và ngay phía trên thanh đỡ
            x = paddleReference.getX() + paddleReference.getWidth() / 2 - width / 2;
            y = paddleReference.getY() - height;
        } else {
            // Nếu không dính, cập nhật vận tốc và di chuyển
            setDx(speed * directionX);
            setDy(speed * directionY);
            move();
        }
    }

    /**
     * Xử lý logic nảy bóng khi va chạm với vật thể khác.
     *
     * @param other Vật thể va chạm (Gạch hoặc Thanh đỡ)
     */
    public void bounceOff(GameObject other) {
        // Logic nảy đặc biệt khi va chạm với PADDLE
        if (other instanceof Paddle) {
            double centerBall = x + width / 2;
            double centerPaddle = other.getX() + other.getWidth() / 2;

            // Tính toán vị trí va chạm tương đối trên thanh đỡ (từ -1.0 đến 1.0)
            double relativeIntersectX = (centerBall - centerPaddle) / (other.getWidth() / 2);

            // Đặt hướng X mới dựa trên vị trí va chạm (tăng độ nảy ở rìa)
            // Giới hạn giá trị để góc nảy không quá dốc
            directionX = relativeIntersectX * 0.8;

            // Đảm bảo bóng luôn nảy LÊN (DirectionY phải là số âm)
            directionY = -Math.abs(directionY);

            // Chuẩn hóa lại vector hướng để đảm bảo tốc độ không đổi
            double length = Math.sqrt(directionX * directionX + directionY * directionY);
            directionX /= length;
            directionY /= length;

        } else { // Logic nảy với GẠCH hoặc TƯỜNG (logic cơ bản)
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
        }
    }


    @Override
    public void render(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x, y, width, height);
        } else {
            // Hình ảnh thay thế nếu không tải được
            gc.setFill(javafx.scene.paint.Color.RED);
            gc.fillOval(x, y, width, height);
        }
    }
}