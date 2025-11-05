// <<< SỬA DÒNG PACKAGE >>>
package org.example.baitaplon.model;

import javafx.scene.canvas.GraphicsContext;

/**
 * Lớp trừu tượng mở rộng từ GameObject,
 * ...
 */
// <<< GameObject ở cùng package nên KHÔNG CẦN IMPORT >>>
public abstract class MovableObject extends GameObject {

    // ... (Toàn bộ code bên trong giữ nguyên) ...
    protected double dx;
    protected double dy;

    public MovableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.dx = 0;
        this.dy = 0;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    protected void move() {
        x += dx;
        y += dy;
    }

    @Override
    public abstract void update();

    @Override
    public abstract void render(GraphicsContext gc);
}