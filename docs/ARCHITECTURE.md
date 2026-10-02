# Sổ Giặt — Architecture

## Package

`com.sotiemgiat`

## Layers

- data/local/database
- data/local/dao
- data/local/entity
- data/repository
- domain/model
- domain/repository
- domain/usecase
- presentation/dashboard
- presentation/orders
- presentation/customers
- presentation/payments
- presentation/settings
- navigation
- ui/components
- ui/theme
- ui/icons
- util

## Architecture rules

1. UI không truy cập Room trực tiếp.
2. Repository là ranh giới giữa data và domain/presentation.
3. ViewModel giữ UI state bằng StateFlow.
4. Room là nguồn dữ liệu cục bộ chính.
5. DataStore dành cho settings/preferences, không dùng thay Room cho dữ liệu nghiệp vụ.
6. Mọi thao tác tạo/cập nhật đơn phải giữ tính nhất quán giữa Order, OrderItem và Payment.
7. Không thêm backend hoặc authentication nếu chưa có yêu cầu sản phẩm rõ ràng.

## UI principles

- Ít màn hình.
- Chữ và nút đủ lớn.
- Một thao tác chính trên mỗi màn hình.
- Ưu tiên tốc độ nhập đơn.
- Không dùng bảng dữ liệu phức tạp trong MVP.
- Trạng thái phải dễ nhận biết.
