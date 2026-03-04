package com.sunten.hrms.tool.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.tool.dao.ToolLocalStorageDao;
import com.sunten.hrms.tool.domain.ToolLocalStorage;
import com.sunten.hrms.tool.dto.ToolLocalStorageDTO;
import com.sunten.hrms.tool.dto.ToolLocalStorageQueryCriteria;
import com.sunten.hrms.tool.mapper.ToolLocalStorageMapper;
import com.sunten.hrms.tool.service.ToolLocalStorageService;
import com.sunten.hrms.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2019-12-25
 */
@Service
@CacheConfig(cacheNames = "localStorage")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ToolLocalStorageServiceImpl extends ServiceImpl<ToolLocalStorageDao, ToolLocalStorage> implements ToolLocalStorageService {
    private final ToolLocalStorageDao toolLocalStorageDao;
    private final ToolLocalStorageMapper toolLocalStorageMapper;

    @Value("${file.path}")
    private String path;

    @Value("${file.maxSize}")
    private long maxSize;

    public ToolLocalStorageServiceImpl(ToolLocalStorageDao toolLocalStorageDao, ToolLocalStorageMapper toolLocalStorageMapper) {
        this.toolLocalStorageDao = toolLocalStorageDao;
        this.toolLocalStorageMapper = toolLocalStorageMapper;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ToolLocalStorageDTO insert(ToolLocalStorage localStorageNew) {
        toolLocalStorageDao.insertAllColumn(localStorageNew);
        return toolLocalStorageMapper.toDto(localStorageNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ToolLocalStorage localStorage = new ToolLocalStorage();
        localStorage.setId(id);
        this.delete(localStorage);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(ToolLocalStorage localStorage) {
        ToolLocalStorage localStorageInDb = Optional.ofNullable(toolLocalStorageDao.getByKey(localStorage.getId())).orElseGet(ToolLocalStorage::new);
        FileUtil.del(localStorageInDb.getPath());
        toolLocalStorageDao.deleteByEntityKey(localStorageInDb);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ToolLocalStorage localStorageNew) {
        ToolLocalStorage localStorageInDb = Optional.ofNullable(toolLocalStorageDao.getByKey(localStorageNew.getId())).orElseGet(ToolLocalStorage::new);
        ValidationUtil.isNull(localStorageInDb.getId(), "LocalStorage", "id", localStorageNew.getId());
        localStorageNew.setId(localStorageInDb.getId());
        toolLocalStorageDao.updateAllColumnByKey(localStorageNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updateName(ToolLocalStorage localStorageNew) {
        ToolLocalStorage localStorageInDb = Optional.ofNullable(toolLocalStorageDao.getByKey(localStorageNew.getId())).orElseGet(ToolLocalStorage::new);
        ValidationUtil.isNull(localStorageInDb.getId(), "LocalStorage", "id", localStorageNew.getId());
        localStorageNew.setId(localStorageInDb.getId());
        toolLocalStorageDao.updateNameById(localStorageNew);
    }

    @Override
    @Cacheable(key = "#p0")
    public ToolLocalStorageDTO getByKey(Long id) {
        ToolLocalStorage localStorage = Optional.ofNullable(toolLocalStorageDao.getByKey(id)).orElseGet(ToolLocalStorage::new);
        ValidationUtil.isNull(localStorage.getId(), "LocalStorage", "id", id);
        return toolLocalStorageMapper.toDto(localStorage);
    }

    @Override
    @Cacheable
    public List<ToolLocalStorageDTO> listAll(ToolLocalStorageQueryCriteria criteria) {
        return toolLocalStorageMapper.toDto(toolLocalStorageDao.listAllByCriteria(criteria));
    }

    @Override
    @Cacheable
    public Map<String, Object> listAll(ToolLocalStorageQueryCriteria criteria, Pageable pageable) {
        Page<ToolLocalStorage> page = PageUtil.startPage(pageable);
        List<ToolLocalStorage> localStorages = toolLocalStorageDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(toolLocalStorageMapper.toDto(localStorages), page.getTotal());
    }

    @Override
    public void download(List<ToolLocalStorageDTO> localStorageDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ToolLocalStorageDTO localStorageDTO : localStorageDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文件名", localStorageDTO.getRealName());
            map.put("备注名", localStorageDTO.getName());
            map.put("文件类型", localStorageDTO.getType());
            map.put("文件大小", localStorageDTO.getFileSize());
            map.put("操作人", localStorageDTO.getOperate());
            map.put("创建日期", localStorageDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ToolLocalStorageDTO insert(String name, MultipartFile multipartFile) {
        FileUtil.checkSize(maxSize, multipartFile.getSize());
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        // 可自行选择方式
//        String type = FileUtil.getFileTypeByMimeType(suffix);
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, path + type + File.separator);
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException("上传失败");
        }
        try {
            name = StringUtils.isBlank(name) ? FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
            ToolLocalStorage localStorage = new ToolLocalStorage(
                    file.getName(),
                    name,
                    suffix,
                    file.getPath(),
                    type,
                    FileUtil.getSize(multipartFile.getSize()),
                    SecurityUtils.getUsername()
            );
            toolLocalStorageDao.insertAllColumn(localStorage);
            return toolLocalStorageMapper.toDto(localStorage);
        } catch (Exception e) {
            FileUtil.del(file);
            throw e;
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            this.delete(id);
        }
    }
}
