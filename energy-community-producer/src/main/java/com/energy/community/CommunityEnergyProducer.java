package com.energy.community;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class CommunityEnergyProducer implements CommandLineRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper mapper;

    public CommunityEnergyProducer() {
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void run(String... args) {
        System.out.println("✅ CommunityEnergyProducer started");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private final Random rnd = new Random();

            @Override
            public void run() {
                EnergyMessage message = new EnergyMessage(
                        "PRODUCER",
                        1 + rnd.nextDouble() * 9,
                        LocalDateTime.now().withSecond(0).withNano(0)
                );

                try {
                    rabbitTemplate.convertAndSend(RabbitConfig.ENERGY_DATA_Q, message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000);
    }
}