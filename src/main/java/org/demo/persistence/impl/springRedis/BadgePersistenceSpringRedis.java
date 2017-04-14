
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.BadgeRecord;
import org.demo.persistence.BadgePersistence;

/**
 * Badge persistence service - SpringRedis implementation 
 *
 */
@Named("BadgePersistence")
public class BadgePersistenceSpringRedis implements BadgePersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Badge";
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
	 * @param badgeNumber
	 * @return the new instance
	 */
	private BadgeRecord newInstanceWithPrimaryKey(Integer badgeNumber) {
		BadgeRecord record = new BadgeRecord();
        record.setBadgeNumber(badgeNumber); 
		return record;
	}

	private String buildRedisKey(BadgeRecord record) {
		return "Badge :" + record.getBadgeNumber().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public BadgeRecord create(BadgeRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(BadgeRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(Integer badgeNumber) {
		BadgeRecord record = newInstanceWithPrimaryKey(badgeNumber);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(BadgeRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(Integer badgeNumber) {
		BadgeRecord record = newInstanceWithPrimaryKey(badgeNumber);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<BadgeRecord> findAll() {
		List<BadgeRecord> recordList = new ArrayList<BadgeRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((BadgeRecord) o);
		}
		return recordList;
	}

	@Override
	public BadgeRecord findById(Integer badgeNumber) {
        BadgeRecord record = newInstanceWithPrimaryKey(badgeNumber);
		String key = buildRedisKey(record);
		return (BadgeRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public BadgeRecord save(BadgeRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(BadgeRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
