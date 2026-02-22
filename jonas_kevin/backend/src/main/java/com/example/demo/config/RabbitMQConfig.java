package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String EXCHANGE_NAME = "shiggy.exchange";
    public static final String QUEUE_NAME = "shiggy.events.queue";
    public static final String ROUTING_KEY = "shiggy.events";
    
    @Bean
    public Queue shiggyEventsQueue() {
        return new Queue(QUEUE_NAME, true);
    }
    
    @Bean
    public TopicExchange shiggyExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
    
    @Bean
    public Binding binding(Queue shiggyEventsQueue, TopicExchange shiggyExchange) {
        return BindingBuilder
            .bind(shiggyEventsQueue)
            .to(shiggyExchange)
            .with(ROUTING_KEY);
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setClassMapper(converter.getJavaTypeMapper());
        return converter;
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter(objectMapper));
        return template;
    }
}
