
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.ShopRecord;
import org.demo.persistence.ShopPersistence;

/**
 * Shop persistence service - SpringRedis implementation 
 *
 */
@Named("ShopPersistence")
public class ShopPersistenceSpringRedis implements ShopPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Shop";
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
	private ShopRecord newInstanceWithPrimaryKey(String code) {
		ShopRecord record = new ShopRecord();
        record.setCode(code); 
		return record;
	}

	private String buildRedisKey(ShopRecord record) {
		return "Shop :" + record.getCode().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public ShopRecord create(ShopRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(ShopRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(String code) {
		ShopRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(ShopRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(String code) {
		ShopRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<ShopRecord> findAll() {
		List<ShopRecord> recordList = new ArrayList<ShopRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((ShopRecord) o);
		}
		return recordList;
	}

	@Override
	public ShopRecord findById(String code) {
        ShopRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		return (ShopRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public ShopRecord save(ShopRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(ShopRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
