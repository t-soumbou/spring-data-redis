
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.CustomerRecord;
import org.demo.persistence.CustomerPersistence;

/**
 * Customer persistence service - SpringRedis implementation 
 *
 */
@Named("CustomerPersistence")
public class CustomerPersistenceSpringRedis implements CustomerPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Customer";
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
	private CustomerRecord newInstanceWithPrimaryKey(String code) {
		CustomerRecord record = new CustomerRecord();
        record.setCode(code); 
		return record;
	}

	private String buildRedisKey(CustomerRecord record) {
		return "Customer :" + record.getCode().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public CustomerRecord create(CustomerRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(CustomerRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(String code) {
		CustomerRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(CustomerRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(String code) {
		CustomerRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<CustomerRecord> findAll() {
		List<CustomerRecord> recordList = new ArrayList<CustomerRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((CustomerRecord) o);
		}
		return recordList;
	}

	@Override
	public CustomerRecord findById(String code) {
        CustomerRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		return (CustomerRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public CustomerRecord save(CustomerRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(CustomerRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
