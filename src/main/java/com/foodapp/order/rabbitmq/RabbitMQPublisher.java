package com.foodapp.order.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RabbitMQPublisher {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQPublisher.class);

	@Autowired
	private AmqpTemplate rabbitTemplate;
	
	@Value("${foodapp.rabbitmq.exchange.direct}")
	private String directExchange;

	
	@Value("${foodapp.rabbitmq.routingkey}")
	private String routingkey;
	
	@Autowired
	@Qualifier("jacksonObjectMapper")
	private ObjectMapper objectMapper;
	
	public void send(OrderQueueMessage message) {
		rabbitTemplate.convertAndSend(this.directExchange, this.routingkey, message);
		try {
			System.out.println("Message updated to OrderQueue" + objectMapper.writeValueAsString(message));
		} catch (JsonProcessingException e) {
			LOGGER.error("Json processing of published message on OrderQueue failed");
		}
	}
	
}
