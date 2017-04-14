
package org.demo.persistence.impl.springRedis;

import org.demo.persistence.BookOrderItemPersistenceGenericTest;
import org.junit.After;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.demo.persistence.impl.springRedis.commons.SpringRedisConfig;


/**
 * JUnit tests for Car persistence service
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRedisConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class BookOrderItemPersistenceSpringRedisIT extends BookOrderItemPersistenceGenericTest {
    
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	private static final String KEY = "BookOrderItem";

	@After
	public void tearDown() throws Exception {
		redisTemplate.delete(KEY);
	}

	@Test
	public void testPersistenceService() throws Exception {
		BookOrderItemPersistenceSpringRedis persistence = new BookOrderItemPersistenceSpringRedis();
		persistence.setRedisTemplate(redisTemplate);
    	testPersistenceService(persistence);
	}
}

