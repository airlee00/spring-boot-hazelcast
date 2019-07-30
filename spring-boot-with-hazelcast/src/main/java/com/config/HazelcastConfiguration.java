package com.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.hazelcast.CacheListeningPolicyType;
import org.springframework.integration.hazelcast.inbound.HazelcastEventDrivenMessageProducer;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.support.PeriodicTrigger;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.topic.LogAndGiveInitiatorHandler;

/**
 *
 * https://bitbucket.org/tomask79/spring-leader-hazelcast/
 *
 * @author kichaelee
 *
 */
@Configuration
public class HazelcastConfiguration {

	public static String INPUT_JOB_MAP = "randomInputDataMap";
	public static String ROLE_JOB_MAP = "";

	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Bean
	public Config hazelCastConfig() {
		Properties prop = new Properties();
		prop.setProperty("time-to-live-seconds", "1111");
		Config config = new XmlConfigBuilder().setProperties(prop).build();
		config.setInstanceName("hazelcast-instance")
				.addMapConfig(new MapConfig().setName("configuration")
						.setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
						.setEvictionPolicy(EvictionPolicy.LRU).setTimeToLiveSeconds(-1));
//        HotRestartPersistenceConfig hotRestartPersistenceConfig = new HotRestartPersistenceConfig()
//                .setEnabled(true)
//                .setBaseDir(new File("cache"))
//                .setBackupDir(new File("cache/backup"))
//                .setParallelism(1)
//                .setClusterDataRecoveryPolicy(HotRestartClusterDataRecoveryPolicy.FULL_RECOVERY_ONLY);
//            config.setHotRestartPersistenceConfig(hotRestartPersistenceConfig);
//
// System.out.println("===2222=======\n\n\n" + config.toString());

		return config;
	}

	public IMap<String, String> getDistributedMapForJobInput() {
		return hazelcastInstance.getMap(INPUT_JOB_MAP);
	}

    @Bean(name ="aaa") // PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        //TODO - property
        pollerMetadata.setTrigger(new PeriodicTrigger(10));
        return pollerMetadata;
    }

	@Bean
	public PollableChannel distributedMapChannel() {
		return new QueueChannel();
	}

	@Bean
	public MessageChannel inputJobChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "distributedMapChannel", poller=@Poller( fixedDelay ="${poller.delay}", maxMessagesPerPoll="${poller.per}"))
	public MessageHandler logger() {
		return new LogAndGiveInitiatorHandler();
	}

	@Bean
	public HazelcastEventDrivenMessageProducer hazelcastEventDrivenMessageProducer() {
		final HazelcastEventDrivenMessageProducer producer = new HazelcastEventDrivenMessageProducer(
				getDistributedMapForJobInput());
		producer.setOutputChannel(distributedMapChannel());
		producer.setCacheEventTypes("ADDED,REMOVED,UPDATED,CLEAR_ALL");
		producer.setCacheListeningPolicy(CacheListeningPolicyType.SINGLE);
		producer.setAutoStartup(true);

		return producer;
	}
}
