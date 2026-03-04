package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.re.dao.ReAwardInterfaceDao;
import com.sunten.hrms.re.domain.ReAwardInterface;
import com.sunten.hrms.re.dto.ReAwardInterfaceDTO;
import com.sunten.hrms.re.dto.ReAwardInterfaceQueryCriteria;
import com.sunten.hrms.re.mapper.ReAwardInterfaceMapper;
import com.sunten.hrms.re.service.ReAwardInterfaceService;
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
 * 奖罚情况临时表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReAwardInterfaceServiceImpl extends ServiceImpl<ReAwardInterfaceDao, ReAwardInterface> implements ReAwardInterfaceService {
    private final ReAwardInterfaceDao reAwardInterfaceDao;
    private final ReAwardInterfaceMapper reAwardInterfaceMapper;

    public ReAwardInterfaceServiceImpl(ReAwardInterfaceDao reAwardInterfaceDao, ReAwardInterfaceMapper reAwardInterfaceMapper) {
        this.reAwardInterfaceDao = reAwardInterfaceDao;
        this.reAwardInterfaceMapper = reAwardInterfaceMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReAwardInterfaceDTO insert(ReAwardInterface awardInterfaceNew) {
        reAwardInterfaceDao.insertAllColumn(awardInterfaceNew);
        return reAwardInterfaceMapper.toDto(awardInterfaceNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReAwardInterface awardInterface = new ReAwardInterface();
        awardInterface.setId(id);
        this.delete(awardInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReAwardInterface awardInterface) {
        // TODO    确认删除前是否需要做检查
        reAwardInterfaceDao.deleteByEntityKey(awardInterface);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReAwardInterface awardInterfaceNew) {
        ReAwardInterface awardInterfaceInDb = Optional.ofNullable(reAwardInterfaceDao.getByKey(awardInterfaceNew.getId())).orElseGet(ReAwardInterface::new);
        ValidationUtil.isNull(awardInterfaceInDb.getId(), "AwardInterface", "id", awardInterfaceNew.getId());
        awardInterfaceNew.setId(awardInterfaceInDb.getId());
        reAwardInterfaceDao.updateAllColumnByKey(awardInterfaceNew);
    }

    @Override
    public ReAwardInterfaceDTO getByKey(Long id) {
        ReAwardInterface awardInterface = Optional.ofNullable(reAwardInterfaceDao.getByKey(id)).orElseGet(ReAwardInterface::new);
        ValidationUtil.isNull(awardInterface.getId(), "AwardInterface", "id", id);
        return reAwardInterfaceMapper.toDto(awardInterface);
    }

    @Override
    public List<ReAwardInterfaceDTO> listAll(ReAwardInterfaceQueryCriteria criteria) {
        return reAwardInterfaceMapper.toDto(reAwardInterfaceDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReAwardInterfaceQueryCriteria criteria, Pageable pageable) {
        Page<ReAwardInterface> page = PageUtil.startPage(pageable);
        List<ReAwardInterface> awardInterfaces = reAwardInterfaceDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reAwardInterfaceMapper.toDto(awardInterfaces), page.getTotal());
    }

    @Override
    public void download(List<ReAwardInterfaceDTO> awardInterfaceDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReAwardInterfaceDTO awardInterfaceDTO : awardInterfaceDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("招骋ID", awardInterfaceDTO.getRecruitmentInterface().getId());
            map.put("奖罚类别", awardInterfaceDTO.getType());
            map.put("奖罚名称", awardInterfaceDTO.getAwardName());
            map.put("奖罚处理开始时间", awardInterfaceDTO.getAwardStarTime());
            map.put("奖罚处理结束时间", awardInterfaceDTO.getAwardEndTime());
            map.put("奖罚单位", awardInterfaceDTO.getAwardCompany());
            map.put("奖罚内容", awardInterfaceDTO.getAwardContent());
            map.put("奖罚结果", awardInterfaceDTO.getAwardResult());
            map.put("奖罚金额", awardInterfaceDTO.getAwardMoney());
            map.put("是否有备查资料", awardInterfaceDTO.getReferenceBackupFlag());
            map.put("备注", awardInterfaceDTO.getRemarks());
            map.put("弹性域1", awardInterfaceDTO.getAttribute1());
            map.put("弹性域2", awardInterfaceDTO.getAttribute2());
            map.put("弹性域3", awardInterfaceDTO.getAttribute3());
            map.put("有效标记默认值", awardInterfaceDTO.getEnabledFlag());
            map.put("id", awardInterfaceDTO.getId());
            map.put("createTime", awardInterfaceDTO.getCreateTime());
            map.put("updateBy", awardInterfaceDTO.getUpdateBy());
            map.put("updateTime", awardInterfaceDTO.getUpdateTime());
            map.put("createBy", awardInterfaceDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
