package CollegeHelp.College_Mantra.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class MyCacheRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    public void setOTPValue(String key,Object value){
        redisTemplate.opsForValue()
                .set(key,value,2, TimeUnit.MINUTES);
    }

    public void setTempIdValue(String key,Object value){
        redisTemplate.opsForValue()
                .set(key,value,10, TimeUnit.MINUTES);
    }

    public void setTransactionValue(String key,Object value){
        redisTemplate.opsForValue()
                .set(key,value,3,TimeUnit.HOURS);
    }

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

}
