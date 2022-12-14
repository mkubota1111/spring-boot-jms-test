package com.mkubota.jms;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserReceiver {
  private Logger logger = LoggerFactory.getLogger(UserReceiver.class);
  private static AtomicInteger id = new AtomicInteger();

  @Autowired
  JmsTemplate jmsTemplate;

  @JmsListener(destination = "userQueue", containerFactory = "connectionFactory", selector = "operation = 'create'")
  public void receiveMessage(User receivedUser) {
    logger.info("Receiver 1");
    logger.info("Received user: " + receivedUser);
    jmsTemplate.convertAndSend("confirmationQueue",
        new Confirmation(id.incrementAndGet(), "User %s received.".formatted(receivedUser.getEmail())));
  }
}
