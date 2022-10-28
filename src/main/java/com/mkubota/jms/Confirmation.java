package com.mkubota.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Confirmation {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private int ackNumber;
  private String verificationComment;

  public Confirmation() {
  }

  public Confirmation(int ackNumber, String verificationComment) {
    this.ackNumber = ackNumber;
    this.verificationComment = verificationComment;
  }

  public int getAckNumber() {
    return ackNumber;
  }

  public void setAckNumber(int ackNumber) {
    this.ackNumber = ackNumber;
  }

  public String getVerificationComment() {
    return verificationComment;
  }

  public void setVerificationComment(String verificationComment) {
    this.verificationComment = verificationComment;
  }

  @Override
  public String toString() {
    String str = """
        Confirmation{ackNumber='%1$d'\
        ,verificationComment=%2$s}\
        """.formatted(ackNumber, verificationComment);
    logger.info("Confirmation.toString(): " + str);
    return str;
  }
}
