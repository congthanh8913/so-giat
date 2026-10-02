package com.sotiemgiat.presentation.orders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sotiemgiat.data.local.entity.ServiceEntity
import com.sotiemgiat.di.AppContainer
import com.sotiemgiat.domain.usecase.orders.CreateOrderRequest
import com.sotiemgiat.domain.usecase.orders.CreateOrderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
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
    private val container = AppContainer(application)
    private val serviceRepository = container.serviceRepository
    private val createOrderUseCase: CreateOrderUseCase = container.createOrderUseCase

    private val _uiState = MutableStateFlow(CreateOrderUiState())
    val uiState: StateFlow<CreateOrderUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            seedServicesIfNeeded()
        }
        viewModelScope.launch {
            serviceRepository.observeActiveServices().collect { services ->
                _uiState.value = _uiState.value.copy(
                    services = services,
                    selectedServiceId = _uiState.value.selectedServiceId
                        ?: services.firstOrNull()?.id
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
    fun setDueDate(date: LocalDate) { update { copy(dueDate = date, error = null) } }

    fun saveOrder() {
        val state = _uiState.value
        val service = state.selectedService
        val quantity = state.quantity.toDoubleOrNull()
        val paid = state.paidAmount.toLongOrNull() ?: 0L

        when {
            state.customerName.isBlank() ->
                update { copy(error = "Vui lòng nhập tên khách hàng.") }
            service == null ->
                update { copy(error = "Vui lòng chọn dịch vụ.") }
            quantity == null || quantity <= 0 ->
                update { copy(error = "Số lượng phải lớn hơn 0.") }
            paid < 0 || paid > state.total ->
                update { copy(error = "Tiền khách trả không hợp lệ.") }
            else -> saveValidOrder(state, service, quantity, paid)
        }
    }

    private fun saveValidOrder(
        state: CreateOrderUiState,
        service: ServiceEntity,
        quantity: Double,
        paid: Long
    ) {
        viewModelScope.launch {
            update { copy(isSaving = true, error = null) }
            runCatching {
                createOrderUseCase(
                    CreateOrderRequest(
                        customerName = state.customerName,
                        customerPhone = state.customerPhone,
                        service = service,
                        quantity = quantity,
                        paidAmount = paid,
                        dueDate = state.dueDate,
                        note = state.note
                    )
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
        if (serviceRepository.count() == 0) {
            listOf(
                ServiceEntity(name = "Giặt sấy", price = 15000),
                ServiceEntity(name = "Giặt chăn", price = 50000),
                ServiceEntity(name = "Sấy", price = 20000),
                ServiceEntity(name = "Ủi", price = 10000)
            ).forEach { service ->\n                serviceRepository.addService(service)\n            }
        }
    }

    private inline fun update(transform: CreateOrderUiState.() -> CreateOrderUiState) {
        _uiState.value = _uiState.value.transform()
    }
}
