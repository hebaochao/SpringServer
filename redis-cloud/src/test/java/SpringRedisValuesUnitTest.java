import com.it.test.MainApplication;
import com.it.test.RedisValues;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: SpringServer
 * @description:
 * @author: baochao
 * @create: 2020-03-02 23:53
 **/
@SpringBootTest(classes = MainApplication.class)
@RunWith(value = SpringRunner.class)
public class SpringRedisValuesUnitTest {

    @Autowired
    private RedisValues redisValues;

    @Autowired
    private RedisTemplate<String ,String> string1RedisTemplate;

    @Autowired
    private  RedisTemplate<String,Object> redisTemplate;

    @Test
    public void testRedisValues(){
        this.redisValues.addValues("test2","测试1");
        String value = this.redisValues.getValues("test2");
        System.out.println("values : "+value);
    }

    /***
     * 值
     */
    @Test
    public void testStringRedis(){
        this.string1RedisTemplate.opsForValue().set("test1","values1");
        this.string1RedisTemplate.opsForValue().set("test2","values2");
        System.out.println(this.string1RedisTemplate.opsForValue().get("test1"));
    }

    /***
     * hash map 结构
     */
    @Test
    public void testJsonRedis(){
        this.redisTemplate.opsForHash().put("food","shuiguo","apple");
        this.redisTemplate.opsForHash().put("food","rourou","pig");
        System.out.println(this.redisTemplate.opsForHash().get("food","shuiguo"));
    }

    /***
     * 链表结构
     */
    @Test
    public void testListRedis(){
        this.redisTemplate.opsForList().leftPush("testList","a1");
        this.redisTemplate.opsForList().rightPush("testList","a1","a2");
        this.redisTemplate.opsForList().rightPush("testList","a2","a4");
        this.redisTemplate.opsForList().leftPush("testList","a4","a3");
        System.out.println(this.redisTemplate.opsForList().leftPop("testList"));
    }

    /***
     * 无序集合
     * 无序存取
     */
    @Test
    public void testSetRedis(){
        this.redisTemplate.opsForSet().add("testSet","b1","b2");
        this.redisTemplate.opsForSet().add("testSet","b3","b4");
        System.out.println(this.redisTemplate.opsForSet().pop("testSet"));
    }

    /***
     * 有序集合
     */
    @Test
    public void testZSetRedis(){
        this.redisTemplate.opsForZSet().add("testzSet","b1",1.0);
        this.redisTemplate.opsForZSet().add("testzSet","b3",2.0);


        Set<ZSetOperations.TypedTuple<String>> set  = new HashSet<ZSetOperations.TypedTuple<String>>();

        this.redisTemplate.opsForZSet().add("testzSet","b4",1.5);
//        System.out.println(this.redisTemplate.opsForZSet().);
    }


    @Test
    public void testHash(){
        this.redisTemplate.opsForHash().put("rootHash1","key1","value1");
        this.redisTemplate.opsForHash().put("rootHash1","key2","value2");
        this.redisTemplate.opsForHash().put("rootHash2","key22","value22");

       String value1 = (String) this.redisTemplate.opsForHash().get("rootHash1","key1");
       System.out.println(value1);
    }



}
