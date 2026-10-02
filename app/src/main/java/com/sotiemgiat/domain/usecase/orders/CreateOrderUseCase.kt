package com.sotiemgiat.domain.usecase.orders

import com.sotiemgiat.data.local.entity.OrderEntity
import com.sotiemgiat.data.local.entity.OrderItemEntity
import com.sotiemgiat.data.local.entity.PaymentEntity
import com.sotiemgiat.data.local.entity.ServiceEntity
import com.sotiemgiat.domain.repository.CustomerRepository
import com.sotiemgiat.domain.repository.OrderRepository
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

data class CreateOrderRequest(
    val customerName: String,
    val customerPhone: String,
    val service: ServiceEntity,
    val quantity: Double,
    val paidAmount: Long,
    val dueDate: LocalDate,
    val note: String
)

class CreateOrderUseCase(
    private val customerRepository: CustomerRepository,
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(request: CreateOrderRequest): Long {
        val phone = request.customerPhone.trim()
        val customerId = if (phone.isNotBlank()) {
            customerRepository.findByPhone(phone)?.id
                ?: customerRepository.addCustomer(request.customerName.trim(), phone)
        } else {
            customerRepository.addCustomer(request.customerName.trim(), "")
        }

        val total = (request.quantity * request.service.price).toLong()
        val now = System.currentTimeMillis()

        return orderRepository.createOrder(
            order = OrderEntity(
                orderNumber = "SG-${UUID.randomUUID().toString().take(8).uppercase()}",
                customerId = customerId,
                receivedAt = now,
                dueAt = request.dueDate.atTime(23, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                completedAt = null,
                deliveredAt = null,
                status = "RECEIVED",
                subtotal = total,
                total = total,
                paidAmount = request.paidAmount,
                note = request.note.trim()
            ),
            items = listOf(
                OrderItemEntity(
                    orderId = 0,
                    serviceId = request.service.id,
                    serviceNameSnapshot = request.service.name,
                    quantity = request.quantity,
                    unitPrice = request.service.price,
                    total = total
                )
            ),
            payment = request.paidAmount.takeIf { it > 0 }?.let { PaymentEntity(orderId = 0, amount = it) }
        )
    }
}
