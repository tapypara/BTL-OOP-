package org.example.baitaplon.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Đại diện cho quả bóng trong game.
 * Quản lý logic di chuyển, va chạm, và trạng thái "dính" (stuck) vào Paddle.
 */
public class Ball extends MovableObject {

    protected double speed;       // Tổng tốc độ (magnitude)
    protected double directionX;  // Thành phần hướng X (chuẩn hóa, -1 đến 1)
    protected double directionY;  // Thành phần hướng Y (chuẩn hóa, -1 đến 1)
    private Image image;

    // Biến trạng thái
    private boolean isStuck; // Bóng có đang dính vào paddle không?
    private Paddle paddleReference; // Tham chiếu đến paddle (để biết vị trí khi dính)

    public Ball(double x, double y, double radius, double speed, double directionX, double directionY) {
        super(x, y, radius * 2, radius * 2); // Kích thước là 2 lần bán kính
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
        this.isStuck = true; // Mặc định là dính
        this.paddleReference = null;
    }

    // ... (Toàn bộ các hàm Getters/Setters và update() giữ nguyên) ...
    // ... (Các hàm getSpeed, setSpeed, getDirectionX, setDirectionX, ...)
    // ... (getDirectionY, setDirectionY, isStuck, setStuck, setPaddleReference, ...)
    // ... (checkCollision, update, render) ...
    // ... (Vui lòng giữ nguyên các hàm đó, chúng không thay đổi) ...

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
            setDx(speed * directionX);
            setDy(-speed * Math.abs(directionY));
        } else {
            setDx(0);
            setDy(0);
        }
    }


    public void setPaddleReference(Paddle paddleReference) {
        this.paddleReference = paddleReference;
    }

    public boolean checkCollision(GameObject other) {
        return this.x < other.getX() + other.getWidth() &&
                this.x + this.width > other.getX() &&
                this.y < other.getY() + other.getHeight() &&
                this.y + this.height > other.getY();
    }

    @Override
    public void update() {
        if (isStuck && paddleReference != null) {
            x = paddleReference.getX() + paddleReference.getWidth() / 2 - width / 2;
            y = paddleReference.getY() - height;
        } else {
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

        // --- Xử lý nảy bóng đặc biệt với PADDLE ---
        if (other instanceof Paddle) {

            // 1. Tính toán độ lún (overlap) ở 3 phía liên quan
            double overlapLeft = (this.x + this.width) - other.getX();
            double overlapRight = (other.getX() + other.getWidth()) - this.x;
            double overlapTop = (this.y + this.height) - other.getY();

            // Tìm độ lún ngang nhỏ nhất
            double minOverlapX = Math.min(overlapLeft, overlapRight);

            // 2. Quyết định va chạm
            // Chỉ va chạm MẶT TRÊN khi:
            // a) Bóng đang đi xuống (dy > 0)
            // b) Độ lún ở mặt trên (overlapTop) nhỏ hơn độ lún ở mặt bên (minOverlapX)
            //    (Nghĩa là nó va chạm vào mặt trên trước)
            if (overlapTop < minOverlapX && this.getDirectionY() > 0) {
                // --- VA CHẠM MẶT TRÊN ---
                // Phân giải: Đẩy bóng lên trên paddle
                this.y = other.getY() - this.height;

                // Phản hồi: Tính toán góc nảy dựa trên vị trí va chạm trên paddle
                double centerBall = x + width / 2;
                double centerPaddle = other.getX() + other.getWidth() / 2;
                double relativeIntersectX = (centerBall - centerPaddle) / (other.getWidth() / 2);

                directionX = relativeIntersectX * 0.8;
                directionY = -Math.abs(directionY); // Luôn nảy lên

                // Chuẩn hóa vector hướng để tốc độ không đổi
                double length = Math.sqrt(directionX * directionX + directionY * directionY);
                if (length != 0) {
                    directionX /= length;
                    directionY /= length;
                }
            }
            else {
                // --- VA CHẠM HÔNG ---
                // (Xảy ra khi minOverlapX <= overlapTop, HOẶC bóng đang đi lên)

                // Phân giải: Đẩy bóng ra hông
                if (overlapLeft < overlapRight) {
                    this.x = other.getX() - this.width; // Đẩy sang trái
                } else {
                    this.x = other.getX() + other.getWidth(); // Đẩy sang phải
                }

                // Phản hồi: Chỉ đảo hướng X (giống như đập tường)
                this.directionX *= -1;
            }
            return; // Hoàn thành xử lý Paddle
        }

        // --- Logic Phân giải AABB (cho GẠCH) ---
        double overlapLeft = (this.x + this.width) - other.getX();
        double overlapRight = (other.getX() + other.getWidth()) - this.x;
        double overlapTop = (this.y + this.height) - other.getY();
        double overlapBottom = (other.getY() + other.getHeight()) - this.y;

        double minOverlapX = Math.min(overlapLeft, overlapRight);
        double minOverlapY = Math.min(overlapTop, overlapBottom);

        if (minOverlapX < minOverlapY) {
            if (overlapLeft < overlapRight) {
                this.x = other.getX() - this.width;
            } else {
                this.x = other.getX() + other.getWidth();
            }
            this.directionX *= -1;
        }
        else {
            if (overlapTop < overlapBottom) {
                this.y = other.getY() - this.height;
            } else {
                this.y = other.getY() + other.getHeight();
            }
            this.directionY *= -1;
        }
    }


    @Override
    public void render (GraphicsContext gc){
        if (image != null) {
            gc.drawImage(image, x, y, width, height);
        } else {
            gc.setFill(javafx.scene.paint.Color.RED);
            gc.fillOval(x, y, width, height);
        }
    }
}
