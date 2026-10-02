package com.sotiemgiat.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sotiemgiat.data.local.model.OrderListItem
import com.sotiemgiat.domain.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class OrdersViewModel(
    orderRepository: OrderRepository
) : ViewModel() {
    val orders: StateFlow<List<OrderListItem>> =
        orderRepository.observeOrderList()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    companion object {
        fun factory(repository: OrderRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    OrdersViewModel(repository) as T
            }
    }
}
