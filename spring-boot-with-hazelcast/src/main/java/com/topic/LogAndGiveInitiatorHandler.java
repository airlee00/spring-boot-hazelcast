package com.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * Created by tomask79 on 24.08.17.
 */
public class LogAndGiveInitiatorHandler implements MessageHandler {

	private final Logger logger = LoggerFactory.getLogger(LogAndGiveInitiatorHandler.class);

	//@Autowired
	//private HazelcastTemplate jobServices;

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.info("-------Waiting for another node to take the work...!");
		logger.info(message.toString());
		// jobServices.giveUp();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("........");
	}
}
