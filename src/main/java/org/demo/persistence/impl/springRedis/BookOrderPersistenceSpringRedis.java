
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.BookOrderRecord;
import org.demo.persistence.BookOrderPersistence;

/**
 * BookOrder persistence service - SpringRedis implementation 
 *
 */
@Named("BookOrderPersistence")
public class BookOrderPersistenceSpringRedis implements BookOrderPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "BookOrder";
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
	private BookOrderRecord newInstanceWithPrimaryKey(Integer id) {
		BookOrderRecord record = new BookOrderRecord();
        record.setId(id); 
		return record;
	}

	private String buildRedisKey(BookOrderRecord record) {
		return "BookOrder :" + record.getId().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public BookOrderRecord create(BookOrderRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(BookOrderRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(Integer id) {
		BookOrderRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(BookOrderRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(Integer id) {
		BookOrderRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<BookOrderRecord> findAll() {
		List<BookOrderRecord> recordList = new ArrayList<BookOrderRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((BookOrderRecord) o);
		}
		return recordList;
	}

	@Override
	public BookOrderRecord findById(Integer id) {
        BookOrderRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		return (BookOrderRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public BookOrderRecord save(BookOrderRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(BookOrderRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
