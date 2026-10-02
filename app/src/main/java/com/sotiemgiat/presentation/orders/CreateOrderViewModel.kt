package com.sotiemgiat.presentation.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sotiemgiat.data.local.database.DatabaseProvider
import com.sotiemgiat.data.local.entity.CustomerEntity
import com.sotiemgiat.data.local.entity.OrderEntity
import com.sotiemgiat.data.local.entity.OrderItemEntity
import com.sotiemgiat.data.local.entity.PaymentEntity
import com.sotiemgiat.data.local.entity.ServiceEntity
import com.sotiemgiat.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID
import kotlin.math.roundToLong

data class CreateOrderUiState(
    val customerName: String = "",
    val customerPhone: String = "",
    val services: List<ServiceEntity> = emptyList(),
    val selectedServiceId: Long? = null,
    val quantity: String = "1",
    val paidAmount: String = "0",
    val dueDate: LocalDate = LocalDate.now(),
    val note: String = "",
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
) {
    val selectedService: ServiceEntity?
        get() = services.firstOrNull { it.id == selectedServiceId }

    val total: Long
        get() = ((quantity.toDoubleOrNull() ?: 0.0) * (selectedService?.price ?: 0L)).roundToLong()
}

class CreateOrderViewModel(application: Application) : AndroidViewModel(application) {
    private val database = DatabaseProvider.get(application)
    private val repository = OrderRepository(database)

    private val _uiState = MutableStateFlow(CreateOrderUiState())
    val uiState: StateFlow<CreateOrderUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            seedServicesIfNeeded()
            repository.observeServices().collect { services ->
                _uiState.value = _uiState.value.copy(
                    services = services,
                    selectedServiceId = _uiState.value.selectedServiceId ?: services.firstOrNull()?.id
                )
            }
        }
    }

    fun updateCustomerName(value: String) { update { copy(customerName = value, error = null) } }
    fun updateCustomerPhone(value: String) { update { copy(customerPhone = value, error = null) } }
    fun updateQuantity(value: String) { update { copy(quantity = value, error = null) } }
    fun updatePaidAmount(value: String) { update { copy(paidAmount = value, error = null) } }
    fun updateNote(value: String) { update { copy(note = value, error = null) } }
    fun selectService(id: Long) { update { copy(selectedServiceId = id, error = null) } }
    fun setDueDate(date: LocalDate) { update { copy(dueDate = date) } }

    fun saveOrder() {
        val state = _uiState.value
        val service = state.selectedService
        val quantity = state.quantity.toDoubleOrNull()
        val paid = state.paidAmount.toLongOrNull() ?: 0L

        if (state.customerName.isBlank()) {
            update { copy(error = "Vui lòng nhập tên khách hàng.") }
            return
        }
        if (service == null) {
            update { copy(error = "Vui lòng chọn dịch vụ.") }
            return
        }
        if (quantity == null || quantity <= 0) {
            update { copy(error = "Số lượng phải lớn hơn 0.") }
            return
        }
        if (paid < 0 || paid > state.total) {
            update { copy(error = "Tiền khách trả không hợp lệ.") }
            return
        }

        viewModelScope.launch {
            update { copy(isSaving = true, error = null) }
            runCatching {
                val now = System.currentTimeMillis()
                val phone = state.customerPhone.trim()
                val customerId = if (phone.isNotBlank()) {
                    database.customerDao().getByPhone(phone)?.id
                        ?: database.customerDao().insert(
                            CustomerEntity(
                                name = state.customerName.trim(),
                                phone = phone
                            )
                        )
                } else {
                    database.customerDao().insert(
                        CustomerEntity(
                            name = state.customerName.trim(),
                            phone = ""
                        )
                    )
                }
                repository.createOrder(
                    order = OrderEntity(
                        orderNumber = "SG-${UUID.randomUUID().toString().take(8).uppercase()}",
                        customerId = customerId,
                        receivedAt = now,
                        dueAt = state.dueDate.atTime(23, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        completedAt = null,
                        deliveredAt = null,
                        status = "RECEIVED",
                        subtotal = state.total,
                        total = state.total,
                        paidAmount = paid,
                        note = state.note.trim()
                    ),
                    items = listOf(
                        OrderItemEntity(
                            orderId = 0,
                            serviceId = service.id,
                            serviceNameSnapshot = service.name,
                            quantity = quantity,
                            unitPrice = service.price,
                            total = state.total
                        )
                    ),
                    payment = if (paid > 0) PaymentEntity(orderId = 0, amount = paid) else null
                )
            }.onSuccess {
                update { copy(isSaving = false, saved = true) }
            }.onFailure { error ->
                update { copy(isSaving = false, error = error.message ?: "Không thể lưu đơn.") }
            }
        }
    }

    fun resetSaved() {
        update {
            CreateOrderUiState(
                services = _uiState.value.services,
                selectedServiceId = _uiState.value.services.firstOrNull()?.id
            )
        }
    }

    private suspend fun seedServicesIfNeeded() {
        if (database.serviceDao().count() == 0) {
            listOf(
                ServiceEntity(name = "Giặt sấy", price = 15000),
                ServiceEntity(name = "Giặt chăn", price = 50000),
                ServiceEntity(name = "Sấy", price = 20000),
                ServiceEntity(name = "Ủi", price = 10000)
            ).forEach { database.serviceDao().insert(it) }
        }
    }

    private inline fun update(transform: CreateOrderUiState.() -> CreateOrderUiState) {
        _uiState.value = _uiState.value.transform()
    }
}
