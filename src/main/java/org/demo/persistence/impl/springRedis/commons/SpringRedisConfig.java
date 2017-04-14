package org.demo.persistence.impl.springRedis.commons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

@Configuration
@ComponentScan("org.demo")
public class SpringRedisConfig {
	@Bean
	public JedisConnectionFactory connectionFactory() {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.setHostName("localhost");
		connectionFactory.setPort(6379);
		return connectionFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(connectionFactory());
		return redisTemplate;
	}

	@Bean
	public RedisAtomicLong redisAtomicLong() {
		return new RedisAtomicLong("counter", connectionFactory());
	}

	@Bean
	public StringRedisTemplate strRedisTemplate() {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(connectionFactory());
		return redisTemplate;
	}
}