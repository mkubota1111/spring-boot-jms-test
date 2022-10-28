package com.mkubota;

import java.io.IOException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.mkubota.jms.User;

@SpringBootApplication
@EnableJms
public class AppStarter {
  private static Logger logger = LoggerFactory.getLogger(AppStarter.class);
  @Value("${spring.activemq.broker-url}")
  private String brokerUrl;

  public static void main(String[] args) throws IOException {
    ConfigurableApplicationContext ctx = SpringApplication.run(AppStarter.class, args);
    JmsTemplate jms = ctx.getBean(JmsTemplate.class);

    logger.info("Sending an user message");
    jms.convertAndSend("userQueue", new User("test@test.gov", 1d, true));

    logger.info("Waiting for user and confirmation");
    System.in.read();
    logger.info("Exiting");
    ctx.close();
  }

  @Bean
  public ActiveMQConnectionFactory activeMQConnectionFactory() {
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
    activeMQConnectionFactory.setBrokerURL(brokerUrl);
    return activeMQConnectionFactory;
  }

  @Bean // Serialize message content to json using TextMessage
  public MessageConverter jacksonJmsMessageConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    return converter;
  }

  @Bean
  public JmsListenerContainerFactory<?> connectionFactory() {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(activeMQConnectionFactory());
    factory.setMessageConverter(jacksonJmsMessageConverter());
    // This provides all boot's default to this factory, including the message
    // converter
    // You could still override some of Boot's default if necessary.
    return factory;
  }
}
