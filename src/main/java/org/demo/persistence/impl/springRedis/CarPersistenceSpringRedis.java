package org.demo.persistence.impl.springRedis;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import org.demo.data.record.CarRecord;
import org.demo.persistence.CarPersistence;

/**
 * Car persistence service - SpringRedis implementation 
 */
@Named("CarPersistence")
public class CarPersistenceSpringRedis implements CarPersistence {

	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	private static final String KEY = "Car";
	@Autowired
	private RedisAtomicLong counterCar;
    //----------------------------------------------------------------------
    // SETTER(S) FOR OTHER DATA FIELDS 
    //----------------------------------------------------------------------
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	public void setCounterCar(RedisAtomicLong counterCar) {
		this.counterCar = counterCar;
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
	private CarRecord newInstanceWithPrimaryKey(int id) {
		CarRecord record = new CarRecord();
        record.setId(id); 
		return record;
	}

	private String buildRedisKey(CarRecord record) {
		return "Car :" + record.getId().toString() ;
	}
	//-------------------------------------------------------------------------------------
	// Persistence interface implementations
	//-------------------------------------------------------------------------------------
	@Override
	public long countAll() {
		return redisTemplate.opsForHash().size(KEY);
	}
	
	@Override
	public CarRecord create(CarRecord record) {
		Integer id = (int)counterCar.incrementAndGet();
		record.setId(id);
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return record;
	}

	@Override
	public boolean delete(CarRecord record) {
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean deleteById(Integer id) {
		CarRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		long r = redisTemplate.opsForHash().delete(KEY, key);
		return r > 0;
	}

	@Override
	public boolean exists(CarRecord record) {
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public boolean exists(Integer id) {
		CarRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		return redisTemplate.opsForHash().hasKey(KEY, key);
	}

	@Override
	public List<CarRecord> findAll() {
		List<CarRecord> recordList = new ArrayList<CarRecord>();
		for (Object o : redisTemplate.opsForHash().values(KEY)) {
			recordList.add((CarRecord) o);
		}
		return recordList;
	}

	@Override
	public CarRecord findById(Integer id) {
        CarRecord record = newInstanceWithPrimaryKey(id);
		String key = buildRedisKey(record);
		return (CarRecord) redisTemplate.opsForHash().get(KEY, key);
	}

	@Override
	public CarRecord save(CarRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY,key, record);
		return record;
	}

	@Override
	public boolean update(CarRecord record) {
		String key = buildRedisKey(record);
		redisTemplate.opsForHash().put(KEY, key, record);
		return true;
	}
}
