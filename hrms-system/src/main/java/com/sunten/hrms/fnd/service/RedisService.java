package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.vo.RedisVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 可自行扩展
 * @author batan
 * @since 2018-12-10
 */
public interface RedisService {

    /**
     * findById
     * @param key 键
     * @return /
     */
    Page listByKey(String key, Pageable pageable);

    /**
     * findById
     * @param key 键
     * @return /
     */
    List<RedisVo> listByKey(String key);

    /**
     * 查询验证码的值
     * @param key 键
     * @return /
     */
    String getCodeVal(String key);

    /**
     * 保存验证码
     * @param key 键
     * @param val 值
     */
    void saveCode(String key, Object val);

    /**
     * delete
     * @param key 键
     */
    void delete(String key);

    /**
     * deleteFuzzy
     * @param key 键
     */
    void deleteFuzzy(String key);

    /**
     * 清空缓存
     */
    void deleteAll();

    /**
     *
     * @param redisVos /
     * @param response /
     */
    void download(List<RedisVo> redisVos, HttpServletResponse response) throws IOException;
}
