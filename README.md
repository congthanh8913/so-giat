# Sổ Giặt

Ứng dụng Android quản lý tiệm giặt nhỏ, ưu tiên đơn giản, nhanh và offline-first.

## MVP

- Dashboard: đơn hôm nay, doanh thu, đang xử lý, chờ nhận, chưa thanh toán.
- Tạo đơn trong khoảng 15–30 giây.
- Quản lý khách hàng.
- Quản lý dịch vụ và giá.
- Trạng thái đơn: RECEIVED → PROCESSING → READY → DELIVERED; có CANCELLED.
- Thanh toán và lịch sử thanh toán.
- Tìm kiếm theo tên khách, số điện thoại, mã đơn.
- Sao lưu và khôi phục dữ liệu cục bộ.
- Thông báo cục bộ cho các đơn cần chú ý.

## Công nghệ

Kotlin + Jetpack Compose + Material 3 + Room + ViewModel + StateFlow + Navigation Compose + DataStore + WorkManager.

## Nguyên tắc

Offline-first. Không cần tài khoản, server, AI, GPS, nhân viên, đa chi nhánh hoặc đồng bộ cloud trong MVP.

Xem đặc tả chi tiết tại `docs/PROJECT_SPEC.md`.
