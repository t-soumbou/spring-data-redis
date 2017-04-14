
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.AuthorRecord;
import org.demo.persistence.AuthorPersistence;

/**
 * Author persistence service - SpringRedis implementation 
 *
 */
@Named("AuthorPersistence")
public class AuthorPersistenceSpringRedis implements AuthorPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Author";
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
	 * @param id
	 * @return the new instance
	 */
	private AuthorRecord newInstanceWithPrimaryKey(Integer id) {
		AuthorRecord record = new AuthorRecord();
        record.setId(id); 
		return record;
	}

	private String buildRedisKey(AuthorRecord record) {
		return "Author :" + record.getId().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public AuthorRecord create(AuthorRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(AuthorRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(Integer id) {
		AuthorRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(AuthorRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(Integer id) {
		AuthorRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<AuthorRecord> findAll() {
		List<AuthorRecord> recordList = new ArrayList<AuthorRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((AuthorRecord) o);
		}
		return recordList;
	}

	@Override
	public AuthorRecord findById(Integer id) {
        AuthorRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		return (AuthorRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public AuthorRecord save(AuthorRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(AuthorRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
