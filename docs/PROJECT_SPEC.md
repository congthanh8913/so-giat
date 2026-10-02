# Sổ Giặt — Product Specification

## Product statement

Một cuốn sổ điện tử giúp chủ tiệm không quên đơn, không quên khách và không quên tiền.

## Target

Chủ tiệm giặt nhỏ cần một công cụ đơn giản thay cho sổ giấy.

## Navigation

- Dashboard
- Orders
- Customers
- Settings

Nút `+` nổi bật để tạo đơn mới.

## Greeting

Theo giờ thiết bị:

- 05:00–11:59: Chào buổi sáng
- 12:00–17:59: Chào buổi chiều
- 18:00–04:59: Chào buổi tối

Câu phụ có thể thay đổi theo ngữ cảnh.

## Order flow

RECEIVED → PROCESSING → READY → DELIVERED

Ngoài ra: CANCELLED.

## Data model

### Customer

- id
- name
- phone
- address
- note
- createdAt
- updatedAt

### Service

- id
- name
- pricingType
- price
- unit
- isActive
- createdAt
- updatedAt

Pricing types: per item, per kg, fixed.

### Order

- id
- orderNumber
- customerId
- receivedAt
- dueAt
- completedAt
- deliveredAt
- status
- subtotal
- discount
- total
- paidAmount
- note
- createdAt
- updatedAt

### OrderItem

- id
- orderId
- serviceId
- serviceNameSnapshot
- quantity
- unitPrice
- total
- note

### Payment

- id
- orderId
- amount
- method
- paidAt
- note

## MVP exclusions

Không triển khai trong MVP:

- AI
- GPS
- nhân viên
- đa chi nhánh
- giao hàng
- barcode/QR
- web admin
- server/backend
- Google login
- realtime sync
- POS/kế toán/hóa đơn điện tử
- CRM/loyalty/marketplace

## Product metric

North-star metric: số đơn được quản lý thành công mỗi ngày.

Mục tiêu thử nghiệm ban đầu: 10 tiệm cài → 5 tiệm dùng hằng ngày → 3 tiệm duy trì 30 ngày → 1 tiệm sẵn sàng trả phí.
