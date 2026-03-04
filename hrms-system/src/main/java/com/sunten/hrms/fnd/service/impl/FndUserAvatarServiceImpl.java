package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndUserAvatarDao;
import com.sunten.hrms.fnd.domain.FndUserAvatar;
import com.sunten.hrms.fnd.dto.FndUserAvatarDTO;
import com.sunten.hrms.fnd.dto.FndUserAvatarQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndUserAvatarMapper;
import com.sunten.hrms.fnd.service.FndUserAvatarService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Service
@CacheConfig(cacheNames = "userAvatar")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndUserAvatarServiceImpl extends ServiceImpl<FndUserAvatarDao, FndUserAvatar> implements FndUserAvatarService {
    private final FndUserAvatarDao fndUserAvatarDao;
    private final FndUserAvatarMapper fndUserAvatarMapper;

    public FndUserAvatarServiceImpl(FndUserAvatarDao fndUserAvatarDao, FndUserAvatarMapper fndUserAvatarMapper) {
        this.fndUserAvatarDao = fndUserAvatarDao;
        this.fndUserAvatarMapper = fndUserAvatarMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndUserAvatarDTO insert(FndUserAvatar userAvatarNew) {
        fndUserAvatarDao.insertAllColumn(userAvatarNew);
        return fndUserAvatarMapper.toDto(userAvatarNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        fndUserAvatarDao.deleteByKey(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FndUserAvatar userAvatarNew) {
        FndUserAvatar userAvatarTemp = Optional.ofNullable(fndUserAvatarDao.getByKey(userAvatarNew.getId())).orElseGet(FndUserAvatar::new);
        ValidationUtil.isNull(userAvatarTemp.getId(), "UserAvatar", "id", userAvatarNew.getId());
        userAvatarNew.setId(userAvatarTemp.getId());
        fndUserAvatarDao.updateAllColumnByKey(userAvatarNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public FndUserAvatarDTO getByKey(Long id) {
        FndUserAvatar userAvatar = Optional.ofNullable(fndUserAvatarDao.getByKey(id)).orElseGet(FndUserAvatar::new);
        ValidationUtil.isNull(userAvatar.getId(), "UserAvatar", "id", id);
        return fndUserAvatarMapper.toDto(userAvatar);
    }

    @Override
    @Cacheable
    public List<FndUserAvatarDTO> listAll(FndUserAvatarQueryCriteria criteria) {
        return fndUserAvatarMapper.toDto(fndUserAvatarDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(FndUserAvatarQueryCriteria criteria, Pageable pageable) {
        Page<FndUserAvatar> page = PageUtil.startPage(pageable);
        List<FndUserAvatar> userAvatars = fndUserAvatarDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndUserAvatarMapper.toDto(userAvatars), page.getTotal());
    }

    @Override
    public void download(List<FndUserAvatarDTO> userAvatarDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndUserAvatarDTO userAvatarDTO : userAvatarDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", userAvatarDTO.getId());
            map.put("realName", userAvatarDTO.getRealName());
            map.put("path", userAvatarDTO.getPath());
            map.put("avatarSize", userAvatarDTO.getAvatarSize());
            map.put("createTime", userAvatarDTO.getCreateTime());
            map.put("createBy", userAvatarDTO.getCreateBy());
            map.put("updateTime", userAvatarDTO.getUpdateTime());
            map.put("updateBy", userAvatarDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
