package com.sotiemgiat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sotiemgiat.di.AppContainer
import com.sotiemgiat.presentation.orders.CreateOrderScreen
import com.sotiemgiat.presentation.orders.OrdersScreen
import com.sotiemgiat.presentation.orders.OrdersViewModel
import java.time.LocalTime

@Composable
fun SoGiatApp() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showCreateOrder by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val appContainer = remember { AppContainer(context) }

    if (showCreateOrder) {
        Scaffold { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                CreateOrderScreen(
                    onSaved = { showCreateOrder = false },
                    appContainer = appContainer
                )
            }
        }
        return
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf("Tổng quan", "Đơn hàng", "Khách hàng", "Cài đặt")
                val icons = listOf(
                    Icons.Default.ReceiptLong,
                    Icons.Default.ReceiptLong,
                    Icons.Default.People,
                    Icons.Default.Settings
                )
                items.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(icons[index], contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 0 || selectedTab == 1) {
                FloatingActionButton(onClick = { showCreateOrder = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Tạo đơn")
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> DashboardScreen(padding, onCreateOrder = { showCreateOrder = true })
            1 -> OrdersTab(appContainer)
            2 -> SimpleScreen(padding, "Khách hàng", "Danh sách khách hàng sẽ được xây dựng ở bước tiếp theo.")
            else -> SimpleScreen(padding, "Cài đặt", "Cấu hình dịch vụ, sao lưu và khôi phục sẽ được xây dựng ở bước tiếp theo.")
        }
    }
}

@Composable
private fun OrdersTab(appContainer: AppContainer) {
    val viewModel: OrdersViewModel = viewModel(
        factory = OrdersViewModel.factory(appContainer.orderRepository)
    )
    OrdersScreen(viewModel)
}

@Composable
private fun DashboardScreen(padding: PaddingValues, onCreateOrder: () -> Unit) {
    val hour = LocalTime.now().hour
    val greeting = when (hour) {
        in 5..11 -> "Chào buổi sáng"
        in 12..17 -> "Chào buổi chiều"
        else -> "Chào buổi tối"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(greeting)
        Text("Sẵn sàng quản lý tiệm giặt hôm nay?")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard("Đơn hôm nay", "0", Modifier.weight(1f))
            SummaryCard("Chưa thanh toán", "0", Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard("Đang xử lý", "0", Modifier.weight(1f))
            SummaryCard("Chờ nhận", "0", Modifier.weight(1f))
        }

        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onCreateOrder,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text("Tạo đơn giặt mới")
        }
    }
}

@Composable
private fun SummaryCard(title: String, value: String, modifier: Modifier) {
    Card(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(title)
            Text(value)
        }
    }
}

@Composable
private fun SimpleScreen(padding: PaddingValues, title: String, description: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(title)
        Text(description)
    }
}
