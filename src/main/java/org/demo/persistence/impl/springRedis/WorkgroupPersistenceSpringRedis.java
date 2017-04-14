
package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.demo.data.record.WorkgroupRecord;
import org.demo.persistence.WorkgroupPersistence;

/**
 * Workgroup persistence service - SpringRedis implementation 
 *
 */
@Named("WorkgroupPersistence")
public class WorkgroupPersistenceSpringRedis implements WorkgroupPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Workgroup";
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
	private WorkgroupRecord newInstanceWithPrimaryKey(Short id) {
		WorkgroupRecord record = new WorkgroupRecord();
        record.setId(id); 
		return record;
	}

	private String buildRedisKey(WorkgroupRecord record) {
		return "Workgroup :" + record.getId().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public WorkgroupRecord create(WorkgroupRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(WorkgroupRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(Short id) {
		WorkgroupRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(WorkgroupRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(Short id) {
		WorkgroupRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<WorkgroupRecord> findAll() {
		List<WorkgroupRecord> recordList = new ArrayList<WorkgroupRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((WorkgroupRecord) o);
		}
		return recordList;
	}

	@Override
	public WorkgroupRecord findById(Short id) {
        WorkgroupRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		return (WorkgroupRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public WorkgroupRecord save(WorkgroupRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(WorkgroupRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
