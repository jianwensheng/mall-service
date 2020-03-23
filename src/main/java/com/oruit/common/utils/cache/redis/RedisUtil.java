/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oruit.common.utils.cache.redis;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author hanfeng
 */
@Slf4j
public class RedisUtil {


    /**
     * 成功,"OK"
     */
    private static final String SUCCESS_OK = "OK";
    /**
     * 成功,1L
     */
    private static final Long SUCCESS_STATUS_LONG = 1L;
    /**
     * 只用key不存在时才设置。Only set the key if it does not already exist
     */
    private static final String NX = "NX";
    /**
     * XX -- 只有key存在时才设置。和NX相反。Only set the key if it already exist.
     */
    private static final String XX = "XX";
    /**
     * EX|PX, 时间单位，EX是秒，PX是毫秒。expire time units: EX = seconds; PX = milliseconds
     */
    private static final String EX = "EX";
    /**
     * EX|PX, 时间单位，EX是秒，PX是毫秒。expire time units: EX = seconds; PX = milliseconds
     */
    private static final String PX = "PX";

    protected RedisUtil() {
    }

    /**
     * 成功返回true
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            String statusCode = jedis.set(key, value);
            if (SUCCESS_OK.equalsIgnoreCase(statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 返回值
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            return jedis.get(key);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return null;
    }

    /**
     * 设置key值和过期时间
     *
     * @param key
     * @param value
     * @param seconds 秒数，不能小于0
     * @return
     */
    public static boolean setByTime(String key, String value, int seconds) {
        if (seconds < 0) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            String statusCode = jedis.setex(key, seconds, value);
            if (SUCCESS_OK.equalsIgnoreCase(statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * @param key
     * @param value
     * @param nxxx  NX|XX 是否存在
     *              <li>NX -- Only set the key if it does not already exist.</li>
     *              <li>XX -- Only set the key if it already exist.</li>
     * @param expx  EX|PX, expire time units ，时间单位格式，秒或毫秒
     *              <li>EX = seconds;</li>
     *              <li>PX = milliseconds</li>
     * @param time  expire time in the units of expx，时间（long型），不能小于0
     * @return
     */
    public static boolean set(String key, String value,
                              String nxxx, String expx, long time) {
        if (time < 0) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            String statusCode = jedis.set(key, value, nxxx, expx, time);
            if (SUCCESS_OK.equalsIgnoreCase(statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 设置key
     *
     * @param key
     * @param value
     * @param nxxx  NX|XX 是否需要存在
     *              <li>NX -- Only set the key if it does not already exist.</li>
     *              <li>XX -- Only set the key if it already exist.</li>
     * @return
     */
    public static boolean set(String key, String value,
                              String nxxx) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            String statusCode = jedis.set(key, value, nxxx);
            if (SUCCESS_OK.equalsIgnoreCase(statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 当key不存在时，设置值，成功返回true
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setIfNotExists(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            Long statusCode = jedis.setnx(key, value);
            if (Objects.equals(SUCCESS_STATUS_LONG, statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 当key不存在时，设置值，成功返回true，同setIfNotExists
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setNX(String key, String value) {
        return setIfNotExists(key, value);
    }

    /**
     * 仅当key不存在时则设置值，成功返回true，存在时不设置值
     *
     * @param key
     * @param value
     * @param seconds，秒数，不能小于0
     * @return
     */
    public static boolean setIfNotExists(String key, String value, long seconds) {
        if (seconds < 0) {
            return false;
        }
        return set(key, value, NX, EX, seconds);
    }

    /**
     * 仅当key不存在时则设置值，成功返回true，存在时不设置值，同setIfNotExists(key, value, seconds)
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public static boolean setNX(String key, String value, Long seconds) {
        return setIfNotExists(key, value, seconds);
    }

    /**
     * 当key存在时则设置值，成功返回true，不存在不设置值
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setIfExists(String key, String value) {
        return set(key, value, XX);
    }

    /**
     * 当key存在时则设置值，成功返回true，不存在不设置值，同setIfExists
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setXX(String key, String value) {
        return setIfExists(key, value);
    }

    /**
     * 仅当key存在时则设置值，成功返回true，不存在不设置值
     *
     * @param key
     * @param value
     * @param seconds，秒数，不能小于0
     * @return
     */
    public static boolean setIfExists(String key, String value, long seconds) {
        if (seconds < 0) {
            return false;
        }
        return set(key, value, XX, EX, seconds);
    }

    /**
     * 仅当key存在时则设置值，成功返回true，不存在不设置值
     *
     * @param key
     * @param value
     * @param seconds，秒数，不能小于0
     * @return
     */
    public static boolean setXX(String key, String value, long seconds) {
        return setIfExists(key, value, seconds);
    }

    /**
     * 设置超期时间
     *
     * @param key
     * @param seconds 为Null时，将会马上过期。可以设置-1，0，表示马上过期
     * @return
     */
    public static boolean setTime(String key, Integer seconds) {
        if (seconds == null) {
            seconds = -1;
        }
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            Long statusCode = jedis.expire(key, seconds);
            if (Objects.equals(SUCCESS_STATUS_LONG, statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 设置超期时间
     *
     * @param key
     * @param seconds 为Null时，将会马上过期。可以设置-1，0，表示马上过期
     * @return
     */
    public static boolean setOutTime(String key, Integer seconds) {
        return setTime(key, seconds);
    }

    /**
     * 设置超期时间
     *
     * @param key
     * @param seconds 秒数，为Null时，将会马上过期。可以设置-1，0，表示马上过期
     * @return
     */
    public static boolean expire(String key, Integer seconds) {
        return setTime(key, seconds);
    }

    /**
     * 判断key是否存在，存在返回true
     *
     * @param key
     * @return
     */
    public static boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            return jedis.exists(key);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 判断key是否存在，存在返回true
     *
     * @param key
     * @return
     */
    public static boolean isExists(String key) {
        return exists(key);
    }

    /**
     * 将key设置为永久
     *
     * @param key
     * @return
     */
    public static boolean persist(String key) {
        long time = getTime(key);
        if (time == -1) {
            return true;
        }
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            //已经是永久的，返回0    
            Long statusCode = jedis.persist(key);
            if (Objects.equals(SUCCESS_STATUS_LONG, statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 获取剩余时间（秒）
     *
     * @param key
     * @return
     */
    public static Long getTime(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            return jedis.ttl(key);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return -1L;
    }

    /**
     * 获取剩余时间（秒）
     *
     * @param key
     * @return
     */
    public static Long Ttl(String key) {
        return getTime(key);
    }

    /**
     * 随机获取一个key
     *
     * @return
     */
    public static String randomKey() {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            return jedis.randomKey();
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return null;
    }

    /**
     * 随机获取一个key
     *
     * @return
     */
    public static String random() {
        return randomKey();
    }

    /**
     * 修改 key 的名称，成功返回true，如果不存在该key，则会抛错：ERR no such key
     * 注：如果newKey已经存在，则会进行覆盖。建议使用renameNX
     *
     * @param oldkey 原来的key
     * @param newKey 新的key
     * @return
     */
    public static boolean rename(String oldkey, String newKey) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            String statusCode = jedis.rename(oldkey, newKey);
            if (SUCCESS_OK.equalsIgnoreCase(statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 仅当 newkey 不存在时，将 key 改名为 newkey 。成功返回true
     *
     * @param oldkey
     * @param newKey
     * @return
     */
    public static boolean renameNX(String oldkey, String newKey) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            Long statusCode = jedis.renamenx(oldkey, newKey);
            if (Objects.equals(SUCCESS_STATUS_LONG, statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 仅当 newkey 不存在时，将 key 改名为 newkey 。成功返回true
     *
     * @param oldkey
     * @param newKey
     * @return
     */
    public static boolean renameIfNotExists(String oldkey, String newKey) {
        return renameNX(oldkey, newKey);
    }

    /**
     * 返回 key 所储存的值的类型。
     *
     * @param key
     * @return
     */
    public static String type(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            return jedis.type(key);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return null;
    }

    /**
     * 返回 key 所储存的值的类型。
     *
     * @param key
     * @return
     */
    public static String getType(String key) {
        return type(key);
    }

    /**
     * 删除key及值
     *
     * @param key
     * @return
     */
    public static boolean del(String key) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            Long statusCode = jedis.del(key);
            if (Objects.equals(SUCCESS_STATUS_LONG, statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 删除key及值
     *
     * @param key
     * @return
     */
    public static boolean delete(String key) {
        return del(key);
    }

    /**
     * 删除key及值
     *
     * @param key
     * @return
     */
    public static boolean remove(String key) {
        return del(key);
    }

    /**
     * 批量删除key及值
     *
     * @param keys
     * @return
     */
    public static boolean del(String[] keys) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            Long statusCode = jedis.del(keys);
            if (statusCode > 0) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 批量删除key及值
     *
     * @param keys
     * @return
     */
    public static boolean delete(String[] keys) {
        return del(keys);
    }

    /**
     * 批量删除key及值
     *
     * @param keys
     * @return
     */
    public static boolean remove(String[] keys) {
        return del(keys);
    }

    /**
     * ************************** redis Hash start**************************
     */
    /**
     * *Redis hash 是一个string类型的field和value的映射表，hash特别适合用于存储对象。**
     */
    /**
     * 设置Hash的属性
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static boolean hset(String key, String field, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            //If the field already exists, and the HSET just produced an update of the value, 0 is returned,
            //otherwise if a new field is created 1 is returned.  
            Long statusCode = jedis.hset(key, field, value);
            if (statusCode > -1) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 批量设置Hash的属性
     *
     * @param key
     * @param fields
     * @param values
     * @return
     */
    public static boolean hmset(String key, String[] fields, String[] values) {
        if (StringUtils.isBlank(key) || StringUtils.isEmptyArray(fields) || StringUtils.isEmptyArray(values)) {
            return false;
        }
        Map<String, String> hash = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            hash.put(fields[i], values[i]);
        }
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            String statusCode = jedis.hmset(key, hash);
            if (SUCCESS_OK.equalsIgnoreCase(statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 批量设置Hash的属性
     *
     * @param key
     * @param map Map<String, String>
     * @return
     */
    public static boolean hmset(String key, Map<String, String> map) {
        if (StringUtils.isBlank(key) || StringUtils.isEmptyMap(map)) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            String statusCode = jedis.hmset(key, map);
            if (SUCCESS_OK.equalsIgnoreCase(statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 仅当field不存在时设置值，成功返回true
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static boolean hsetNX(String key, String field, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            Long statusCode = jedis.hsetnx(key, field, value);
            if (Objects.equals(SUCCESS_STATUS_LONG, statusCode)) {
                return true;
            }
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * 获取属性的值
     *
     * @param key
     * @param field
     * @return
     */
    public static String hget(String key, String field) {
        String value = null;
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            value = jedis.hget(key, field);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return value;
    }

    /**
     * 批量获取属性的值
     *
     * @param key
     * @param fields String...
     * @return
     */
    public static List<String> hmget(String key, String... fields) {
        if (StringUtils.isBlank(key) || StringUtils.isNull(fields)) {
            return null;
        }
        List<String> values = null;
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            values = jedis.hmget(key, fields);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return values;
    }

    /**
     * 获取在哈希表中指定 key 的所有字段和值
     *
     * @param key
     * @return Map<String, String>
     */
    public static Map<String, String> hgetAll(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Map<String, String> map = null;
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            map = jedis.hgetAll(key);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return map;
    }

    /**
     * 删除hash的属性
     *
     * @param key
     * @param fields
     * @return
     */
    public static boolean hdel(String key, String... fields) {
        if (StringUtils.isBlank(key) || StringUtils.isNull(fields)) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            jedis.hdel(key, fields);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        //if(logger.isDebugEnabled()){ logger.debug("statusCode="+statusCode); }
        return true;
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在。
     *
     * @param key
     * @param field
     * @return
     */
    public static boolean hexists(String key, String field) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            return false;
        }
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            result = jedis.hexists(key, field);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment 。
     *
     * @param key
     * @param field
     * @param increment 正负数、0、正整数
     * @return
     */
    public static long hincrBy(String key, String field, long increment) {
        long result = 0L;
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            result = jedis.hincrBy(key, field, increment);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 为哈希表 key 中的指定字段的浮点数值加上增量 increment 。(注：如果field不存在时，会设置新的值)
     *
     * @param key
     * @param field
     * @param increment，可以为负数、正数、0
     * @return
     */
    public static Double hincrByFloat(String key, String field, double increment) {
        Double result = null;
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            result = jedis.hincrByFloat(key, field, increment);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 获取所有哈希表中的字段
     *
     * @param key
     * @return Set<String>
     */
    public static Set<String> hkeys(String key) {
        Set<String> result = null;
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            result = jedis.hkeys(key);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 获取哈希表中所有值
     *
     * @param key
     * @return List<String>
     */
    public static List<String> hvals(String key) {
        List<String> result = null;
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            result = jedis.hvals(key);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 获取哈希表中字段的数量，当key不存在时，返回0
     *
     * @param key
     * @return
     */
    public static Long hlen(String key) {
        Long result = 0L;
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            result = jedis.hlen(key);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return result;
    }

    /**
     * 迭代哈希表中的键值对。
     *
     * @param key
     * @param cursor
     * @return ScanResult<Entry<String, String>>
     */
    public static ScanResult<Entry<String, String>> hscan(String key, String cursor) {
        ScanResult<Entry<String, String>> scanResult = null;
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            scanResult = jedis.hscan(key, cursor);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        //if(logger.isDebugEnabled()){ logger.debug(scanResult.getResult()); }
        return scanResult;
    }

    public static Set<String> zrangeByScore(String key, long min, long max, int offset, int count) {
        Set<String> result = null;
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            result = jedis.zrangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return result;
    }

    public static Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        Set<String> result = null;
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            result = jedis.zrangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return result;
    }

    // 存储对象
    public static void setObject(String key,int expire, Object obj) {
        Jedis jedis = null;
        try {
            if(obj==null) {
                return ;
            }
            jedis = RedisPoolUtil.getJedis();
            jedis.setex(key,expire, JSONObject.toJSONString(obj));
        } catch (Exception e) {
            log.info("缓存服务器连接异常！");
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
    }

    // 获取对象
    public static <T> T getObject(String key,Class<T> t) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getJedis();
            String str = jedis.get(key);
            if(str==null) {
                return null;
            }
            return JSONObject.parseObject(str,t);
        } catch (Exception e) {
            log.info("缓存服务器连接异常！");
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                RedisPoolUtil.returnResource(jedis);
            }
        }
        return null;
    }


    /**
     * ************************** redis Hash end**************************
     */
}
