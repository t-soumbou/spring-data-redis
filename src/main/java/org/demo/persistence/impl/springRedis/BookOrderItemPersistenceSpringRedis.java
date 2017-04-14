
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.BookOrderItemRecord;
import org.demo.persistence.BookOrderItemPersistence;

/**
 * BookOrderItem persistence service - SpringRedis implementation 
 *
 */
@Named("BookOrderItemPersistence")
public class BookOrderItemPersistenceSpringRedis implements BookOrderItemPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "BookOrderItem";
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
	 * @param bookOrderId
	 * @param bookId
	 * @return the new instance
	 */
	private BookOrderItemRecord newInstanceWithPrimaryKey(Integer bookOrderId, Integer bookId) {
		BookOrderItemRecord record = new BookOrderItemRecord();
        record.setBookOrderId(bookOrderId); 
        record.setBookId(bookId); 
		return record;
	}

	private String buildRedisKey(BookOrderItemRecord record) {
		return "BookOrderItem :" + record.getBookOrderId().toString() +"|"+record.getBookId().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public BookOrderItemRecord create(BookOrderItemRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(BookOrderItemRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(Integer bookOrderId, Integer bookId) {
		BookOrderItemRecord record = newInstanceWithPrimaryKey(bookOrderId, bookId);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(BookOrderItemRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(Integer bookOrderId, Integer bookId) {
		BookOrderItemRecord record = newInstanceWithPrimaryKey(bookOrderId, bookId);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<BookOrderItemRecord> findAll() {
		List<BookOrderItemRecord> recordList = new ArrayList<BookOrderItemRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((BookOrderItemRecord) o);
		}
		return recordList;
	}

	@Override
	public BookOrderItemRecord findById(Integer bookOrderId, Integer bookId) {
        BookOrderItemRecord record = newInstanceWithPrimaryKey(bookOrderId, bookId);
		String key = buildRedisKey(record);
		return (BookOrderItemRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public BookOrderItemRecord save(BookOrderItemRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(BookOrderItemRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
