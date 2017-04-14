
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.ReviewRecord;
import org.demo.persistence.ReviewPersistence;

/**
 * Review persistence service - SpringRedis implementation 
 *
 */
@Named("ReviewPersistence")
public class ReviewPersistenceSpringRedis implements ReviewPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Review";
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
	 * @param customerCode
	 * @param bookId
	 * @return the new instance
	 */
	private ReviewRecord newInstanceWithPrimaryKey(String customerCode, Integer bookId) {
		ReviewRecord record = new ReviewRecord();
        record.setCustomerCode(customerCode); 
        record.setBookId(bookId); 
		return record;
	}

	private String buildRedisKey(ReviewRecord record) {
		return "Review :" + record.getCustomerCode().toString() +"|"+record.getBookId().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public ReviewRecord create(ReviewRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(ReviewRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(String customerCode, Integer bookId) {
		ReviewRecord record = newInstanceWithPrimaryKey(customerCode, bookId);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(ReviewRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(String customerCode, Integer bookId) {
		ReviewRecord record = newInstanceWithPrimaryKey(customerCode, bookId);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<ReviewRecord> findAll() {
		List<ReviewRecord> recordList = new ArrayList<ReviewRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((ReviewRecord) o);
		}
		return recordList;
	}

	@Override
	public ReviewRecord findById(String customerCode, Integer bookId) {
        ReviewRecord record = newInstanceWithPrimaryKey(customerCode, bookId);
		String key = buildRedisKey(record);
		return (ReviewRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public ReviewRecord save(ReviewRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(ReviewRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
