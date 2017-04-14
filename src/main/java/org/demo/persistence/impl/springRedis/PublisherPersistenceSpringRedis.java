
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.PublisherRecord;
import org.demo.persistence.PublisherPersistence;

/**
 * Publisher persistence service - SpringRedis implementation 
 *
 */
@Named("PublisherPersistence")
public class PublisherPersistenceSpringRedis implements PublisherPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Publisher";
    //----------------------------------------------------------------------
    // SETTER(S) FOR OTHER DATA FIELDS 
    //----------------------------------------------------------------------
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	// -------------------------------------------------------------------------------------
	// Utils methods
	// -------------------------------------------------------------------------------------
	/**
	 * Creates a new bean instance and set its primary key value(s)
	 * 
	 * @param code
	 * @return the new instance
	 */
	private PublisherRecord newInstanceWithPrimaryKey(Integer code) {
		PublisherRecord record = new PublisherRecord();
        record.setCode(code); 
		return record;
	}

	private String buildRedisKey(PublisherRecord record) {
		return "Publisher :" + record.getCode().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public PublisherRecord create(PublisherRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(PublisherRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(Integer code) {
		PublisherRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(PublisherRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(Integer code) {
		PublisherRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<PublisherRecord> findAll() {
		List<PublisherRecord> recordList = new ArrayList<PublisherRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((PublisherRecord) o);
		}
		return recordList;
	}

	@Override
	public PublisherRecord findById(Integer code) {
        PublisherRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		return (PublisherRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public PublisherRecord save(PublisherRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(PublisherRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
