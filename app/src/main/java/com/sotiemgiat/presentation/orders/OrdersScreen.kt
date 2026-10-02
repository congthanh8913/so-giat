package com.sotiemgiat.presentation.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sotiemgiat.data.local.model.OrderListItem
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(viewModel: OrdersViewModel) {
    val orders by viewModel.orders.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Đơn hàng", style = MaterialTheme.typography.headlineSmall)

        if (orders.isEmpty()) {
            Text("Chưa có đơn hàng nào.")
            Text("Hãy tạo một đơn mới để bắt đầu.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(orders, key = { it.id }) { order ->
                    OrderCard(order)
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: OrderListItem) {
    val money = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("vi", "VN"))
    val debt = (order.total - order.paidAmount).coerceAtLeast(0)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(order.orderNumber, style = MaterialTheme.typography.titleMedium)
                Text(
                    "  " + statusLabel(order.status),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Text(order.customerName, style = MaterialTheme.typography.titleMedium)
            if (order.customerPhone.isNotBlank()) {
                Text(order.customerPhone)
            }
            Text("Nhận: " + date.format(Date(order.receivedAt)))
            order.dueAt?.let { Text("Hẹn: " + date.format(Date(it))) }
            Text("Tổng: " + money.format(order.total) + "đ")
            Text("Đã trả: " + money.format(order.paidAmount) + "đ")
            Text(
                if (debt > 0) "Còn nợ: " + money.format(debt) + "đ"
                else "Đã thanh toán đủ"
            )
        }
    }
}

private fun statusLabel(status: String): String = when (status) {
    "RECEIVED" -> "Mới nhận"
    "PROCESSING" -> "Đang xử lý"
    "READY" -> "Sẵn sàng"
    "DELIVERED" -> "Đã giao"
    "CANCELLED" -> "Đã hủy"
    else -> status
}
