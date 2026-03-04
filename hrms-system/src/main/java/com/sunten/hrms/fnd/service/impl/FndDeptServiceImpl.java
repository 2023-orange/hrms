package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.ac.dao.AcLeaveApplicationDao;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dao.FndDeptDao;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.domain.FndUpdateHistory;
import com.sunten.hrms.fnd.dto.FndDeptDTO;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.dto.PieCriteria;
import com.sunten.hrms.fnd.mapper.FndDeptMapper;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.fnd.service.FndUpdateHistoryService;
import com.sunten.hrms.fnd.vo.DeptEmp;
import com.sunten.hrms.fnd.vo.PieVo;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.dao.PmEmployeeJobDao;
import com.sunten.hrms.pm.domain.PmEmployeeJob;
import com.sunten.hrms.pm.dto.PmEmployeeJobQueryCriteria;
import com.sunten.hrms.utils.DateUtil;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@Service
@CacheConfig(cacheNames = "dept")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndDeptServiceImpl extends ServiceImpl<FndDeptDao, FndDept> implements FndDeptService {
    private final FndDeptDao fndDeptDao;
    private final FndDeptMapper fndDeptMapper;
    private final FndUpdateHistoryService fndUpdateHistoryService;
    private final PmEmployeeDao pmEmployeeDao;
    private final PmEmployeeJobDao pmEmployeeJobDao;
    static List<FndDept> childDepts = new ArrayList<>();

    @Autowired
    private FndDeptServiceImpl instance;
    @Qualifier("acLeaveApplicationDao")
    @Autowired
    private AcLeaveApplicationDao acLeaveApplicationDao;

    public FndDeptServiceImpl(FndDeptDao fndDeptDao, FndDeptMapper fndDeptMapper, FndUpdateHistoryService fndUpdateHistoryService, PmEmployeeDao pmEmployeeDao, PmEmployeeJobDao pmEmployeeJobDao) {
        this.fndDeptDao = fndDeptDao;
        this.fndDeptMapper = fndDeptMapper;
        this.fndUpdateHistoryService = fndUpdateHistoryService;
        this.pmEmployeeDao = pmEmployeeDao;
        this.pmEmployeeJobDao = pmEmployeeJobDao;
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FndDeptDTO insert(FndDept deptNew) {
        deptNew.setDeptCode(deptNew.getDeptName());
        FndDeptQueryCriteria criteria = new FndDeptQueryCriteria();
        criteria.setDeleted(false);
        Boolean deptCodeFlag = true;
        do { // 循环验证部门代号是否已存在，存在则往后缀加字符1
            criteria.setDeptCode(deptNew.getDeptCode());
            List<FndDept> depts = fndDeptDao.listAllByCriteria(criteria);
            if (depts != null && depts.size() > 0) {
                deptNew.setDeptCode(deptNew.getDeptCode() + "1");
//                throw new InfoCheckWarningMessException("节点代号" + deptNew.getDeptCode() + "已存在！");
            } else {
                deptCodeFlag = false;
            }
        } while (deptCodeFlag);

        // 自动写入序号
        Long maxSequence = fndDeptDao.getMaxDeptSequence(deptNew.getParentId());
        maxSequence = (maxSequence == null ? 0L : maxSequence);
        if (deptNew.getDeptSequence().equals(0L) || deptNew.getDeptSequence() > maxSequence) {
            deptNew.setDeptSequence(maxSequence + 1L);
        } else {
            fndDeptDao.thanAutoIncrement(deptNew.getDeptSequence(), deptNew.getParentId());
        }

        fndDeptDao.insertAllColumn(deptNew);
        fndDeptDao.updateDeptExtend(deptNew.getId());
        return fndDeptMapper.toDto(deptNew);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        checkSonAndPerson(id);
        FndDept deptTemp = Optional.ofNullable(fndDeptDao.getByKey(id)).orElseGet(FndDept::new);
        FndDept dept = new FndDept();
        dept.setId(id);
        dept.setDeletedFlag(true);
        fndDeptDao.updateDeletedFlag(dept);
        // 删除时序号大于删除部门序号的，同层部门序号-1
        fndDeptDao.lessenDeptSequence(deptTemp.getDeptSequence(), deptTemp.getParentId());
        FndUpdateHistory updateHistory = new FndUpdateHistory();
        updateHistory.setTableName("fnd_dept");
        updateHistory.setColumnName("deletedFlag");
        updateHistory.setTableId(id);
        updateHistory.setNewValue("true");
        updateHistory.setOldValue("false");
        fndUpdateHistoryService.insert(updateHistory);
    }

    @Override
    @Caching(evict = {@CacheEvict(allEntries = true),
            @CacheEvict(value = "user", allEntries = true),
            @CacheEvict(value = "job", allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void update(FndDept deptNew) {
        if (deptNew.getId().equals(deptNew.getParentId())) {
            throw new InfoCheckWarningMessException("上级不能是自己");
        }
        // 获取数据库部门信息
        FndDept deptTemp = Optional.ofNullable(fndDeptDao.getByKey(deptNew.getId())).orElseGet(FndDept::new);
        ValidationUtil.isNull(deptTemp.getId(), "Dept", "id", deptNew.getId());
        deptNew.setId(deptTemp.getId());

        boolean nameChanged = !deptNew.getDeptName().equals(deptTemp.getDeptName());
        boolean parentChanged = !deptTemp.getParentId().equals(deptNew.getParentId());
        boolean levelChanged = !deptTemp.getDeptLevel().equals(deptNew.getDeptLevel());

        Long userId = -1L;
        String timestamp = DateUtil.asLong(LocalDateTime.now()).toString();

        // 当名称或父节点、层次发生变化，先保留该节点及所有下属节点快照
        if (nameChanged || parentChanged || levelChanged) {
            fndDeptDao.createTempSnap(deptNew.getId(), timestamp);
        }

        // 名称变化，更新employeeJob相关部门名称
        if (nameChanged) {
            PmEmployeeJob pmEmployeeJob = new PmEmployeeJob();
            pmEmployeeJob.setDept(deptNew);
            pmEmployeeJobDao.updateDeptName(pmEmployeeJob);
        }
        if (deptTemp.getEnabledFlag() == true && deptNew.getEnabledFlag() == false) {
            // enabledFlag 旧值true 新值 false，进行数据检测
            checkSonAndPerson(deptNew.getId());
        } else {
            // enabledFlag 旧值false 新值 true，进行数据检测
            if (deptTemp.getEnabledFlag() == false && deptNew.getEnabledFlag() == true) {
                checkParent(deptNew.getParentId());
            }
        }

        // 当部门序号改变或者上级部门有变动时
        Long sequence = checkDeptSequence(deptNew, deptTemp);
        if (sequence != null) deptNew.setDeptSequence(sequence);

        fndDeptDao.updateAllColumnByKey(deptNew);

        // 父节点、层次发生变化，更新扩展层次字段
        if (parentChanged || levelChanged) {
            fndDeptDao.updateDeptExtend(deptNew.getId());
        }

        // 当名称或父节点、层次发生变化，插入相关人员的“机构调整”岗位调动数据
        if (nameChanged || parentChanged || levelChanged) {
            String remarks = "部门[" + deptNew.getDeptName() + "]机构调整：";
            remarks += nameChanged ? "名称变更：{" + deptTemp.getDeptName() + "=>" + deptNew.getDeptName() + "};" : "";
            remarks += parentChanged ? "上级变更：{" + instance.getNameById(deptTemp.getParentId()) + "=>" + instance.getNameById(deptNew.getParentId()) + "};" : "";
            remarks += levelChanged ? "层级变更：{" + deptTemp.getDeptLevel() + "=>" + deptNew.getDeptLevel() + "};" : "";
            fndDeptDao.insertOrgAdjJobTransfer(timestamp, userId, LocalDateTime.now(), remarks);
            fndDeptDao.deleteTempSnap(timestamp);
        }

        // 生成字段变更历史
        String columnS = "id,deptName,deptCode,parentId,deptLevel,enabledFlag,sequence";
        fndUpdateHistoryService.insertDomainEqualsResultList("fnd_dept", columnS, deptNew.getId(), deptNew, deptTemp);
    }

    private void checkSonAndPerson(Long id) {
        List<FndDept> fndDepts = fndDeptDao.listByPid(id);
        if (fndDepts != null && fndDepts.size() > 0) {
            String exception = "存在子节点：";
            for (FndDept dept : fndDepts) {
                log.debug(dept.toString());
                if (dept.getEnabledFlag()) {
                    exception += "[" + dept.getDeptName() + "]";
                }
            }
            if (!exception.equals("存在子节点：")) {
                throw new InfoCheckWarningMessException(exception);
            }
        }
        //检查此部门是否存在在职人员信息
        PmEmployeeJobQueryCriteria jobQueryCriteria = new PmEmployeeJobQueryCriteria();
        jobQueryCriteria.setEnabled(true);
        List<Long> depts = new ArrayList<Long>();
        depts.add(id);
        jobQueryCriteria.setDepts(depts);
        List<PmEmployeeJob> pmEmployeeJobs = pmEmployeeJobDao.checkHavePm(jobQueryCriteria);
        if (pmEmployeeJobs.size() > 0) {
            throw new InfoCheckWarningMessException("此部门下存在在职人员,人员数：" + pmEmployeeJobs.size());
        }
    }

    private void checkParent(Long id) {
        FndDept fndDept = fndDeptDao.getByKey(id);
        if (fndDept != null && fndDept.getDeletedFlag() == false && fndDept.getEnabledFlag() == true) {

        } else {
            String exception = "父节点未启动或不存在!";
            throw new InfoCheckWarningMessException(exception);
        }
    }

    private Long checkDeptSequence(FndDept deptNew, FndDept deptTemp) {
        Long resSequence = null;
        Long newSequence = deptNew.getDeptSequence();
        Long tempSequence = deptTemp.getDeptSequence();

        if (deptTemp.getParentId().equals(deptNew.getParentId()) && deptNew.getDeptSequence().equals(deptTemp.getDeptSequence())) {
            // 如果上级部门和序号均未改变，直接返回
            return resSequence;
        }
        Long newMaxSequence = fndDeptDao.getMaxDeptSequence(deptNew.getParentId());
        newMaxSequence = (newMaxSequence == null ? 1L : newMaxSequence); // 新部门最大序号
        Long tempMaxSequence = fndDeptDao.getMaxDeptSequence(deptTemp.getParentId());
        tempMaxSequence = (tempMaxSequence == null ? 1L : tempMaxSequence); // 原部门最大序号
        /**
         * 1、上级部门改变：大于原部门序号的-1，新序号对比新部门序号，大于最大或为0则取最大+1，否则新部门大于该序号的+1
         * 2、上级部门未变、序号改变：为0或大于原部门序号取最大+1，否则判断往前插入还是往后插入
         */
        if (!deptTemp.getParentId().equals(deptNew.getParentId())) { // 1
            fndDeptDao.lessenDeptSequence(tempSequence, deptTemp.getParentId());
            if (newSequence.equals(0L) || newSequence > newMaxSequence) {
                resSequence = newMaxSequence + 1L;
            } else {
                fndDeptDao.thanAutoIncrement(newSequence, deptNew.getParentId());
            }
        } else if (!deptNew.getDeptSequence().equals(deptTemp.getDeptSequence())) { // 2
            if (newSequence.equals(0L) || newSequence > tempMaxSequence) {
                resSequence = tempMaxSequence + 1L;
            } else {
                if (newSequence > tempSequence) { // 往后插入
                    fndDeptDao.inLessen(tempSequence, newSequence, deptNew.getParentId());
                } else { // 往前插入
                    fndDeptDao.inAutoIncrement(newSequence, tempSequence, deptNew.getParentId());
                }
            }
        }
        return resSequence;
    }

    @Override
    @Cacheable(key = "#p0")
    public FndDeptDTO getByKey(Long id) {
        FndDept dept = Optional.ofNullable(fndDeptDao.getByKey(id)).orElseGet(FndDept::new);
        ValidationUtil.isNull(dept.getId(), "Dept", "id", id);
        return fndDeptMapper.toDto(dept);
    }

    @Override
    public List<FndDeptDTO> listAll(FndDeptQueryCriteria criteria) {
        return fndDeptMapper.toDto(fndDeptDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndDeptQueryCriteria criteria, Pageable pageable) {
        Page<FndDept> page = PageUtil.startPage(pageable);
        List<FndDept> depts = fndDeptDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndDeptMapper.toDto(depts), page.getTotal());
    }

    @Override
    public void download(List<FndDeptDTO> deptDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndDeptDTO deptDTO : deptDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("部门名称", deptDTO.getDeptName());
            map.put("部门状态", deptDTO.getEnabledFlag() ? "启用" : "停用");
            map.put("创建日期", deptDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void downloadDeptTree(List<FndDeptDTO> deptDTOS, HttpServletResponse response) throws IOException {
        List<FndDeptDTO> depts = getTreeData(deptDTOS);
        List<Map<String, Object>> list = getDeptChildrens(depts, 0);
        FileUtil.downloadExcelLeft(list, response);
    }

    private List<Map<String, Object>> getDeptChildrens(List<FndDeptDTO> deptDTOS, int i) {
        String str = "  ";
        int k = i;
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndDeptDTO deptDTO : deptDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            StringBuilder deptName = new StringBuilder(deptDTO.getDeptName());
            for (int j = 0; j < k; j++) {
                deptName.insert(0, str);
            }
            map.put("部门名称", deptName);
            list.add(map);
            if (deptDTO.getChildren() != null && deptDTO.getChildren().size() > 0) {
                k += 1;
                // 循环子体，间隔
                list.addAll(getDeptChildrens(deptDTO.getChildren(), k));
            }
        }

        return list;
    }


    @Override
    public Object buildTree(List<FndDeptDTO> deptDTOS, Boolean ignoreTopFlag) {
        System.out.println(ignoreTopFlag);
        log.debug(deptDTOS.toString());
        Set<FndDeptDTO> trees = new LinkedHashSet<>();
        Set<FndDeptDTO> depts = new LinkedHashSet<>();
        FndDeptDTO top = new FndDeptDTO();
        FndDeptDTO leader = new FndDeptDTO();
        if (ignoreTopFlag) {
            top = fndDeptMapper.toDto(fndDeptDao.getByKey(1L));
            leader = fndDeptMapper.toDto(fndDeptDao.getByKey(70L));

            deptDTOS = deptDTOS.stream().filter(fndDeptDTO ->  !"0".equals(fndDeptDTO.getParentId().toString())).collect(Collectors.toList());
        }
        List<String> deptNames = deptDTOS.stream().map(FndDeptDTO::getDeptName).collect(Collectors.toList());
        boolean isChild;
        for (FndDeptDTO deptDTO : deptDTOS) {
            isChild = false;
            if (ignoreTopFlag) {
                if (("70".equals(deptDTO.getParentId().toString()))) {
                    trees.add(deptDTO);
                }
            } else {
                if ("0".equals(deptDTO.getParentId().toString())) {
                    trees.add(deptDTO);
                }
            }
            for (FndDeptDTO it : deptDTOS) {
                if (it.getParentId().equals(deptDTO.getId())) {
                    deptDTO.setUsed(true);
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if (isChild && (null == deptDTO.getUsed() || !deptDTO.getUsed()))
                depts.add(deptDTO);
            else if (!deptNames.contains(instance.getNameById(deptDTO.getParentId())))
                depts.add(deptDTO);
        }
        if (ignoreTopFlag) {
            System.out.println(top);
            trees.add(top);
            trees.add(leader);
        }
        if (CollectionUtils.isEmpty(trees)) {
            trees = depts;
        }
        Integer totalElements = deptDTOS.size();
        Map<String, Object> map = new HashMap<>();
        map.put("totalElements", totalElements);
        map.put("content", CollectionUtils.isEmpty(trees) ? deptDTOS : trees);
        return map;
    }

    @Override
    public Object buildAuthorizationTree(List<FndDeptDTO> deptDTOS) {
        return null;
    }

    @Override
    public Object buildAcTree(List<FndDeptDTO> deptDTOS) {
        Set<FndDeptDTO> trees = new LinkedHashSet<>();
        Set<FndDeptDTO> depts = new LinkedHashSet<>();
        List<String> deptNames = deptDTOS.stream().map(FndDeptDTO::getDeptName).collect(Collectors.toList());
        boolean isChild;
        for (FndDeptDTO deptDTO : deptDTOS) {
            isChild = false;
            if ("0".equals(deptDTO.getParentId().toString())) {
                trees.add(deptDTO);
            }
            for (FndDeptDTO it : deptDTOS) {
                if (it.getParentId().equals(deptDTO.getId())) {
                    deptDTO.setUsed(true);
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if (isChild && (null == deptDTO.getUsed() || !deptDTO.getUsed())) {
                depts.add(deptDTO);
            } else if (!deptNames.contains(instance.getNameById(deptDTO.getParentId()))) {
                depts.add(deptDTO);
            }
        }
        if (CollectionUtils.isEmpty(trees)) {
            trees = depts;
        }
        Integer totalElements = deptDTOS.size();
        Map<String, Object> map = new HashMap<>();
        map.put("totalElements", totalElements);
        map.put("content", CollectionUtils.isEmpty(trees) ? deptDTOS : trees);
        return map;
    }


    private List<FndDeptDTO> getTreeData(List<FndDeptDTO> deptDTOS) {
        Set<FndDeptDTO> trees = new LinkedHashSet<>();
        Set<FndDeptDTO> depts = new LinkedHashSet<>();
        List<String> deptNames = deptDTOS.stream().map(FndDeptDTO::getDeptName).collect(Collectors.toList());
        boolean isChild;
        for (FndDeptDTO deptDTO : deptDTOS) {
            isChild = false;
            if ("0".equals(deptDTO.getParentId().toString())) {
                trees.add(deptDTO);
            }
            for (FndDeptDTO it : deptDTOS) {
                if (it.getParentId().equals(deptDTO.getId())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if (isChild)
                depts.add(deptDTO);
            else if (!deptNames.contains(instance.getNameById(deptDTO.getParentId())))
                depts.add(deptDTO);
        }
        if (CollectionUtils.isEmpty(trees)) {
            trees = depts;
        }

        return CollectionUtils.isEmpty(trees) ? deptDTOS : new ArrayList<>(trees);
    }

    @Override
    public List<FndDept> listByPid(long pid) {
        return fndDeptDao.listByPid(pid);
    }

    @Override
    public List<FndDept> listAllChildrenByPid(Long id) {
        return fndDeptDao.listAllChildrenByPid(id);
    }

    @Override
    public List<Long> listAllEnableChildrenIdByPid(Long id) {
        List<FndDept> depts = this.listAllChildrenByPid(id);
        List<Long> list = new ArrayList<>();
        depts.forEach(dept -> {
                    if (dept != null && dept.getEnabledFlag()) {
                        list.add(dept.getId());
                    }
                }
        );
        return list;
    }

    @Override
    public Set<FndDept> listByRoleId(Long id) {
        return new HashSet<>(fndDeptDao.listByRoleId(id));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchSort(List<FndDept> depts) {
        if (depts != null) {
            for (FndDept dept : depts) {
                fndDeptDao.updateSort(dept);
            }
        }
    }

    @Override
    @Cacheable(key = "'getNameById:' + #p0")
    public String getNameById(Long id) {
        return Optional.ofNullable(fndDeptDao.getNameById(id)).orElse("");
    }

    @Override
    public List<PieVo> getPieVo(PieCriteria criteria) {
        childDepts = new ArrayList<>();
        FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
        fndDeptQueryCriteria.setEnabled(true);
        fndDeptQueryCriteria.setDeleted(false);
        List<FndDept> fndDepts = fndDeptDao.listAllByCriteria(fndDeptQueryCriteria);
        fndDepts = this.getDeptListByPid(fndDepts, criteria.getDeptId());
        List<Long> deptIds = fndDepts.stream().map(FndDept::getId).collect(Collectors.toList());
        deptIds.add(criteria.getDeptId());

        List<PieVo> pieVos = new ArrayList<>();
        if (criteria.getTypeCheckBox().equals("education")) {
            // 获取学历
            pieVos = fndDeptDao.getPieEducation(deptIds);
        }
        if (criteria.getTypeCheckBox().equals("age")) {
            // 年龄
            List<Double> ages = fndDeptDao.getPieAge(deptIds);
            PieVo pieVo1 = new PieVo("小于30", (double) 0);
            PieVo pieVo2 = new PieVo("31~40", (double) 0);
            PieVo pieVo3 = new PieVo("41~50", (double) 0);
            PieVo pieVo4 = new PieVo("51~60", (double) 0);
            PieVo pieVo5 = new PieVo("大于60", (double) 0);
            for (Double age : ages) {
                if (age <= 30) {
                    pieVo1.setValue(pieVo1.getValue() + 1);
                } else if (age <= 40) {
                    pieVo2.setValue(pieVo2.getValue() + 1);
                } else if (age <= 50) {
                    pieVo3.setValue(pieVo3.getValue() + 1);
                } else if (age <= 60) {
                    pieVo4.setValue(pieVo4.getValue() + 1);
                } else {
                    pieVo5.setValue(pieVo5.getValue() + 1);
                }
            }
            pieVos.add(pieVo1);
            pieVos.add(pieVo2);
            pieVos.add(pieVo3);
            pieVos.add(pieVo4);
            pieVos.add(pieVo5);
        }
        if (criteria.getTypeCheckBox().equals("workAge")) {
            // 工龄
            List<Double> workAges = fndDeptDao.getPieWorkAge(deptIds);
            PieVo pieVo1 = new PieVo("小于3", (double) 0);
            PieVo pieVo2 = new PieVo("3~10", (double) 0);
            PieVo pieVo3 = new PieVo("11~20", (double) 0);
            PieVo pieVo4 = new PieVo("21~30", (double) 0);
            PieVo pieVo5 = new PieVo("大于30", (double) 0);
            for (Double age : workAges) {
                if (age <= 3) {
                    pieVo1.setValue(pieVo1.getValue() + 1);
                } else if (age <= 10) {
                    pieVo2.setValue(pieVo2.getValue() + 1);
                } else if (age <= 20) {
                    pieVo3.setValue(pieVo3.getValue() + 1);
                } else if (age <= 30) {
                    pieVo4.setValue(pieVo4.getValue() + 1);
                } else {
                    pieVo5.setValue(pieVo5.getValue() + 1);
                }
            }
            pieVos.add(pieVo1);
            pieVos.add(pieVo2);
            pieVos.add(pieVo3);
            pieVos.add(pieVo4);
            pieVos.add(pieVo5);
        }
        if (criteria.getTypeCheckBox().equals("titleLevel")) {
            // 职称级别
            pieVos = fndDeptDao.getPieVocationalLevel(deptIds);
        }
        if (criteria.getTypeCheckBox().equals("category")) {
            // 职类
            pieVos = fndDeptDao.getPieOccupationCategory(deptIds);
        }
        if (criteria.getTypeCheckBox().equals("jobCategory")) {
            // 职种
            pieVos = fndDeptDao.getPieOccupationType(deptIds);
        }

        return pieVos;
    }

    @Override
    public Set<FndDept> listByJobId(Long id) {
        return new HashSet<>(fndDeptDao.listByJobId(id));
    }

    public DeptEmp getPmByAdminJobId(Long deptId) {
        DeptEmp deptSnapshotEmp = new DeptEmp();
        while (true) {
            deptSnapshotEmp = fndDeptDao.getPmByAdminJobId(deptId);
            if (null == deptSnapshotEmp) {
                FndDeptQueryCriteria fndDeptQueryCriteria = new FndDeptQueryCriteria();
                fndDeptQueryCriteria.setDeptId(deptId);
                List<FndDept> fndDepts = fndDeptDao.getDeptByCriteria(fndDeptQueryCriteria);
                if (fndDepts.size() > 0) {
                    if (fndDepts.get(0).getParentId() == 0L) {
                        return null;
                    } else {
                        deptId = fndDepts.get(0).getParentId();
                        this.getPmByAdminJobId(deptId);
                    }
                } else {
                    return null;
                }
            } else {
                return deptSnapshotEmp;
            }
        }
    }


    // 获取某父节点的所有子节点
    public List<FndDept> getDeptListByPid(List<FndDept> fndDepts, Long pid) {
        for (FndDept fndDept : fndDepts
        ) {
            if (fndDept.getParentId().longValue() == pid.longValue()) {
                getDeptListByPid(fndDepts, fndDept.getId());
                childDepts.add(fndDept);
            }
        }
        return childDepts;
    }

    // 获取某父节点的直接管理的子节点
    public List<FndDept> getDeptListByPid2(List<FndDept> fndDepts, Long pid) {
        List<FndDept> depts = new ArrayList<>();
        for (FndDept fndDept : fndDepts) {
            if (fndDept.getParentId().longValue() == pid.longValue()) {
                depts.add(fndDept);
                if (fndDept.getAdminJobId().equals(-1L)) {
                    depts.addAll(getDeptListByPid2(fndDepts, fndDept.getId()));
                }
            }
        }
        return depts;
    }

    @Override
    public List<FndDeptDTO> listByAuthorization(Long employeeId) {
        List<FndDept> adminDepts = fndDeptDao.getDeptByEmpAndAdminJob(employeeId); // 管理部门
        Set<Long> deptIds = new HashSet<>();
        if (adminDepts.size() > 0) {
            List<FndDept> fndDepts = fndDeptDao.getDeptByAdminBy();
            List<FndDept> resDepts = new LinkedList<>();
            adminDepts.forEach(dept -> {
                deptIds.add(dept.getId());
            });
            resDepts.addAll(adminDepts);
            deptIds.forEach(id -> {
                resDepts.addAll(this.getDeptListByPid2(fndDepts, id));
            });
            List<FndDept> lastList = resDepts.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(FndDept::getId))),
                    ArrayList::new
            ));
            return fndDeptMapper.toDto(lastList);
        } else {
            return new ArrayList<FndDeptDTO>();
        }
    }

    @Override
    public Map<String, Object> getSuperiorByDept(Long employeeId) {
        DeptEmp deptEmp = fndDeptDao.getDeptByEmployeeId(employeeId);
        Map<String, Object> map = getSupperiorMap(deptEmp);
        return map;
    }

    @Override
    public Long getLeaderPmIdByColleague(Long employeeId) {
        DeptEmp deptEmp = fndDeptDao.getDeptByEmployeeId(employeeId);
        System.out.println("deptEmp: " + deptEmp);
        // 判断登录人部门的类型，如果管理人员不为空，则赋值给返回管理人员id
        // 这里已经做了修改，管理人员的试用期考核，第一个父节点不能是自己
        if (deptEmp.getEmployee() != null && deptEmp.getEmployee().getId() != null && !deptEmp.getEmployee().getId().equals(employeeId)) {
            return deptEmp.getEmployee().getId();
        }
        // 获取所有部门
        List<DeptEmp> fndDepts = fndDeptDao.getDeptAndAdminEmployee();
        // 获取参数部门的上级部门
        List<DeptEmp> allSuperiorDepts = getSuperiorDeptById(fndDepts, deptEmp);
        System.out.println("allSuperiorDepts: " + allSuperiorDepts);
        Long currentSupervisorId = null;
        Long currentManagerId = null;
        Long seniorManagerId = null;
        if (allSuperiorDepts.size() >0) {
            for (DeptEmp item : allSuperiorDepts) {
                if ("公司领导".equals(item.getFndDept().getDeptCode()) && null != item.getEmployee()) {
                    seniorManagerId = item.getEmployee().getId();
                } else {
                    if ("科室".equals(item.getFndDept().getDeptLevel()) && null != item.getEmployee()) {
                        currentSupervisorId = item.getEmployee().getId();
                    } else if ("部门".equals(item.getFndDept().getDeptLevel()) && null != item.getEmployee() && !"公司领导".equals(item.getFndDept().getDeptName())) {
                        currentManagerId = item.getEmployee().getId();
                    } else if ("部".equals(item.getFndDept().getDeptLevel()) && null != item.getEmployee()){
                        seniorManagerId = item.getEmployee().getId();
                    }
                }
            }
            if (currentSupervisorId != null) return currentSupervisorId;
            if (currentManagerId != null) return currentManagerId;
            if (seniorManagerId != null) return seniorManagerId;
            return 0L;
        } else {
            /**
             * 如果没有匹配的到，那就找请假审批领导表
             * 1、走到这里都是主管或者经理
             * 2、进行主岗的判断
             */
            String jobName = acLeaveApplicationDao.getJobNameByEmployeeId(employeeId);
            Long deptId = acLeaveApplicationDao.getDeptIdByEmployeeId(employeeId);
            HashMap<String, Object> res = acLeaveApplicationDao.getCurrentMap(deptId);
            if (jobName != null && jobName.contains("主管")) {
                // 主管找经理
                if (res.get("manger_employee_id") != null) {
                    return (Long) res.get("manger_employee_id");
                } else if (res.get("leader_employee_id") != null) {
                    return (Long) res.get("leader_employee_id");
                } else {
                    return 0L;
                }
            } else {
                if (res.get("leader_employee_id") != null) {
                    return (Long) res.get("leader_employee_id");
                } else {
                    return 0L;
                }
            }
        }
    }

    @Override
    public Map<String, Object> getSuperiorByDeptId(Long deptId) {
        DeptEmp deptEmp = fndDeptDao.getDeptAndEmployeeById(deptId);
        Map<String, Object> map = getSupperiorMap(deptEmp);
        return map;
    }

    public Map<String, Object> getSupperiorMap(DeptEmp deptEmp) {
        Map<String, Object> map = new HashMap<>();
        // 获取所有部门
        List<DeptEmp> fndDepts = fndDeptDao.getDeptAndAdminEmployee();
        // 获取参数部门的上级部门
        List<DeptEmp> allSuperiorDepts = getSuperiorDeptById(fndDepts, deptEmp);
        Long deptParentId = null;
        Long superiorParentId = null;
        String currentSupervisor = null;
        String currentManager = null;
        String seniorManager = null;
        String str = deptEmp.getEmployee() == null ? null : deptEmp.getEmployee().getWorkCard();
        // 判断登录人部门的类型，如果管理人员不为空，则赋值给对应角色
        if (deptEmp.getEmployee() != null) {
            if ("科室".equals(deptEmp.getFndDept().getDeptLevel())) {
                currentSupervisor = str;
            } else if ("部门".equals(deptEmp.getFndDept().getDeptLevel())) {
                deptParentId = deptEmp.getFndDept().getId();
                currentManager = str;
            } else if ("部".equals(deptEmp.getFndDept().getDeptLevel())) {
                superiorParentId = deptEmp.getFndDept().getId();
                seniorManager = str;
            }
        }
        System.out.println("当前获取的上部部门数量：" + allSuperiorDepts.size());
        if (allSuperiorDepts.size() > 0) {
            for (DeptEmp item : allSuperiorDepts) {
                if ("公司领导".equals(item.getFndDept().getDeptCode()) && null != item.getEmployee()) {
                    if (null == superiorParentId || item.getFndDept().getParentId().equals(superiorParentId)) {
                        superiorParentId = item.getFndDept().getId();
                        seniorManager = item.getEmployee().getWorkCard();
                    }
                } else {
                    if ("部门".equals(item.getFndDept().getDeptLevel()) && null != item.getEmployee() && !"公司领导".equals(item.getFndDept().getDeptName())) {
                        if (null == deptParentId || item.getFndDept().getParentId().equals(deptParentId)) {
                            deptParentId = item.getFndDept().getId();
                            currentManager = item.getEmployee().getWorkCard();
                        }
                    } else if ("部".equals(item.getFndDept().getDeptLevel()) && null != item.getEmployee()) {
                        if (null == superiorParentId || item.getFndDept().getParentId().equals(superiorParentId)) {
                            superiorParentId = item.getFndDept().getId();
                            seniorManager = item.getEmployee().getWorkCard();
                        }
                    }
                }
            }
        }
        map.put("currentSupervisor", currentSupervisor); // 用人部门主管
        map.put("currentManager", currentManager); // 用人部门经理
        map.put("seniorManager", seniorManager); // 用人部门分管领导

        return map;
    }

    // 获取节点的除公司节点外的所有父级及以上节点,不包括当亲节点
    public List<DeptEmp> getSuperiorDeptById(List<DeptEmp> fndDepts, DeptEmp dept) {
        List<DeptEmp> depts = new ArrayList<>();
        for (DeptEmp fndDept : fndDepts) {
            if (fndDept.getFndDept().getId().longValue() == dept.getFndDept().getParentId().longValue() && !fndDept.getFndDept().getParentId().equals(0L)) {
                depts.add(fndDept);
                if (!fndDept.getFndDept().getParentId().equals(0L)) {
                    depts.addAll(getSuperiorDeptById(fndDepts, fndDept));
                }
            }
        }
        return depts;
    }

    @Override
    public List<FndDeptDTO> getDeptListByIds(List<Long> ids) {
        return fndDeptMapper.toDto(fndDeptDao.getDeptListByIds(ids));
    }

    @Override
    public List<FndDeptDTO> getDeptByEmpAndAdminJob(Long employeeId) {
        return fndDeptMapper.toDto(fndDeptDao.getDeptByEmpAndAdminJob(employeeId));
    }
}
