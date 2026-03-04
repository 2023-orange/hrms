package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.pm.dao.PmEmployeeWorkhistoryDao;
import com.sunten.hrms.pm.dao.PmEmployeeWorkhistoryTempDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeeWorkhistory;
import com.sunten.hrms.pm.domain.PmEmployeeWorkhistoryTemp;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryDTO;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryTempQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeeWorkhistoryCheckMapper;
import com.sunten.hrms.pm.mapper.PmEmployeeWorkhistoryMapper;
import com.sunten.hrms.pm.service.PmEmployeeWorkhistoryService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 工作经历表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeeWorkhistoryServiceImpl extends ServiceImpl<PmEmployeeWorkhistoryDao, PmEmployeeWorkhistory> implements PmEmployeeWorkhistoryService {
    private final PmEmployeeWorkhistoryDao pmEmployeeWorkhistoryDao;
    private final PmEmployeeWorkhistoryMapper pmEmployeeWorkhistoryMapper;
    private final PmEmployeeWorkhistoryCheckMapper pmEmployeeWorkhistoryCheckMapper;
    private final PmEmployeeWorkhistoryTempDao pmEmployeeWorkhistoryTempDao;


    public PmEmployeeWorkhistoryServiceImpl(PmEmployeeWorkhistoryDao pmEmployeeWorkhistoryDao, PmEmployeeWorkhistoryMapper pmEmployeeWorkhistoryMapper, PmEmployeeWorkhistoryCheckMapper pmEmployeeWorkhistoryCheckMapper, PmEmployeeWorkhistoryTempDao pmEmployeeWorkhistoryTempDao) {
        this.pmEmployeeWorkhistoryDao = pmEmployeeWorkhistoryDao;
        this.pmEmployeeWorkhistoryMapper = pmEmployeeWorkhistoryMapper;
        this.pmEmployeeWorkhistoryCheckMapper = pmEmployeeWorkhistoryCheckMapper;
        this.pmEmployeeWorkhistoryTempDao = pmEmployeeWorkhistoryTempDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeeWorkhistoryDTO insert(PmEmployeeWorkhistory employeeWorkhistoryNew) {
        pmEmployeeWorkhistoryDao.insertAllColumn(employeeWorkhistoryNew);
        return pmEmployeeWorkhistoryMapper.toDto(employeeWorkhistoryNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeeWorkhistory employeeWorkhistory = new PmEmployeeWorkhistory();
        employeeWorkhistory.setId(id);
        employeeWorkhistory.setEnabledFlag(false);
        this.delete(employeeWorkhistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeeWorkhistory employeeWorkhistory) {
        //  确认删除前是否需要做检查，只失效，不删除
        pmEmployeeWorkhistoryDao.updateEnableFlag(employeeWorkhistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeeWorkhistory employeeWorkhistoryNew) {
        PmEmployeeWorkhistory employeeWorkhistoryInDb = Optional.ofNullable(pmEmployeeWorkhistoryDao.getByKey(employeeWorkhistoryNew.getId())).orElseGet(PmEmployeeWorkhistory::new);
        ValidationUtil.isNull(employeeWorkhistoryInDb.getId(), "EmployeeWorkhistory", "id", employeeWorkhistoryNew.getId());
        employeeWorkhistoryNew.setId(employeeWorkhistoryInDb.getId());
        pmEmployeeWorkhistoryDao.updateAllColumnByKey(employeeWorkhistoryNew);
    }

    @Override
    public PmEmployeeWorkhistoryDTO getByKey(Long id) {
        PmEmployeeWorkhistory employeeWorkhistory = Optional.ofNullable(pmEmployeeWorkhistoryDao.getByKey(id)).orElseGet(PmEmployeeWorkhistory::new);
        ValidationUtil.isNull(employeeWorkhistory.getId(), "EmployeeWorkhistory", "id", id);
        return pmEmployeeWorkhistoryMapper.toDto(employeeWorkhistory);
    }

    @Override
    public List<PmEmployeeWorkhistoryDTO> listAll(PmEmployeeWorkhistoryQueryCriteria criteria) {
        return pmEmployeeWorkhistoryMapper.toDto(pmEmployeeWorkhistoryDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeeWorkhistoryQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeeWorkhistory> page = PageUtil.startPage(pageable);
        List<PmEmployeeWorkhistory> employeeWorkhistorys = pmEmployeeWorkhistoryDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeeWorkhistoryMapper.toDto(employeeWorkhistorys), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeeWorkhistoryDTO> employeeWorkhistoryDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeeWorkhistoryDTO employeeWorkhistoryDTO : employeeWorkhistoryDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", employeeWorkhistoryDTO.getEmployee().getId());
            map.put("单位", employeeWorkhistoryDTO.getCompany());
            map.put("职务", employeeWorkhistoryDTO.getPost());
            map.put("开始时间", employeeWorkhistoryDTO.getStartTime());
            map.put("结束时间", employeeWorkhistoryDTO.getEndTime());
            map.put("联系电话", employeeWorkhistoryDTO.getTele());
            map.put("备注", employeeWorkhistoryDTO.getRemarks());
            map.put("弹性域1", employeeWorkhistoryDTO.getAttribute1());
            map.put("弹性域2", employeeWorkhistoryDTO.getAttribute2());
            map.put("弹性域3", employeeWorkhistoryDTO.getAttribute3());
            map.put("有效标记默认值", employeeWorkhistoryDTO.getEnabledFlag());
            map.put("id", employeeWorkhistoryDTO.getId());
            map.put("创建时间", employeeWorkhistoryDTO.getCreateTime());
            map.put("创建人ID", employeeWorkhistoryDTO.getCreateBy());
            map.put("修改时间", employeeWorkhistoryDTO.getUpdateTime());
            map.put("修改人ID", employeeWorkhistoryDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<PmEmployeeWorkhistoryDTO> batchInsert(List<PmEmployeeWorkhistory> pmEmployeeWorkhistories, Long employeeId) {
        for (PmEmployeeWorkhistory pew : pmEmployeeWorkhistories) {
            if (employeeId != null) {
                if (pew.getEmployee() == null) {
                    pew.setEmployee(new PmEmployee());
                }
                pew.getEmployee().setId(employeeId);
            }
            if (pew.getEmployee() == null || pew.getEmployee().getId() == null) {
                throw new InfoCheckWarningMessException("员工id不能为空");
            }
            if (pew.getId() != null) {
                if (pew.getId().equals(-1L)) {
                    pmEmployeeWorkhistoryDao.insertAllColumn(pew);
                } else {
                    pmEmployeeWorkhistoryDao.updateAllColumnByKey(pew);
                }
            } else {
                throw new InfoCheckWarningMessException("工作经历批量插入时ID不能为空");
            }
        }

        return pmEmployeeWorkhistoryMapper.toDto(pmEmployeeWorkhistories);
    }

    @Override
    public List<PmEmployeeWorkhistoryDTO> listAllByCheck(Long employeeId) {
        List<PmEmployeeWorkhistory> pmEmployeeWorkhistorys = pmEmployeeWorkhistoryDao.listAllAndTempByEmployee(employeeId);//正式学历及其temp记录sssss

        if (pmEmployeeWorkhistorys.size() > 0) {
            for (PmEmployeeWorkhistory pee : pmEmployeeWorkhistorys) {//循环将temp修改记录添加进children
                pee.setIdKey("P" + pee.getId().toString());
                if (pee.getWorkhistoryTemp() != null) {
                    PmEmployeeWorkhistory tempWorkhistory = pmEmployeeWorkhistoryCheckMapper.toEntity(pee.getWorkhistoryTemp());
                    tempWorkhistory.setId(pee.getWorkhistoryTemp().getId());
                    tempWorkhistory.setIdKey("S" + tempWorkhistory.getId().toString());
                    pee.setChildren(new HashSet<>());
                    pee.getChildren().add(tempWorkhistory);
                    pee.setWorkhistoryTemp(null);
                }
            }
        }
        /*查询新添加的待校核数据*/
        PmEmployeeWorkhistoryTempQueryCriteria WorkhistoryTempQueryCriteria = new PmEmployeeWorkhistoryTempQueryCriteria();
        WorkhistoryTempQueryCriteria.setEnabled(true);//生效
        WorkhistoryTempQueryCriteria.setEmployeeId(employeeId);//员工
        WorkhistoryTempQueryCriteria.setInstructionsFlag("A");
        WorkhistoryTempQueryCriteria.setCheckFlag("D");
        List<PmEmployeeWorkhistoryTemp> pmEmployeeWorkhistoryTemps = pmEmployeeWorkhistoryTempDao.listAllByCriteria(WorkhistoryTempQueryCriteria);
        if (pmEmployeeWorkhistoryTemps.size() > 0) {
            PmEmployeeWorkhistory addTempWorkhistory = new PmEmployeeWorkhistory();
            addTempWorkhistory.setPost("新增");
            addTempWorkhistory.setChildren(new HashSet<>());
            addTempWorkhistory.setIdKey("NEW");
            for (PmEmployeeWorkhistoryTemp peet : pmEmployeeWorkhistoryTemps) {
                PmEmployeeWorkhistory childrenWorkhistory = pmEmployeeWorkhistoryCheckMapper.toEntity(peet);
                childrenWorkhistory.setId(peet.getId());
                childrenWorkhistory.setIdKey("S" + peet.getId().toString());
                addTempWorkhistory.getChildren().add(childrenWorkhistory);
            }
            pmEmployeeWorkhistorys.add(addTempWorkhistory);
        }

        return pmEmployeeWorkhistoryMapper.toDto(pmEmployeeWorkhistorys);
    }
}
