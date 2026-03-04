package com.sunten.hrms.kpi.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sunten.hrms.kpi.dao.KpiAssessmentDimensionDeptDao;
import com.sunten.hrms.kpi.dao.KpiDepartmentTreeEmployeeDao;
import com.sunten.hrms.kpi.domain.KpiAssessmentDimensionDept;
import com.sunten.hrms.kpi.domain.KpiDepartmentTree;
import com.sunten.hrms.kpi.dao.KpiDepartmentTreeDao;
import com.sunten.hrms.kpi.domain.KpiDepartmentTreeEmployee;
import com.sunten.hrms.kpi.service.KpiDepartmentTreeService;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeDTO;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeQueryCriteria;
import com.sunten.hrms.kpi.mapper.KpiDepartmentTreeMapper;
import com.sunten.hrms.utils.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * KPI部门树表 服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@Service
@CacheConfig(cacheNames = "KpiDept")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KpiDepartmentTreeServiceImpl extends ServiceImpl<KpiDepartmentTreeDao, KpiDepartmentTree> implements KpiDepartmentTreeService {
    private final KpiDepartmentTreeDao kpiDepartmentTreeDao;
    private final KpiDepartmentTreeMapper kpiDepartmentTreeMapper;
    private final KpiAssessmentDimensionDeptDao kpiAssessmentDimensionDeptDao;
    private final KpiDepartmentTreeEmployeeDao kpiDepartmentTreeEmployeeDao;

    @Autowired
    private KpiDepartmentTreeServiceImpl instance;

    public KpiDepartmentTreeServiceImpl(KpiDepartmentTreeDao kpiDepartmentTreeDao, KpiDepartmentTreeMapper kpiDepartmentTreeMapper, KpiAssessmentDimensionDeptDao kpiAssessmentDimensionDeptDao, KpiDepartmentTreeEmployeeDao kpiDepartmentTreeEmployeeDao) {
        this.kpiDepartmentTreeDao = kpiDepartmentTreeDao;
        this.kpiDepartmentTreeMapper = kpiDepartmentTreeMapper;
        this.kpiAssessmentDimensionDeptDao = kpiAssessmentDimensionDeptDao;
        this.kpiDepartmentTreeEmployeeDao = kpiDepartmentTreeEmployeeDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KpiDepartmentTreeDTO insert(KpiDepartmentTree departmentTreeNew) {
        kpiDepartmentTreeDao.insertAllColumn(departmentTreeNew);
        return kpiDepartmentTreeMapper.toDto(departmentTreeNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        KpiDepartmentTree departmentTree = new KpiDepartmentTree();
        departmentTree.setId(id);
        this.delete(departmentTree);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(KpiDepartmentTree departmentTree) {
        // TODO    确认删除前是否需要做检查
        kpiDepartmentTreeDao.deleteByEntityKey(departmentTree);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(KpiDepartmentTree departmentTreeNew) {
        KpiDepartmentTree departmentTreeInDb = Optional.ofNullable(kpiDepartmentTreeDao.getByKey(departmentTreeNew.getId())).orElseGet(KpiDepartmentTree::new);
        ValidationUtil.isNull(departmentTreeInDb.getId() ,"DepartmentTree", "id", departmentTreeNew.getId());
        departmentTreeNew.setId(departmentTreeInDb.getId());
        kpiDepartmentTreeDao.updateAllColumnByKey(departmentTreeNew);
    }

    @Override
    public KpiDepartmentTreeDTO getByKey(Long id) {
        KpiDepartmentTree departmentTree = Optional.ofNullable(kpiDepartmentTreeDao.getByKey(id)).orElseGet(KpiDepartmentTree::new);
        ValidationUtil.isNull(departmentTree.getId() ,"DepartmentTree", "id", id);
        return kpiDepartmentTreeMapper.toDto(departmentTree);
    }

    @Override
    public List<KpiDepartmentTreeDTO> listAll(KpiDepartmentTreeQueryCriteria criteria) {
        List<KpiDepartmentTree> kpiDepartmentTree = kpiDepartmentTreeDao.listAllByCriteria(criteria);
        for (KpiDepartmentTree kpiDepartmentTrees : kpiDepartmentTree) {
            String employees = "";
            String assessmentDimensions = "";
            List<KpiDepartmentTreeEmployee> kpiDepartmentTreeEmployee = kpiDepartmentTreeEmployeeDao.listDepartmentTreeEmployee(kpiDepartmentTrees.getId());
            for (KpiDepartmentTreeEmployee kpiDepartmentTreeEmployees : kpiDepartmentTreeEmployee)
            {
                employees = employees + ' ' +kpiDepartmentTreeEmployees.getName();
            }
            List<KpiAssessmentDimensionDept>  kpiAssessmentDimensionDept = kpiAssessmentDimensionDeptDao.listMultipleChoice(kpiDepartmentTrees.getId());
            for (KpiAssessmentDimensionDept kpiAssessmentDimensionDepts : kpiAssessmentDimensionDept)
            {
                if (kpiAssessmentDimensionDepts != null) {
                    assessmentDimensions = assessmentDimensions + ' ' + kpiAssessmentDimensionDepts.getAssessmentDimension();
                }
            }
            kpiDepartmentTrees.setEmployees(employees);
            kpiDepartmentTrees.setAssessmentDimensions(assessmentDimensions);
        }
        return kpiDepartmentTreeMapper.toDto(kpiDepartmentTree);
    }

    @Override
    public Map<String, Object> listAll(KpiDepartmentTreeQueryCriteria criteria, Pageable pageable) {
        Page<KpiDepartmentTree> page = PageUtil.startPage(pageable);
        List<KpiDepartmentTree> departmentTrees = kpiDepartmentTreeDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(kpiDepartmentTreeMapper.toDto(departmentTrees), page.getTotal());
    }

    @Override
    public void download(List<KpiDepartmentTreeDTO> departmentTreeDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (KpiDepartmentTreeDTO departmentTreeDTO : departmentTreeDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("主键", departmentTreeDTO.getId());
            map.put("年份", departmentTreeDTO.getYear());
            map.put("KPI考核年度编号", departmentTreeDTO.getKpiAnnualId());
            map.put("部门编号", departmentTreeDTO.getDeptId());
            map.put("部门名称", departmentTreeDTO.getDeptName());
            map.put("父级节点id", departmentTreeDTO.getParentId());
            map.put("部门层级", departmentTreeDTO.getDeptLevel());
            map.put("节点序号", departmentTreeDTO.getDeptSequence());
            map.put("序号", departmentTreeDTO.getSequence());
            map.put("部、部门（根据架构树扩展）", departmentTreeDTO.getExtDeptId());
            map.put("科室（根据架构树扩展）", departmentTreeDTO.getExtDepartmentId());
            map.put("班组（根据架构树扩展）", departmentTreeDTO.getExtTeamId());
            map.put("管理岗ID", departmentTreeDTO.getAdminJobId());
            map.put("是否为考核部门", departmentTreeDTO.getAssessmentDepartmentFlag());
            map.put("是否为被考核部门", departmentTreeDTO.getAssessedDepartmentFlag());
            map.put("资料填写人", departmentTreeDTO.getInfoWorkCard());
            map.put("弹性域1", departmentTreeDTO.getAttribute1());
            map.put("弹性域2", departmentTreeDTO.getAttribute2());
            map.put("弹性域3", departmentTreeDTO.getAttribute3());
            map.put("弹性域4", departmentTreeDTO.getAttribute4());
            map.put("弹性域5", departmentTreeDTO.getAttribute5());
            map.put("创建时间", departmentTreeDTO.getCreateTime());
            map.put("创建人", departmentTreeDTO.getCreateBy());
            map.put("更新时间", departmentTreeDTO.getUpdateTime());
            map.put("更新人", departmentTreeDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    // 生成KPI部门树
    @Override
    public Object buildTree(List<KpiDepartmentTreeDTO> kpiDepartmentTreeDTOS, Boolean ignoreTopFlag){
        log.debug(kpiDepartmentTreeDTOS.toString());
        String year = null;
        if (kpiDepartmentTreeDTOS == null) {
             year = kpiDepartmentTreeDTOS.get(0).getYear();
        }
        Set<KpiDepartmentTreeDTO> trees = new LinkedHashSet<>();
        Set<KpiDepartmentTreeDTO> depts = new LinkedHashSet<>();
        KpiDepartmentTreeDTO top = new KpiDepartmentTreeDTO();
        KpiDepartmentTreeDTO leader = new KpiDepartmentTreeDTO();
        if (ignoreTopFlag) {
            top = kpiDepartmentTreeMapper.toDto(kpiDepartmentTreeDao.getByDeptId(1L, year));
            leader = kpiDepartmentTreeMapper.toDto(kpiDepartmentTreeDao.getByDeptId(70L, year));
            kpiDepartmentTreeDTOS = kpiDepartmentTreeDTOS.stream().filter(kpiDepartmentTreeDTO ->  !"0".equals(kpiDepartmentTreeDTO.getParentId().toString())).collect(Collectors.toList());
        }
        List<String> deptNames = kpiDepartmentTreeDTOS.stream().map(KpiDepartmentTreeDTO::getDeptName).collect(Collectors.toList());
        boolean isChild;
        for (KpiDepartmentTreeDTO kpiDepartmentTreeDTO : kpiDepartmentTreeDTOS) {
            isChild = false;
            if (ignoreTopFlag) {
                if (("70".equals(kpiDepartmentTreeDTO.getParentId().toString()))) {
                    trees.add(kpiDepartmentTreeDTO);
                }
            } else {
                if ("0".equals(kpiDepartmentTreeDTO.getParentId().toString())) {
                    trees.add(kpiDepartmentTreeDTO);
                }
            }
            for (KpiDepartmentTreeDTO it : kpiDepartmentTreeDTOS) {
                if ((it.getParentId().equals(kpiDepartmentTreeDTO.getDeptId())) && !(it.getDeptId().toString().equals("70"))) {
                    kpiDepartmentTreeDTO.setUsed(true);
                    isChild = true;
                    if (kpiDepartmentTreeDTO.getChildren() == null) {
                        kpiDepartmentTreeDTO.setChildren(new ArrayList<>());
                    }
                    kpiDepartmentTreeDTO.getChildren().add(it);
                }
            }
            if (isChild && (null == kpiDepartmentTreeDTO.getUsed() || !kpiDepartmentTreeDTO.getUsed()))
                depts.add(kpiDepartmentTreeDTO);
            else if (!deptNames.contains(instance.getNameByDeptId(kpiDepartmentTreeDTO.getParentId(),year)))
                depts.add(kpiDepartmentTreeDTO);
        }
        if (ignoreTopFlag) {
            trees.add(top);
            trees.add(leader);
        }
        if (CollectionUtils.isEmpty(trees)) {
            trees = depts;
        }
        Integer totalElements = kpiDepartmentTreeDTOS.size();
        Map<String, Object> map = new HashMap<>();
        map.put("totalElements", totalElements);
        map.put("content", CollectionUtils.isEmpty(trees) ? kpiDepartmentTreeDTOS : trees);
        return map;
    }


    @Override
    @Cacheable(key = "'getNameByDeptId:' + #p0")
    public String getNameByDeptId(Long id,String year) {
        return Optional.ofNullable(kpiDepartmentTreeDao.getNameByDeptId(id,year)).orElse("");
    }

    @Override
    public void updateByKpiTree(KpiDepartmentTree kpiDepartmentTree) {
        kpiDepartmentTreeDao.updateByKpiTree(kpiDepartmentTree);
        List<Long> kpiAssessmentDimensionDeptOld = new ArrayList<>();
        List<Long> kpiAssessmentDimensionDeptNew = kpiAssessmentDimensionDeptDao.listMultipleChoiceLongList(kpiDepartmentTree.getId());
        Long[] kpiAssessmentDimensionDept = kpiDepartmentTree.getKpiAssessmentDimensionDept();
        for(int i = 0; i < kpiAssessmentDimensionDept.length; i++) {
            kpiAssessmentDimensionDeptOld.add(kpiAssessmentDimensionDept[i]);
        }

        ListUtils.listComp(kpiAssessmentDimensionDeptOld, kpiAssessmentDimensionDeptNew);
        KpiAssessmentDimensionDept kpiAssessmentDimensionDepts = new KpiAssessmentDimensionDept();
        kpiAssessmentDimensionDepts.setDepartmentTreeId(kpiDepartmentTree.getId());

        kpiAssessmentDimensionDeptOld.stream().forEach((id) -> {
            kpiAssessmentDimensionDepts.setAssessmentDimensionId(id);
            kpiAssessmentDimensionDeptDao.insertAllColumn(kpiAssessmentDimensionDepts);
        });
        kpiAssessmentDimensionDeptNew.stream().forEach((id) -> {
            kpiAssessmentDimensionDepts.setAssessmentDimensionId(id);
            kpiAssessmentDimensionDeptDao.deleteByKpiTree(kpiAssessmentDimensionDepts);
        });
    }
}
