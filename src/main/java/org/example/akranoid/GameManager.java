package org.example.akranoid;

public class GameManager {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    private Paddle paddle;

    public GameManager() {
        paddle = new Paddle(350, 550, 100, 20, 10.0, SCREEN_WIDTH); // truyền SCREEN_WIDTH vào khi khởi tạo paddle
    }

    public void updateGame() {
        paddle.update();
    }
}
