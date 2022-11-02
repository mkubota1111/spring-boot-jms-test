package com.mkubota;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import com.mkubota.jms.User;

@SpringBootApplication
public class AppStarter {
  private static Logger logger = LoggerFactory.getLogger(AppStarter.class);

  public static void main(String[] args) throws IOException, InterruptedException {
    ConfigurableApplicationContext ctx = SpringApplication.run(AppStarter.class, args);
    JmsTemplate jms = ctx.getBean(JmsTemplate.class);

    logger.info("Sending an user message");
    jms.convertAndSend("userQueue", new User("test@test.gov", 1d, true), msg -> {
      msg.setStringProperty("operation", "create");
      return msg;
    });
    jms.convertAndSend("userQueue", new User("test2@test.gov", 1d, true), msg -> {
      msg.setStringProperty("operation", "create2");
      return msg;
    });
    // Instead of userService.processUser(new User(...));

    logger.info("Waiting for user and confirmation");
    Thread.sleep(3000);
    logger.info("Exiting");
    ctx.close();
  }
}
