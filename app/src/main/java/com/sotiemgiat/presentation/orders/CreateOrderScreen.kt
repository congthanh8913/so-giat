package com.sotiemgiat.presentation.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sotiemgiat.di.AppContainer
import java.time.LocalDate

@Composable
fun CreateOrderScreen(
    onSaved: () -> Unit,
    appContainer: AppContainer,
    viewModel: CreateOrderViewModel = viewModel(factory = CreateOrderViewModel.factory(appContainer))
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.saved) {
        if (state.saved) {
            viewModel.resetSaved()
            onSaved()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Text("Tạo đơn giặt mới") }
        item {
            OutlinedTextField(
                value = state.customerName,
                onValueChange = viewModel::updateCustomerName,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Tên khách hàng *") },
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = state.customerPhone,
                onValueChange = viewModel::updateCustomerPhone,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Số điện thoại") },
                singleLine = true
            )
        }
        item { Text("Dịch vụ") }
        items(state.services) { service ->
            Card(onClick = { viewModel.selectService(service.id) }) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    RadioButton(
                        selected = service.id == state.selectedServiceId,
                        onClick = { viewModel.selectService(service.id) }
                    )
                    Column {
                        Text(service.name)
                        Text("${service.price}đ / ${service.unit}")
                    }
                }
            }
        }
        item {
            OutlinedTextField(
                value = state.quantity,
                onValueChange = viewModel::updateQuantity,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Số lượng") },
                singleLine = true
            )
        }
        item {
            Text("Hẹn trả: ${state.dueDate}")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.setDueDate(LocalDate.now()) }) { Text("Hôm nay") }
                Button(onClick = { viewModel.setDueDate(LocalDate.now().plusDays(1)) }) { Text("Ngày mai") }
            }
        }
        item {
            HorizontalDivider()
            Text("Tổng tiền: ${state.total}đ")
        }
        item {
            OutlinedTextField(
                value = state.paidAmount,
                onValueChange = viewModel::updatePaidAmount,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Khách trả") },
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = state.note,
                onValueChange = viewModel::updateNote,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ghi chú") },
                minLines = 2
            )
        }
        state.error?.let { error -> item { Text(error) } }
        item {
            Button(
                onClick = viewModel::saveOrder,
                enabled = !state.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isSaving) "Đang lưu..." else "Lưu đơn")
            }
        }
    }
}
