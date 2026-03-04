package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndRoleDao;
import com.sunten.hrms.fnd.dao.FndUserDao;
import com.sunten.hrms.fnd.dao.FndUsersRolesDao;
import com.sunten.hrms.fnd.domain.FndRole;
import com.sunten.hrms.fnd.domain.FndUser;
import com.sunten.hrms.fnd.domain.FndUsersRoles;
import com.sunten.hrms.fnd.service.RedisService;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.td.domain.TdTrainEmployeeJurisdiction;
import com.sunten.hrms.td.dao.TdTrainEmployeeJurisdictionDao;
import com.sunten.hrms.td.service.TdTrainEmployeeJurisdictionService;
import com.sunten.hrms.td.dto.TdTrainEmployeeJurisdictionDTO;
import com.sunten.hrms.td.dto.TdTrainEmployeeJurisdictionQueryCriteria;
import com.sunten.hrms.td.mapper.TdTrainEmployeeJurisdictionMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * 培训员权限表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-06-23
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdTrainEmployeeJurisdictionServiceImpl extends ServiceImpl<TdTrainEmployeeJurisdictionDao, TdTrainEmployeeJurisdiction> implements TdTrainEmployeeJurisdictionService {
    private final TdTrainEmployeeJurisdictionDao tdTrainEmployeeJurisdictionDao;
    private final TdTrainEmployeeJurisdictionMapper tdTrainEmployeeJurisdictionMapper;
    private final PmEmployeeDao pmEmployeeDao;
    private final FndUserDao fndUserDao;
    private final FndRoleDao fndRoleDao;
    private final FndUsersRolesDao fndUsersRolesDao;
    private final RedisService redisService;

    public TdTrainEmployeeJurisdictionServiceImpl(TdTrainEmployeeJurisdictionDao tdTrainEmployeeJurisdictionDao, TdTrainEmployeeJurisdictionMapper tdTrainEmployeeJurisdictionMapper, PmEmployeeDao pmEmployeeDao, FndUserDao fndUserDao, FndRoleDao fndRoleDao, FndUsersRolesDao fndUsersRolesDao, RedisService redisService) {
        this.tdTrainEmployeeJurisdictionDao = tdTrainEmployeeJurisdictionDao;
        this.tdTrainEmployeeJurisdictionMapper = tdTrainEmployeeJurisdictionMapper;
        this.pmEmployeeDao = pmEmployeeDao;
        this.fndUserDao = fndUserDao;
        this.fndRoleDao = fndRoleDao;
        this.fndUsersRolesDao = fndUsersRolesDao;
        this.redisService = redisService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdTrainEmployeeJurisdictionDTO insert(TdTrainEmployeeJurisdiction trainEmployeeJurisdictionNew) {
        // 判断该员工权限是否已存在
        List<Long> depts = tdTrainEmployeeJurisdictionDao.getDeptsByEmployeeeId(trainEmployeeJurisdictionNew.getEmployeeId());
        if (depts.size() >0) {
            throw new InfoCheckWarningMessException("该培训员已存在，请勿重复新增");
        }
        trainEmployeeJurisdictionNew.getDeptIds().forEach(id -> {
            TdTrainEmployeeJurisdiction employeeJurisdiction = new TdTrainEmployeeJurisdiction();
            employeeJurisdiction.setDeptId(id);
            employeeJurisdiction.setEnabledFlag(true);
            employeeJurisdiction.setEmployeeId(trainEmployeeJurisdictionNew.getEmployeeId());
            tdTrainEmployeeJurisdictionDao.insertAllColumn(employeeJurisdiction);
        });
        // 1、获取当前新增人员信息
        PmEmployee employee = pmEmployeeDao.getByKey(trainEmployeeJurisdictionNew.getEmployeeId(),null);
        FndUser user = fndUserDao.getByUsername(employee.getWorkCard());
        // 2、获取培训员角色
        FndRole role = fndRoleDao.getByName("培训员");
        FndUsersRoles usersRoles = new FndUsersRoles();
        // 3、给新增员工填写培训员角色
        usersRoles.setUserId(user.getId());
        usersRoles.setRoleId(role.getId());
        fndUsersRolesDao.insertAllColumn(usersRoles);
        // 如果用户的角色改变了，需要手动清理下缓存
        String key = "role::loadPermissionByUser:" + user.getId();
        redisService.delete(key);
        key = "role::findByUserId:" + user.getId();
        redisService.delete(key);

        return tdTrainEmployeeJurisdictionMapper.toDto(trainEmployeeJurisdictionNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdTrainEmployeeJurisdiction trainEmployeeJurisdiction = new TdTrainEmployeeJurisdiction();
        trainEmployeeJurisdiction.setId(id);
        this.delete(trainEmployeeJurisdiction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdTrainEmployeeJurisdiction trainEmployeeJurisdiction) {
        // TODO    确认删除前是否需要做检查
        tdTrainEmployeeJurisdictionDao.deleteByEntityKey(trainEmployeeJurisdiction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdTrainEmployeeJurisdiction trainEmployeeJurisdictionNew) {
        TdTrainEmployeeJurisdiction trainEmployeeJurisdictionInDb = Optional.ofNullable(tdTrainEmployeeJurisdictionDao.getByKey(trainEmployeeJurisdictionNew.getId())).orElseGet(TdTrainEmployeeJurisdiction::new);
        ValidationUtil.isNull(trainEmployeeJurisdictionInDb.getId() ,"TrainEmployeeJurisdiction", "id", trainEmployeeJurisdictionNew.getId());
        trainEmployeeJurisdictionNew.setId(trainEmployeeJurisdictionInDb.getId());
        tdTrainEmployeeJurisdictionDao.updateAllColumnByKey(trainEmployeeJurisdictionNew);
    }

    @Override
    public TdTrainEmployeeJurisdictionDTO getByKey(Long id) {
        TdTrainEmployeeJurisdiction trainEmployeeJurisdiction = Optional.ofNullable(tdTrainEmployeeJurisdictionDao.getByKey(id)).orElseGet(TdTrainEmployeeJurisdiction::new);
        ValidationUtil.isNull(trainEmployeeJurisdiction.getId() ,"TrainEmployeeJurisdiction", "id", id);
        return tdTrainEmployeeJurisdictionMapper.toDto(trainEmployeeJurisdiction);
    }

    @Override
    public List<TdTrainEmployeeJurisdictionDTO> listAll(TdTrainEmployeeJurisdictionQueryCriteria criteria) {
        return tdTrainEmployeeJurisdictionMapper.toDto(tdTrainEmployeeJurisdictionDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdTrainEmployeeJurisdictionQueryCriteria criteria, Pageable pageable) {
        Page<TdTrainEmployeeJurisdiction> page = PageUtil.startPage(pageable);
        List<TdTrainEmployeeJurisdiction> trainEmployeeJurisdictions = tdTrainEmployeeJurisdictionDao.listAllByCriteriaPage(page, criteria);
        List<TdTrainEmployeeJurisdictionDTO> tdTrainEmployeeJurisdictionDTOS = tdTrainEmployeeJurisdictionMapper.toDto(trainEmployeeJurisdictions);
        return PageUtil.toPage(tdTrainEmployeeJurisdictionDTOS, page.getTotal());
    }

    @Override
    public void download(List<TdTrainEmployeeJurisdictionDTO> trainEmployeeJurisdictionDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdTrainEmployeeJurisdictionDTO trainEmployeeJurisdictionDTO : trainEmployeeJurisdictionDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("员工id", trainEmployeeJurisdictionDTO.getPmEmployee().getId());
            map.put("部门id", trainEmployeeJurisdictionDTO.getDeptId());
            map.put("删除标识", trainEmployeeJurisdictionDTO.getEnabledFlag());
            map.put("attribute3", trainEmployeeJurisdictionDTO.getAttribute3());
            map.put("attribute1", trainEmployeeJurisdictionDTO.getAttribute1());
            map.put("id", trainEmployeeJurisdictionDTO.getId());
            map.put("attribute2", trainEmployeeJurisdictionDTO.getAttribute2());
            map.put("创建时间", trainEmployeeJurisdictionDTO.getCreateTime());
            map.put("创建人ID", trainEmployeeJurisdictionDTO.getCreateBy());
            map.put("修改时间", trainEmployeeJurisdictionDTO.getUpdateTime());
            map.put("修改人ID", trainEmployeeJurisdictionDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional
    public void batchUpdate(TdTrainEmployeeJurisdiction trainEmployeeJurisdictionNew) {
        if (trainEmployeeJurisdictionNew.getAddList() != null && trainEmployeeJurisdictionNew.getAddList().size() > 0) {
            trainEmployeeJurisdictionNew.getAddList().forEach(id -> {
                TdTrainEmployeeJurisdiction employeeJurisdiction = new TdTrainEmployeeJurisdiction();
                employeeJurisdiction.setDeptId(id);
                employeeJurisdiction.setEnabledFlag(true);
                employeeJurisdiction.setEmployeeId(trainEmployeeJurisdictionNew.getEmployeeId());
                tdTrainEmployeeJurisdictionDao.insertAllColumn(employeeJurisdiction);
            });
        }
        if (trainEmployeeJurisdictionNew.getDelList() != null && trainEmployeeJurisdictionNew.getDelList().size() > 0) {
            trainEmployeeJurisdictionNew.getDelList().forEach(deptId -> {
                tdTrainEmployeeJurisdictionDao.deleteByEmployeeAndDept(trainEmployeeJurisdictionNew.getEmployeeId(),deptId);
            });
        }
    }

    @Override
    public List<Long> getDeptsByEmployeeeId(Long employeeId) {
        return tdTrainEmployeeJurisdictionDao.getDeptsByEmployeeeId(employeeId);
    }

    @Override
    @Transactional
    public void deleteByEmployee(Long employeeId) {
        tdTrainEmployeeJurisdictionDao.deleteByEmployee(employeeId);
        // 删除培训员角色
        // 1、获取当前新增人员信息
        PmEmployee employee = pmEmployeeDao.getByKey(employeeId,null);
        FndUser user = fndUserDao.getByUsername(employee.getWorkCard());
        // 2、获取培训员角色并删除
        FndRole role = fndRoleDao.getByName("培训员");
        FndUsersRoles fndUsersRoles = fndUsersRolesDao.getByKey(user.getId(),role.getId());
        if (fndUsersRoles != null) {
            fndUsersRolesDao.deleteByKey(user.getId(),role.getId());
            // 如果用户的角色改变了，需要手动清理下缓存
            String key = "role::loadPermissionByUser:" + user.getId();
            redisService.delete(key);
            key = "role::findByUserId:" + user.getId();
            redisService.delete(key);
        }
    }
}
