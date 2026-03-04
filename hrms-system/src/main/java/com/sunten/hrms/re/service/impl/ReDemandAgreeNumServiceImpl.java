package com.sunten.hrms.re.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.re.*;
import com.sunten.hrms.re.dao.ReDemandAgreeNumDao;
import com.sunten.hrms.re.domain.ReDemandAgreeNum;
import com.sunten.hrms.re.dto.ReDemandAgreeNumDTO;
import com.sunten.hrms.re.dto.ReDemandAgreeNumQueryCriteria;
import com.sunten.hrms.re.mapper.ReDemandAgreeNumMapper;
import com.sunten.hrms.re.service.ReDemandAgreeNumService;
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
 *  服务实现类
 * </p>
 *
 * @author zhoujy
 * @since 2022-11-22
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReDemandAgreeNumServiceImpl extends ServiceImpl<ReDemandAgreeNumDao, ReDemandAgreeNum> implements ReDemandAgreeNumService {
    private final ReDemandAgreeNumDao reDemandAgreeNumDao;
    private final ReDemandAgreeNumMapper reDemandAgreeNumMapper;

    public ReDemandAgreeNumServiceImpl(ReDemandAgreeNumDao reDemandAgreeNumDao, ReDemandAgreeNumMapper reDemandAgreeNumMapper) {
        this.reDemandAgreeNumDao = reDemandAgreeNumDao;
        this.reDemandAgreeNumMapper = reDemandAgreeNumMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReDemandAgreeNumDTO insert(ReDemandAgreeNum reDemandAgreeNumNew) {
        reDemandAgreeNumDao.insertAllColumn(reDemandAgreeNumNew);
        return reDemandAgreeNumMapper.toDto(reDemandAgreeNumNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ReDemandAgreeNum reDemandAgreeNum = new ReDemandAgreeNum();
        reDemandAgreeNum.setId(id);
        this.delete(reDemandAgreeNum);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReDemandAgreeNum reDemandAgreeNum) {
        // TODO    确认删除前是否需要做检查
        reDemandAgreeNumDao.deleteByEntityKey(reDemandAgreeNum);
    }


    @Override
    public ReDemandAgreeNumDTO getByKey(Long id) {
        ReDemandAgreeNum reDemandAgreeNum = Optional.ofNullable(reDemandAgreeNumDao.getByKey(id)).orElseGet(ReDemandAgreeNum::new);
        ValidationUtil.isNull(reDemandAgreeNum.getId() ,"ReDemandAgreeNum", "id", id);
        return reDemandAgreeNumMapper.toDto(reDemandAgreeNum);
    }

    @Override
    public List<ReDemandAgreeNumDTO> listAll(ReDemandAgreeNumQueryCriteria criteria) {
        return reDemandAgreeNumMapper.toDto(reDemandAgreeNumDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(ReDemandAgreeNumQueryCriteria criteria, Pageable pageable) {
        Page<ReDemandAgreeNum> page = PageUtil.startPage(pageable);
        List<ReDemandAgreeNum> reDemandAgreeNums = reDemandAgreeNumDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(reDemandAgreeNumMapper.toDto(reDemandAgreeNums), page.getTotal());
    }

    @Override
    public void download(List<ReDemandAgreeNumDTO> reDemandAgreeNumDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReDemandAgreeNumDTO reDemandAgreeNumDTO : reDemandAgreeNumDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("id", reDemandAgreeNumDTO.getId());
            map.put("需求id", reDemandAgreeNumDTO.getDemandId());
            map.put("同意用人数", reDemandAgreeNumDTO.getAgreeNum());
            map.put("修改人姓名", reDemandAgreeNumDTO.getEditBy());
            map.put("修改时间", reDemandAgreeNumDTO.getCreateTime());
            map.put("创建人工牌号", reDemandAgreeNumDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
