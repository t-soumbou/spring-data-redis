
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.CountryRecord;
import org.demo.persistence.CountryPersistence;

/**
 * Country persistence service - SpringRedis implementation 
 *
 */
@Named("CountryPersistence")
public class CountryPersistenceSpringRedis implements CountryPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Country";
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
	private CountryRecord newInstanceWithPrimaryKey(String code) {
		CountryRecord record = new CountryRecord();
        record.setCode(code); 
		return record;
	}

	private String buildRedisKey(CountryRecord record) {
		return "Country :" + record.getCode().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public CountryRecord create(CountryRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(CountryRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(String code) {
		CountryRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(CountryRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(String code) {
		CountryRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<CountryRecord> findAll() {
		List<CountryRecord> recordList = new ArrayList<CountryRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((CountryRecord) o);
		}
		return recordList;
	}

	@Override
	public CountryRecord findById(String code) {
        CountryRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		return (CountryRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public CountryRecord save(CountryRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(CountryRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
