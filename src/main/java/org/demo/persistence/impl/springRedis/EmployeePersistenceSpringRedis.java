
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.EmployeeRecord;
import org.demo.persistence.EmployeePersistence;

/**
 * Employee persistence service - SpringRedis implementation 
 *
 */
@Named("EmployeePersistence")
public class EmployeePersistenceSpringRedis implements EmployeePersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Employee";
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
	private EmployeeRecord newInstanceWithPrimaryKey(String code) {
		EmployeeRecord record = new EmployeeRecord();
        record.setCode(code); 
		return record;
	}

	private String buildRedisKey(EmployeeRecord record) {
		return "Employee :" + record.getCode().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public EmployeeRecord create(EmployeeRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(EmployeeRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(String code) {
		EmployeeRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(EmployeeRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(String code) {
		EmployeeRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<EmployeeRecord> findAll() {
		List<EmployeeRecord> recordList = new ArrayList<EmployeeRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((EmployeeRecord) o);
		}
		return recordList;
	}

	@Override
	public EmployeeRecord findById(String code) {
        EmployeeRecord record = newInstanceWithPrimaryKey(code);
		String key = buildRedisKey(record);
		return (EmployeeRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public EmployeeRecord save(EmployeeRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(EmployeeRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
