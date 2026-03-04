package com.sunten.hrms.td.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.td.dao.TdCoursewareDao;
import com.sunten.hrms.td.domain.TdCourseware;
import com.sunten.hrms.td.domain.TdCoursewareEmployee;
import com.sunten.hrms.td.dto.TdCoursewareDTO;
import com.sunten.hrms.td.dto.TdCoursewareQueryCriteria;
import com.sunten.hrms.td.mapper.TdCoursewareMapper;
import com.sunten.hrms.td.service.TdCoursewareService;
import com.sunten.hrms.tool.dao.ToolLocalStorageDao;
import com.sunten.hrms.tool.domain.ToolLocalStorage;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.SecurityUtils;
import com.sunten.hrms.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Value;
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
 * 课件资料表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-06-18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdCoursewareServiceImpl extends ServiceImpl<TdCoursewareDao, TdCourseware> implements TdCoursewareService {

    @Value("${file.courseware}")
    private String courseware;
    @Value("${file.maxSize}")
    private long maxSize;

    private final TdCoursewareDao tdCoursewareDao;
    private final TdCoursewareMapper tdCoursewareMapper;
    private final ToolLocalStorageDao toolLocalStorageDao;

    public TdCoursewareServiceImpl(TdCoursewareDao tdCoursewareDao, TdCoursewareMapper tdCoursewareMapper, ToolLocalStorageDao toolLocalStorageDao) {
        this.tdCoursewareDao = tdCoursewareDao;
        this.tdCoursewareMapper = tdCoursewareMapper;
        this.toolLocalStorageDao = toolLocalStorageDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdCoursewareDTO insert(TdCourseware coursewareNew) {
        tdCoursewareDao.insertAllColumn(coursewareNew);
        return tdCoursewareMapper.toDto(coursewareNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdCourseware courseware = new TdCourseware();
        courseware.setId(id);
        this.delete(courseware);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdCourseware courseware) {
        // TODO    确认删除前是否需要做检查
        tdCoursewareDao.deleteByEntityKey(courseware);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdCourseware coursewareNew) {
        TdCourseware coursewareInDb = Optional.ofNullable(tdCoursewareDao.getByKey(coursewareNew.getId())).orElseGet(TdCourseware::new);
        ValidationUtil.isNull(coursewareInDb.getId() ,"Courseware", "id", coursewareNew.getId());
        coursewareNew.setId(coursewareInDb.getId());
        updateOldCoursewareEmployee(coursewareNew);
        tdCoursewareDao.updateAllColumnByKey(coursewareNew);
    }

    @Override
    public TdCoursewareDTO getByKey(Long id) {
        TdCourseware courseware = Optional.ofNullable(tdCoursewareDao.getByKey(id)).orElseGet(TdCourseware::new);
        ValidationUtil.isNull(courseware.getId() ,"Courseware", "id", id);
        return tdCoursewareMapper.toDto(courseware);
    }

    @Override
    public List<TdCoursewareDTO> listAll(TdCoursewareQueryCriteria criteria) {
        List<TdCourseware> coursewares = tdCoursewareDao.listAllByCriteria(criteria);
        for(TdCourseware courseware : coursewares) {
            courseware.setSelfFlag(courseware.getCreateBy().equals(criteria.getUserId()));
            courseware.setEmployees(tdCoursewareDao.getEmployeeByCoursewareId(courseware.getId()));
        }
        return tdCoursewareMapper.toDto(coursewares);
    }

    @Override
    public Map<String, Object> listAll(TdCoursewareQueryCriteria criteria, Pageable pageable) {
        Page<TdCourseware> page = PageUtil.startPage(pageable);
        List<TdCourseware> coursewares = tdCoursewareDao.listAllByCriteriaPage(page, criteria);
        for(TdCourseware courseware : coursewares) {
            courseware.setSelfFlag(courseware.getCreateBy().equals(criteria.getUserId()));
            courseware.setEmployees(tdCoursewareDao.getEmployeeByCoursewareId(courseware.getId()));
        }

        return PageUtil.toPage(tdCoursewareMapper.toDto(coursewares), page.getTotal());
    }

    @Override
    public void download(List<TdCoursewareDTO> coursewareDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdCoursewareDTO coursewareDTO : coursewareDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("课件名称", coursewareDTO.getName());
            map.put("归属课程或项目类型", coursewareDTO.getDependentProject());
            map.put("归属课程或项目id", coursewareDTO.getDependentId());
            map.put("课件开发人员类别（内部、外部）", coursewareDTO.getEmployeeType());
            map.put("内部人员工号", coursewareDTO.getInWorkCard());
            map.put("内部人员姓名", coursewareDTO.getInName());
            map.put("内部员工id", coursewareDTO.getInEmployeeId());
            map.put("外部人员姓名", coursewareDTO.getOutName());
            map.put("外部人员机构", coursewareDTO.getOutOrganization());
            map.put("课件适用对象", coursewareDTO.getUseObject());
            map.put("课件权限设置", coursewareDTO.getJurisdictionSetting());
            map.put("附件id", coursewareDTO.getStorage().getId());
            map.put("可下载标识", coursewareDTO.getDownloadFlag());
            map.put("有效标记", coursewareDTO.getEnabledFlag());
            map.put("id", coursewareDTO.getId());
            map.put("创建时间", coursewareDTO.getCreateTime());
            map.put("创建人", coursewareDTO.getCreateBy());
            map.put("最后修改时间", coursewareDTO.getUpdateTime());
            map.put("最后修改人", coursewareDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional
    public TdCoursewareDTO attestationCourseware(TdCourseware tdCourseware, MultipartFile multipartFile) {
        ToolLocalStorage editStorage = null; // 用来记录修改时的原附件信息
        if (tdCourseware.getId() != null) {
//            System.out.println("执行修改操作");
            TdCourseware old = Optional.ofNullable(tdCoursewareDao.getByKey(tdCourseware.getId())).orElseGet(TdCourseware::new);
            ValidationUtil.isNull(old.getId() ,"Courseware", "id", tdCourseware.getId());
            editStorage = old.getStorage();
        }
        FileUtil.checkSize(maxSize, multipartFile.getSize());
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String name = FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename());
        // 可自行选择方式
//        String type = FileUtil.getFileTypeByMimeType(suffix);
        String type = FileUtil.getFileType(suffix);
        File file = null;
        if (editStorage != null) {
            file = FileUtil.uploadByPath(multipartFile,editStorage.getPath());
        } else {
            file = FileUtil.upload(multipartFile, courseware + type + File.separator);
        }
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException("上传失败");
        }
        try {
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
            tdCourseware.setStorage(localStorage);
            if (tdCourseware.getId() != null) {
                toolLocalStorageDao.deleteByKey(editStorage.getId()); // 删除原文件信息
                // 判断是否有人员信息存入
                tdCoursewareDao.updateAllColumnByKey(tdCourseware);
            } else {
                // 判断是否有人员信息存入
                tdCoursewareDao.insertAllColumn(tdCourseware);
            }
            updateOldCoursewareEmployee(tdCourseware);
        } catch (Exception e) {
            FileUtil.del(file);
            throw e;
        }

        return tdCoursewareMapper.toDto(tdCourseware);
    }

    private void updateOldCoursewareEmployee(TdCourseware tdCourseware) {
        /**大致更新逻辑：
         *  1、如果没有新记录也没有旧记录，则什么都不做，否则2
         *
         *  2、
         *      2.1 如果有旧记录也有新纪录，说明有过改动，先删除所有旧记录.否则2.2
         *
         *      2.2 如果旧记录大于0，
         * */
        List<TdCoursewareEmployee> oldEmps = tdCoursewareDao.getEmployeeByCoursewareId(tdCourseware.getId()); // 曾经的权限人员
        if (tdCourseware.getJurisdictionSetting() != null
                && "仅限手动选中的人员可见".equals(tdCourseware.getJurisdictionSetting())) {
            List<TdCoursewareEmployee> addList = null;
            List<Long> notDelIds = null;
            if (tdCourseware.getEmployees().size() >0) {
                addList = new LinkedList<>();
                notDelIds = new LinkedList<>();
                for (int i=0; i< tdCourseware.getEmployees().size(); i++) {
                    if (tdCourseware.getEmployees().get(i).getId() == null) {
                        tdCourseware.getEmployees().get(i).setCoursewareId(tdCourseware.getId());
                        addList.add(tdCourseware.getEmployees().get(i));
                    } else {
                        notDelIds.add(tdCourseware.getEmployees().get(i).getId());
                    }
                }
                tdCoursewareDao.deleteEmployeeNotInList(notDelIds,tdCourseware.getId());

                if (addList != null && addList.size() >0) {
                    tdCoursewareDao.insertCoursewareEmployeeList(addList);
                }
            }
        } else {
            if (oldEmps.size() > 0) tdCoursewareDao.deleteEmployeeNotInList(null,tdCourseware.getId());
        }
    }
    @Override
    @Transactional
    public void approval(TdCourseware tdCourseware) {

        tdCoursewareDao.updateByApproval(tdCourseware);
    }

    @Override
    public TdCoursewareDTO getByOaOrder(String oaOrder) {
        return tdCoursewareMapper.toDto(tdCoursewareDao.getByOaOrder(oaOrder));
    }
}
