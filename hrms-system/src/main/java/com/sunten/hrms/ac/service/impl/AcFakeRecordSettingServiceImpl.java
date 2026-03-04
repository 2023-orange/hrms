package com.sunten.hrms.ac.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcFakeRecordSettingDao;
import com.sunten.hrms.ac.domain.AcFakeRecordSetting;
import com.sunten.hrms.ac.dto.AcFakeRecordSettingDTO;
import com.sunten.hrms.ac.dto.AcFakeRecordSettingQueryCriteria;
import com.sunten.hrms.ac.mapper.AcFakeRecordSettingMapper;
import com.sunten.hrms.ac.service.AcFakeRecordSettingService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-12-22
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcFakeRecordSettingServiceImpl extends ServiceImpl<AcFakeRecordSettingDao, AcFakeRecordSetting> implements AcFakeRecordSettingService {
    private final AcFakeRecordSettingDao acFakeRecordSettingDao;
    private final AcFakeRecordSettingMapper acFakeRecordSettingMapper;

    public AcFakeRecordSettingServiceImpl(AcFakeRecordSettingDao acFakeRecordSettingDao, AcFakeRecordSettingMapper acFakeRecordSettingMapper) {
        this.acFakeRecordSettingDao = acFakeRecordSettingDao;
        this.acFakeRecordSettingMapper = acFakeRecordSettingMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcFakeRecordSettingDTO insert(AcFakeRecordSetting fakeRecordSettingNew) {
        acFakeRecordSettingDao.insertAllColumn(fakeRecordSettingNew);
        return acFakeRecordSettingMapper.toDto(fakeRecordSettingNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String username) {
        AcFakeRecordSetting fakeRecordSetting = new AcFakeRecordSetting();
        fakeRecordSetting.setUserName(username);
        this.delete(fakeRecordSetting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcFakeRecordSetting fakeRecordSetting) {
        // TODO    确认删除前是否需要做检查
        acFakeRecordSettingDao.deleteByEntityKey(fakeRecordSetting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcFakeRecordSetting fakeRecordSettingNew) {
        AcFakeRecordSetting fakeRecordSettingInDb = Optional.ofNullable(acFakeRecordSettingDao.getByKey(fakeRecordSettingNew.getUserName())).orElseGet(AcFakeRecordSetting::new);
        acFakeRecordSettingDao.updateAllColumnByKey(fakeRecordSettingNew);
    }

    @Override
    public AcFakeRecordSettingDTO getByKey(String usernmae) {
        AcFakeRecordSetting fakeRecordSetting = Optional.ofNullable(acFakeRecordSettingDao.getByKey(usernmae)).orElseGet(AcFakeRecordSetting::new);
        return acFakeRecordSettingMapper.toDto(fakeRecordSetting);
    }

    @Override
    public List<AcFakeRecordSettingDTO> listAll(AcFakeRecordSettingQueryCriteria criteria) {
        return acFakeRecordSettingMapper.toDto(acFakeRecordSettingDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcFakeRecordSettingQueryCriteria criteria, Pageable pageable) {
        Page<AcFakeRecordSetting> page = PageUtil.startPage(pageable);
        List<AcFakeRecordSetting> fakeRecordSettings = acFakeRecordSettingDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acFakeRecordSettingMapper.toDto(fakeRecordSettings), page.getTotal());
    }

    @Override
    public void download(List<AcFakeRecordSettingDTO> fakeRecordSettingDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcFakeRecordSettingDTO fakeRecordSettingDTO : fakeRecordSettingDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("用户名", fakeRecordSettingDTO.getUserName());
            map.put("有效标记", fakeRecordSettingDTO.getEnabledFlag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void updateEnabled(AcFakeRecordSetting fakeRecordSettingNew) {
        acFakeRecordSettingDao.updateEnabled(fakeRecordSettingNew);
    }
}
