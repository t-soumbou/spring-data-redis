
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.SynopsisRecord;
import org.demo.persistence.SynopsisPersistence;

/**
 * Synopsis persistence service - SpringRedis implementation 
 *
 */
@Named("SynopsisPersistence")
public class SynopsisPersistenceSpringRedis implements SynopsisPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Synopsis";
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
	 * @param bookId
	 * @return the new instance
	 */
	private SynopsisRecord newInstanceWithPrimaryKey(Integer bookId) {
		SynopsisRecord record = new SynopsisRecord();
        record.setBookId(bookId); 
		return record;
	}

	private String buildRedisKey(SynopsisRecord record) {
		return "Synopsis :" + record.getBookId().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public SynopsisRecord create(SynopsisRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(SynopsisRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(Integer bookId) {
		SynopsisRecord record = newInstanceWithPrimaryKey(bookId);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(SynopsisRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(Integer bookId) {
		SynopsisRecord record = newInstanceWithPrimaryKey(bookId);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<SynopsisRecord> findAll() {
		List<SynopsisRecord> recordList = new ArrayList<SynopsisRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((SynopsisRecord) o);
		}
		return recordList;
	}

	@Override
	public SynopsisRecord findById(Integer bookId) {
        SynopsisRecord record = newInstanceWithPrimaryKey(bookId);
		String key = buildRedisKey(record);
		return (SynopsisRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public SynopsisRecord save(SynopsisRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(SynopsisRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
