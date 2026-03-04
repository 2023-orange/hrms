package com.sunten.hrms.ac.service.impl;

import com.sunten.hrms.ac.domain.AcBeLateDate;
import com.sunten.hrms.ac.dao.AcBeLateDateDao;
import com.sunten.hrms.ac.service.AcBeLateDateService;
import com.sunten.hrms.ac.dto.AcBeLateDateDTO;
import com.sunten.hrms.ac.dto.AcBeLateDateQueryCriteria;
import com.sunten.hrms.ac.mapper.AcBeLateDateMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;

/**
 * <p>
 * 厂车迟到时间记录表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-07-08
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcBeLateDateServiceImpl extends ServiceImpl<AcBeLateDateDao, AcBeLateDate> implements AcBeLateDateService {
    private final AcBeLateDateDao acBeLateDateDao;
    private final AcBeLateDateMapper acBeLateDateMapper;

    public AcBeLateDateServiceImpl(AcBeLateDateDao acBeLateDateDao, AcBeLateDateMapper acBeLateDateMapper) {
        this.acBeLateDateDao = acBeLateDateDao;
        this.acBeLateDateMapper = acBeLateDateMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcBeLateDateDTO insert(AcBeLateDate beLateDateNew) {
        AcBeLateDate beLateDate = acBeLateDateDao.getByDay(beLateDateNew.getBeLateDate());
        if (beLateDate != null) throw new IndexOutOfBoundsException("该迟到日期已存在，请勿重复新增");
        acBeLateDateDao.insertAllColumn(beLateDateNew);
        return acBeLateDateMapper.toDto(beLateDateNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AcBeLateDate beLateDate = new AcBeLateDate();
        beLateDate.setId(id);
        beLateDate.setEnabledFlag(false);
        this.delete(beLateDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(AcBeLateDate beLateDate) {
        // 删除时不做物理删除，只失效该记录，以留备份
//        acBeLateDateDao.deleteByEntityKey(beLateDate);
        acBeLateDateDao.updateEnableByKey(beLateDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcBeLateDate beLateDateNew) {
        AcBeLateDate beLateDateInDb = Optional.ofNullable(acBeLateDateDao.getByKey(beLateDateNew.getId())).orElseGet(AcBeLateDate::new);
        ValidationUtil.isNull(beLateDateInDb.getId() ,"BeLateDate", "id", beLateDateNew.getId());
        AcBeLateDate beLateDate = acBeLateDateDao.getByDay(beLateDateNew.getBeLateDate());
        if (beLateDate != null && !beLateDate.getId().equals(beLateDateInDb.getId())) throw new IndexOutOfBoundsException("该迟到日期已存在，不能重复修改");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
        beLateDateNew.setId(beLateDateInDb.getId());
        // 保留修改历史，如果修改次数过多可能导致数据库存储长度不够，出现此错误再说
        String attribute1 = beLateDateInDb.getAttribute1();
        if (attribute1 != null && !"".equals(attribute1)) {
            attribute1 += "," + beLateDateInDb.getUpdateBy() + "-" + formatter.format(beLateDateInDb.getUpdateTime());
        } else {
            attribute1 = beLateDateInDb.getUpdateBy() + "-" + formatter.format(beLateDateInDb.getUpdateTime());
        }
        String attribute2 = beLateDateInDb.getAttribute2();
        if (attribute2 != null && !"".equals(attribute2)) {
            attribute2 += "," + formatter1.format(beLateDateInDb.getBeLateDate()) + " " + formatter2.format(beLateDateInDb.getBeLateTime());
        } else {
            attribute2 = formatter1.format(beLateDateInDb.getBeLateDate()) + " " + formatter2.format(beLateDateInDb.getBeLateTime());
        }
        beLateDateNew.setAttribute1(attribute1);
        beLateDateNew.setAttribute2(attribute2);
        acBeLateDateDao.updateAllColumnByKey(beLateDateNew);
    }

    @Override
    public AcBeLateDateDTO getByKey(Long id) {
        AcBeLateDate beLateDate = Optional.ofNullable(acBeLateDateDao.getByKey(id)).orElseGet(AcBeLateDate::new);
        ValidationUtil.isNull(beLateDate.getId() ,"BeLateDate", "id", id);
        return acBeLateDateMapper.toDto(beLateDate);
    }

    @Override
    public AcBeLateDateDTO getByDay(LocalDate lateDate) {
        AcBeLateDate beLateDate = acBeLateDateDao.getByDay(lateDate);
        return acBeLateDateMapper.toDto(beLateDate);
    }

    @Override
    public List<AcBeLateDateDTO> listAll(AcBeLateDateQueryCriteria criteria) {
        return acBeLateDateMapper.toDto(acBeLateDateDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(AcBeLateDateQueryCriteria criteria, Pageable pageable) {
        Page<AcBeLateDate> page = PageUtil.startPage(pageable);
        List<AcBeLateDate> beLateDates = acBeLateDateDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(acBeLateDateMapper.toDto(beLateDates), page.getTotal());
    }

    @Override
    public void download(List<AcBeLateDateDTO> beLateDateDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcBeLateDateDTO beLateDateDTO : beLateDateDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("迟到日期", beLateDateDTO.getBeLateDate());
            map.put("迟到延迟时间，默认9:00", beLateDateDTO.getBeLateTime());
            map.put("年份", beLateDateDTO.getBeLateYear());
            map.put("月份", beLateDateDTO.getBeLateMonth());
            map.put("弹性域1", beLateDateDTO.getAttribute1());
            map.put("弹性域2", beLateDateDTO.getAttribute2());
            map.put("弹性域3", beLateDateDTO.getAttribute3());
            map.put("id", beLateDateDTO.getId());
            map.put("createTime", beLateDateDTO.getCreateTime());
            map.put("updateBy", beLateDateDTO.getUpdateBy());
            map.put("updateTime", beLateDateDTO.getUpdateTime());
            map.put("createBy", beLateDateDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
