# BTL-OOP: Game Arkanoid

Dựa trên trò chơi Arkanoid cổ điển, được xây dựng bằng Java và JavaFX.

Người chơi điều khiển một thanh đỡ để hứng một quả bóng, làm nó nảy lên để phá hủy các viên gạch ở phía trên màn hình.

## Tính năng chính

Dự án này được xây dựng tuân thủ các nguyên tắc OOP, tách biệt rõ ràng các thành phần logic (model), điều khiển (controller - `MainApplication`, `GameManager`), và giao diện (UI).

* **Lối chơi Arkanoid cốt lõi:**
    * Điều khiển thanh đỡ (Paddle) sang trái/phải.
    * Bóng nảy vật lý khi va chạm với tường và thanh đỡ.
    * Bóng "dính" vào thanh đỡ khi bắt đầu hoặc khi mất mạng, cho phép người chơi chọn thời điểm bắn.
    * Logic nảy bóng: Góc nảy của bóng phụ thuộc vào vị trí va chạm trên thanh đỡ (nảy mạnh hơn ở rìa).

* **Hệ thống Gạch (Bricks):**
    * **Gạch nhiều HP:** Các viên gạch (có nhiều màu) cần nhiều lần va chạm để phá hủy. Chúng sẽ tự động "giảm cấp" (thay đổi hình ảnh) sau mỗi lần bị bắn trúng.
    * **Gạch bất tử (Unbreakable):** Gạch màu xám không thể bị phá hủy.
    * **Gạch Power-up:** Các loại gạch đặc biệt sẽ vỡ ngay lập tức khi va chạm.

* **Quản lý Màn chơi (Level):**
    * Hỗ trợ nhiều màn chơi khác nhau.
    * Các màn chơi được thiết kế và tải động từ các file `.txt` trong thư mục `resources/levels/`.
    * Game tự động chuyển sang màn chơi tiếp theo khi tất cả gạch (trừ gạch bất tử) bị phá hủy.

* **Giao diện người dùng (UI) và Trạng thái game:**
    * **Màn hình Menu:** Cho phép bắt đầu, thoát game và bật/tắt âm thanh.
    * **Màn hình Tạm dừng:** Nhấn `ESCAPE` để tạm dừng game và tiếp tục.
    * **Màn hình Game Over:** Hiển thị khi hết mạng, cho phép chơi lại và quay về menu chính.

* **Điểm số và Mạng sống:**
    * Hệ thống tính điểm (`StatManager`) khi phá vỡ các loại gạch khác nhau.
    * Hiển thị số mạng sống bằng hình ảnh trái tim.
    * Người chơi bị trừ mạng khi bóng rơi xuống đáy, và game over khi hết mạng.

* **Âm thanh:**
    * Nhạc nền (background music) chạy lặp lại.
    * Hiệu ứng âm thanh (SFX) khi bóng va chạm thanh đỡ và gạch.
    * Có thể bật/tắt nhạc nền từ màn hình Menu.

* **Các Power-up:**
    * Làm bóng to lên và chậm đi
    * Làm bóng nhỏ đi và nhanh lên
    * Làm thanh đỡ dài ra
    * Thêm mạng sống cho người chơi
    * Làm viên gạch nổ lan sang các viên gạch gần nó

## Cách chạy game

Dự án được quản lý bằng Maven.

1.  Clone repository.
2.  Mở dự án bằng IntelliJ IDEA hoặc IDE hỗ trợ Maven.
3.  Chờ Maven tải về các thư viện (dependencies) của JavaFX.
4.  Chạy lớp `MainApplication.java`.

## Thành viên nhóm

* Vũ Quang Huy - MSV 24022804
* Trần Anh Khoa - MSV 24022806
* Cao Nguyễn Lâm - MSV 24022808
