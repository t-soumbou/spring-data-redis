
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.BookRecord;
import org.demo.persistence.BookPersistence;

/**
 * Book persistence service - SpringRedis implementation 
 *
 */
@Named("BookPersistence")
public class BookPersistenceSpringRedis implements BookPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Book";
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
	private BookRecord newInstanceWithPrimaryKey(Integer id) {
		BookRecord record = new BookRecord();
        record.setId(id); 
		return record;
	}

	private String buildRedisKey(BookRecord record) {
		return "Book :" + record.getId().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public BookRecord create(BookRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(BookRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(Integer id) {
		BookRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(BookRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(Integer id) {
		BookRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<BookRecord> findAll() {
		List<BookRecord> recordList = new ArrayList<BookRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((BookRecord) o);
		}
		return recordList;
	}

	@Override
	public BookRecord findById(Integer id) {
        BookRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		return (BookRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public BookRecord save(BookRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(BookRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
