package com.sunten.hrms.pm.service.impl;

import com.sunten.hrms.pm.domain.PmTransferComment;
import com.sunten.hrms.pm.dao.PmTransferCommentDao;
import com.sunten.hrms.pm.service.PmTransferCommentService;
import com.sunten.hrms.pm.dto.PmTransferCommentDTO;
import com.sunten.hrms.pm.dto.PmTransferCommentQueryCriteria;
import com.sunten.hrms.pm.mapper.PmTransferCommentMapper;
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
 * 调动人员流转记录表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2021-05-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmTransferCommentServiceImpl extends ServiceImpl<PmTransferCommentDao, PmTransferComment> implements PmTransferCommentService {
    private final PmTransferCommentDao pmTransferCommentDao;
    private final PmTransferCommentMapper pmTransferCommentMapper;

    public PmTransferCommentServiceImpl(PmTransferCommentDao pmTransferCommentDao, PmTransferCommentMapper pmTransferCommentMapper) {
        this.pmTransferCommentDao = pmTransferCommentDao;
        this.pmTransferCommentMapper = pmTransferCommentMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmTransferCommentDTO insert(PmTransferComment transferCommentNew) {
        pmTransferCommentDao.insertAllColumn(transferCommentNew);
        return pmTransferCommentMapper.toDto(transferCommentNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmTransferComment transferComment = new PmTransferComment();
        transferComment.setId(id);
        this.delete(transferComment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmTransferComment transferComment) {
        // TODO    确认删除前是否需要做检查
        pmTransferCommentDao.deleteByEntityKey(transferComment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmTransferComment transferCommentNew) {
        PmTransferComment transferCommentInDb = Optional.ofNullable(pmTransferCommentDao.getByKey(transferCommentNew.getId())).orElseGet(PmTransferComment::new);
        ValidationUtil.isNull(transferCommentInDb.getId() ,"TransferComment", "id", transferCommentNew.getId());
        transferCommentNew.setId(transferCommentInDb.getId());
        pmTransferCommentDao.updateAllColumnByKey(transferCommentNew);
    }

    @Override
    public PmTransferCommentDTO getByKey(Long id) {
        PmTransferComment transferComment = Optional.ofNullable(pmTransferCommentDao.getByKey(id)).orElseGet(PmTransferComment::new);
        ValidationUtil.isNull(transferComment.getId() ,"TransferComment", "id", id);
        return pmTransferCommentMapper.toDto(transferComment);
    }

    @Override
    public List<PmTransferCommentDTO> listAll(PmTransferCommentQueryCriteria criteria) {
        return pmTransferCommentMapper.toDto(pmTransferCommentDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmTransferCommentQueryCriteria criteria, Pageable pageable) {
        Page<PmTransferComment> page = PageUtil.startPage(pageable);
        List<PmTransferComment> transferComments = pmTransferCommentDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmTransferCommentMapper.toDto(transferComments), page.getTotal());
    }

    @Override
    public void download(List<PmTransferCommentDTO> transferCommentDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmTransferCommentDTO transferCommentDTO : transferCommentDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("调动申请表id", transferCommentDTO.getTransferId());
            map.put("实习通知日期", transferCommentDTO.getInternshipDate());
            map.put("实习记录人", transferCommentDTO.getInternshipBy());
            map.put("实习工资实施日期", transferCommentDTO.getInternshipSalaryDate());
            map.put("实习工资实施记录人", transferCommentDTO.getInternshipSalaryBy());
            map.put("上岗资格证日期", transferCommentDTO.getJobProveDate());
            map.put("上岗资格证审核人", transferCommentDTO.getJobProveBy());
            map.put("上岗通知日期", transferCommentDTO.getGoJobDate());
            map.put("上岗通知记录人", transferCommentDTO.getGoJobBy());
            map.put("实施调入岗薪资日期", transferCommentDTO.getSalaryInformDate());
            map.put("实施调入薪资记录人", transferCommentDTO.getSalaryInformBy());
            map.put("未调动原因", transferCommentDTO.getFailureReazon());
            map.put("未调动日期", transferCommentDTO.getFailureDate());
            map.put("未调动记录人", transferCommentDTO.getFailureBy());
            map.put("借调通知日期", transferCommentDTO.getShortDate());
            map.put("借调通知记录人", transferCommentDTO.getShortBy());
            map.put("借调薪资实施日期", transferCommentDTO.getShortSalaryDate());
            map.put("借调薪资实施记录人", transferCommentDTO.getShortSalaryBy());
            map.put("id", transferCommentDTO.getId());
            map.put("attribute2", transferCommentDTO.getAttribute2());
            map.put("attribute3", transferCommentDTO.getAttribute3());
            map.put("attribute1", transferCommentDTO.getAttribute1());
            map.put("创建时间", transferCommentDTO.getCreateTime());
            map.put("创建人", transferCommentDTO.getCreateBy());
            map.put("最后修改时间", transferCommentDTO.getUpdateTime());
            map.put("最后修改人", transferCommentDTO.getUpdateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
