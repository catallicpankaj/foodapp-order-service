package com.foodapp.order.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.foodapp.order.rabbitmq.RabbitMQConsumer;

@Configuration
public class RabbitMQConfig {

	@Value("${foodapp.rabbitmq.order.queue}")
	private String queueName;

	@Value("${foodapp.rabbitmq.order.queue.update}")
	private String updateQueueName;
	
	@Value("${foodapp.rabbitmq.exchange.direct}")
	private String directExchange;

	@Value("${foodapp.rabbitmq.exchange.topic}")
	private String topicExchange;
	
	@Value("${foodapp.rabbitmq.routingkey}")
	private String routingkey;
	
	@Bean
	public Queue orderQueue() {
		return new Queue(this.queueName, false);
	}
	
	@Bean
	public Queue orderUpdateQueue() {
		return new Queue(this.updateQueueName, false);
	}

	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(this.directExchange);
	}

	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(this.topicExchange);
	}
	
	@Bean
	public Binding orderQueueBinding(Queue orderQueue, DirectExchange exchange) {
		return BindingBuilder.bind(orderQueue).to(exchange).with(this.routingkey);
	}
	
	@Bean
	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		simpleMessageListenerContainer.setQueues(this.orderUpdateQueue());
		simpleMessageListenerContainer.setMessageListener(rabbitMQConsumer());
		return simpleMessageListenerContainer;
	}
	
	@Bean("rabbitMQConsumer")
	public RabbitMQConsumer rabbitMQConsumer() {
		return new RabbitMQConsumer(); 
	}
	
	
	
//	@Bean
//	ConnectionFactory connectionFactory() {
//		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost");
//		cachingConnectionFactory.setUsername(username);
//		cachingConnectionFactory.setUsername(password);
//		return cachingConnectionFactory;
//	}
//	@Bean
//	MessageListenerContainer messageListenerContainer() {
//		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
//		simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
//		simpleMessageListenerContainer.setQueues(queue());
//		simpleMessageListenerContainer.setMessageListener(new RabbitMQListner());
//		return simpleMessageListenerContainer;
//
//	}
}
