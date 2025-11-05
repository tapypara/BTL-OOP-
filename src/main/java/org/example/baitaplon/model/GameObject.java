// <<< SỬA DÒNG PACKAGE >>>
package org.example.baitaplon.model;

import javafx.scene.canvas.GraphicsContext;

/**
 * Lớp trừu tượng đại diện cho mọi đối tượng trong game.
 * ...
 */
public abstract class GameObject {
    // ... (Toàn bộ code bên trong giữ nguyên) ...
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public abstract void update();
    public abstract void render(GraphicsContext gc);
}