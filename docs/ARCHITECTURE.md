# Sổ Giặt — Architecture

## Mục tiêu

Sổ Giặt dùng kiến trúc **feature-oriented + layered architecture**: mỗi tính năng có khu vực riêng ở presentation, còn dữ liệu và nghiệp vụ dùng các lớp chung.

Mục tiêu của cấu trúc này là:
- Dễ tìm code theo chức năng.
- UI không phụ thuộc trực tiếp vào Room.
- Nghiệp vụ quan trọng có thể kiểm thử độc lập.
- Có thể tách feature thành Gradle module sau này mà không phải viết lại toàn bộ app.
- MVP vẫn giữ số lượng module Gradle nhỏ để tránh over-engineering.

## Package

`com.sotiemgiat`

## Cấu trúc hiện tại

```
com.sotiemgiat/
├── data/
│   ├── local/
│   │   ├── database/          # Room database + migrations
│   │   ├── dao/               # Chỉ giao tiếp với SQLite/Room
│   │   └── entity/            # Cấu trúc lưu trữ
│   └── repository/            # Implementation của repository
│
├── domain/
│   ├── repository/            # Contract nghiệp vụ, không biết UI
│   └── usecase/
│       └── orders/             # Nghiệp vụ theo từng feature
│
├── presentation/
│   ├── dashboard/
│   ├── orders/
│   ├── customers/
│   ├── payments/
│   └── settings/
│
├── di/                        # Nơi ghép dependency của app
├── ui/                        # App shell, theme và UI dùng chung
├── navigation/                # Dành cho navigation graph khi app mở rộng
└── util/                      # Utility thuần dùng chung
```

## Luồng phụ thuộc

```
Screen
  ↓
ViewModel
  ↓
UseCase
  ↓
Domain Repository (interface)
  ↓
Data Repository (implementation)
  ↓
DAO
  ↓
Room Database
```

Ví dụ tạo đơn:

```
CreateOrderScreen
      ↓
CreateOrderViewModel
      ↓
CreateOrderUseCase
      ├── CustomerRepository
      └── OrderRepository
              ↓
       Room / SQLite
```

ViewModel **không được truy cập DAO hoặc Room trực tiếp**.

## Ranh giới feature

### Orders

Chịu trách nhiệm:
- Tạo đơn.
- Danh sách đơn.
- Chi tiết đơn.
- Cập nhật trạng thái.
- Hoàn thành/giao đơn.

### Customers

Chịu trách nhiệm:
- Danh sách khách.
- Tìm kiếm khách.
- Chi tiết khách.
- Lịch sử đơn của khách.

### Payments

Chịu trách nhiệm:
- Ghi nhận thanh toán.
- Lịch sử thanh toán.
- Số tiền đã thu và còn nợ.

### Dashboard

Chỉ tổng hợp dữ liệu để hiển thị:
- Đơn hôm nay.
- Doanh thu.
- Đang xử lý.
- Chờ nhận.
- Chưa thanh toán.
- Quá hạn.

Dashboard không sở hữu dữ liệu nghiệp vụ riêng.

### Settings

Chịu trách nhiệm:
- Cấu hình dịch vụ.
- Thiết lập ứng dụng.
- Sao lưu/khôi phục.
- Các tùy chọn người dùng.

## Dependency Injection

Hiện tại MVP dùng `AppContainer` để tập trung việc khởi tạo:
- Database.
- Repository implementations.
- Use cases.

Chưa thêm Hilt/Dagger để giữ MVP nhẹ. Nếu dependency graph lớn lên, có thể chuyển `AppContainer` sang Hilt mà không thay đổi ranh giới feature.

## Quy tắc dữ liệu

1. Room là nguồn dữ liệu nghiệp vụ cục bộ chính.
2. DataStore chỉ dành cho settings/preferences.
3. Entity là mô hình lưu trữ; không dùng Entity làm nơi chứa logic UI.
4. Repository implementation nằm trong `data/repository`.
5. Domain repository chỉ là contract mà ViewModel/UseCase cần.
6. Các thao tác tạo đơn phải đảm bảo Order + OrderItem + Payment được ghi trong cùng transaction.
7. Không thêm backend/auth/sync khi chưa có yêu cầu sản phẩm rõ ràng.

## Quy tắc mở rộng

Khi một feature trở nên lớn:

```
presentation/orders/
domain/usecase/orders/
data/...
```

có thể được gom thành:

```
:feature-orders
```

Tương tự với Customers, Payments, Dashboard và Settings.

**Không tách Gradle module chỉ vì có một màn hình.** Chỉ tách khi feature có đủ kích thước hoặc cần ranh giới build/dependency riêng.

## UI principles

- Ít màn hình.
- Chữ và nút đủ lớn.
- Một thao tác chính trên mỗi màn hình.
- Ưu tiên tốc độ nhập đơn.
- Không dùng bảng dữ liệu phức tạp trong MVP.
- Trạng thái phải dễ nhận biết.
