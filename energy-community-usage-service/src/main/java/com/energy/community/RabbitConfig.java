package com.energy.community;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // основная очередь, куда летят исходные данные
    public static final String ENERGY_DATA_Q = "energy-data";
    // очередь-триггер для перерасчёта процентов
    public static final String USAGE_UPDATED_Q = "usage_updated";

    /* — Jackson настроенный на Java Time — */
    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /* — JSON-конвертер для всех шаблонов и листенеров — */
    @Bean
    public MessageConverter jsonConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    /* — Очередь (durable=true) — */
    @Bean
    public Queue energyDataQueue()      { return new Queue(ENERGY_DATA_Q, true); }

    @Bean
    public Queue usageUpdatedQueue() {
        return QueueBuilder.nonDurable(USAGE_UPDATED_Q).build();
    }

    /* — RabbitTemplate c тем же конвертером — */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                         MessageConverter jsonConverter) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(jsonConverter);
        return tpl;
    }
}