package com.example.messagingredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@SpringBootApplication
public class MessagingRedisApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessagingRedisApplication.class);

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
	                                        MessageListenerAdapter listenerAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic("chat2"));
		container.addMessageListener(listenerAdapter, new PatternTopic("chat3"));

		return container;
	}

	@Bean
	RedisMessageListenerContainer containers(RedisConnectionFactory connectionFactory,
	                                        MessageListenerAdapter listenerAdapters) {

		RedisMessageListenerContainer containers = new RedisMessageListenerContainer();
		containers.setConnectionFactory(connectionFactory);
		containers.addMessageListener(listenerAdapters, new PatternTopic("chat2"));
		containers.addMessageListener(listenerAdapters, new PatternTopic("chat3"));

		return containers;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	MessageListenerAdapter listenerAdapters(Receivers receivers) {
		return new MessageListenerAdapter(receivers, "receiveMessage");
	}
	
	@Bean
	Receiver receiver() {
		return new Receiver();
	}

	@Bean
	Receivers receivers() {
		return new Receivers();
	}

	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

	public static void main(String[] args) throws InterruptedException {

		ApplicationContext ctx = SpringApplication.run(MessagingRedisApplication.class, args);

		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
		Receiver receiver = ctx.getBean(Receiver.class);
		Receivers receivers = ctx.getBean(Receivers.class);

		while (receiver.getCount() == 0) {

			LOGGER.info("Sending message...");
			template.convertAndSend("chat", "Hello from Redis!");
			template.convertAndSend("chat2", "Hello from Redis!");
			template.convertAndSend("chat3", "Hello from Redis!");
			Thread.sleep(500L);
		}

		System.exit(0);
	}
}
