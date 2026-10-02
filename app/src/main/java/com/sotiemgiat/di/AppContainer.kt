package com.sotiemgiat.di

import android.content.Context
import com.sotiemgiat.data.local.database.DatabaseProvider
import com.sotiemgiat.data.repository.CustomerRepositoryImpl
import com.sotiemgiat.data.repository.OrderRepositoryImpl
import com.sotiemgiat.data.repository.ServiceRepositoryImpl
import com.sotiemgiat.domain.repository.CustomerRepository
import com.sotiemgiat.domain.repository.OrderRepository
import com.sotiemgiat.domain.repository.ServiceRepository
import com.sotiemgiat.domain.usecase.orders.CreateOrderUseCase

class AppContainer(context: Context) {
    private val database = DatabaseProvider.get(context)

    val customerRepository: CustomerRepository = CustomerRepositoryImpl(database.customerDao())
    val serviceRepository: ServiceRepository = ServiceRepositoryImpl(database.serviceDao())
    val orderRepository: OrderRepository = OrderRepositoryImpl(database)

    val createOrderUseCase: CreateOrderUseCase =
        CreateOrderUseCase(customerRepository, orderRepository)
}
