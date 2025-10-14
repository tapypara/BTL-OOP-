package org.example.akranoid;

/**
 * Đại diện cho viên gạch trong game.
 * Mỗi viên gạch có thể có độ bền và loại khác nhau.
 */
public class Brick extends GameObject {

    protected int hitPoints;
    protected String type;

    public Brick(double x, double y, double width, double height, int hitPoints, String type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
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
        // TODO: giảm hitPoints khi bị bóng va chạm
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    @Override
    public void update() {
        // TODO: logic cập nhật (hiện tại gạch không di chuyển)
    }

    @Override
    public void render() {
        // TODO: vẽ gạch
    }
}
