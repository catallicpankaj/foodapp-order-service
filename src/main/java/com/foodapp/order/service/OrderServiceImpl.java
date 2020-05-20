package com.foodapp.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.foodapp.order.dto.Order;
import com.foodapp.order.rabbitmq.OrderQueueMessage;
import com.foodapp.order.rabbitmq.RabbitMQPublisher;
import com.foodapp.order.repo.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

	private static final String ORDER_CREATED = "ORDER_CREATED";

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	RabbitMQPublisher rabbitMQPublisher;
	
	@Value("${rest.paymentsvc.url}")
	String paymentSvcUrl;

	@Override
	public void createOrder(Order order) {
		orderRepository.save(order);
		createEventOnQueue(order);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public Order getOrder(String orderId) {
		return orderRepository.findById(orderId).get();
	}

	private void createEventOnQueue(Order order) {
		OrderQueueMessage message = new OrderQueueMessage();
		message.setOrderId(order.getOrderId());
		message.setCustomerId(order.getCustomerId());
		message.setTotalPaymentRequired(order.getTotalPrice());
		message.setOrderStatus(ORDER_CREATED);
		rabbitMQPublisher.send(message);
	}

	@Override
	public List<Order> getAllOrdersForCustomerId(String customerId, String orderStatus) {
		return orderRepository.getAllOrdersForCustomerId(customerId, orderStatus);
	}
	
}
