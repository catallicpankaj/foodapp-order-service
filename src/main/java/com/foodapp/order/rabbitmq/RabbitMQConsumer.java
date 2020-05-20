package com.foodapp.order.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodapp.order.dto.Order;
import com.foodapp.order.repo.OrderRepository;


public class RabbitMQConsumer implements MessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	@Qualifier("jacksonObjectMapper")
	private ObjectMapper objectMapper;
	
	@Override
	public void onMessage(Message message) {
		String messageReceived = new String(message.getBody());
		LOGGER.info("Message Received on ORDER_UPDATE_QUEUE - {}", messageReceived);
		
		OrderQueueMessage messageOnOrderQueue = null;
		try {
			LOGGER.info("ObjectMapper is:"+objectMapper);
			messageOnOrderQueue = this.objectMapper.readValue(messageReceived, OrderQueueMessage.class);
			
		} catch (JsonProcessingException e) {
			LOGGER.error("Exception occured while processing listened message");
			e.printStackTrace();
		}
		
		Order existingOrder = orderRepository.findById(messageOnOrderQueue.getOrderId()).get();
		existingOrder.setOrderStatus(messageOnOrderQueue.getOrderStatus());
		orderRepository.save(existingOrder);
	}

}
