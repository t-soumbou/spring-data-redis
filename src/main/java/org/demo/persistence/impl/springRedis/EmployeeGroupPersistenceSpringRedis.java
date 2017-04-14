
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.EmployeeGroupRecord;
import org.demo.persistence.EmployeeGroupPersistence;

/**
 * EmployeeGroup persistence service - SpringRedis implementation 
 *
 */
@Named("EmployeeGroupPersistence")
public class EmployeeGroupPersistenceSpringRedis implements EmployeeGroupPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "EmployeeGroup";
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
	 * @param employeeCode
	 * @param groupId
	 * @return the new instance
	 */
	private EmployeeGroupRecord newInstanceWithPrimaryKey(String employeeCode, Short groupId) {
		EmployeeGroupRecord record = new EmployeeGroupRecord();
        record.setEmployeeCode(employeeCode); 
        record.setGroupId(groupId); 
		return record;
	}

	private String buildRedisKey(EmployeeGroupRecord record) {
		return "EmployeeGroup :" + record.getEmployeeCode().toString() +"|"+record.getGroupId().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public EmployeeGroupRecord create(EmployeeGroupRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(EmployeeGroupRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(String employeeCode, Short groupId) {
		EmployeeGroupRecord record = newInstanceWithPrimaryKey(employeeCode, groupId);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(EmployeeGroupRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(String employeeCode, Short groupId) {
		EmployeeGroupRecord record = newInstanceWithPrimaryKey(employeeCode, groupId);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<EmployeeGroupRecord> findAll() {
		List<EmployeeGroupRecord> recordList = new ArrayList<EmployeeGroupRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((EmployeeGroupRecord) o);
		}
		return recordList;
	}

	@Override
	public EmployeeGroupRecord findById(String employeeCode, Short groupId) {
        EmployeeGroupRecord record = newInstanceWithPrimaryKey(employeeCode, groupId);
		String key = buildRedisKey(record);
		return (EmployeeGroupRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public EmployeeGroupRecord save(EmployeeGroupRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(EmployeeGroupRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
