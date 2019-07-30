package com.topic;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.config.HazelcastConfiguration;
import com.controllers.HazelcastController;

@Service
public class HazelcastTemplate {
	private final Logger logger = LoggerFactory.getLogger(HazelcastTemplate.class);

	public static String INPUT_JOB_MAP = "randomInputDataMap";
	public static String ROLE_JOB_MAP = "";

	@Autowired
	private HazelcastConfiguration hazelcastConfiguration;

	public void send(final String mapName, String value) {
		logger.info("--->{},{}", mapName, value);
		hazelcastConfiguration.getDistributedMapForJobInput().put(mapName , value);
	}

}