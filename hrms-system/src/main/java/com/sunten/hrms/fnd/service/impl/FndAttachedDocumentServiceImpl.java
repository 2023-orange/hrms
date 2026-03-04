package com.sunten.hrms.fnd.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.fnd.dao.FndAttachedDocumentDao;
import com.sunten.hrms.fnd.domain.FndAttachedDocument;
import com.sunten.hrms.fnd.dto.FndAttachedDocumentDTO;
import com.sunten.hrms.fnd.dto.FndAttachedDocumentQueryCriteria;
import com.sunten.hrms.fnd.mapper.FndAttachedDocumentMapper;
import com.sunten.hrms.fnd.service.FndAttachedDocumentService;
import com.sunten.hrms.fnd.vo.FndAttachedDocumentVo;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
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
 * 附件表 服务实现类
 * </p>
 *
 * @author batan
 * @since 2020-09-25
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FndAttachedDocumentServiceImpl extends ServiceImpl<FndAttachedDocumentDao, FndAttachedDocument> implements FndAttachedDocumentService {
    private final FndAttachedDocumentDao fndAttachedDocumentDao;
    private final FndAttachedDocumentMapper fndAttachedDocumentMapper;

    @Value("${file.attachedDoc}")
    private String attachedDoc;

    public FndAttachedDocumentServiceImpl(FndAttachedDocumentDao fndAttachedDocumentDao, FndAttachedDocumentMapper fndAttachedDocumentMapper) {
        this.fndAttachedDocumentDao = fndAttachedDocumentDao;
        this.fndAttachedDocumentMapper = fndAttachedDocumentMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FndAttachedDocumentDTO insert(FndAttachedDocument attachedDocumentNew) {
        fndAttachedDocumentDao.insertAllColumn(attachedDocumentNew);
        return fndAttachedDocumentMapper.toDto(attachedDocumentNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        FndAttachedDocument attachedDocument = new FndAttachedDocument();
        attachedDocument.setId(id);
        attachedDocument.setEnabledFlag(false);
        this.delete(attachedDocument);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(FndAttachedDocument attachedDocument) {
//        fndAttachedDocumentDao.deleteByEntityKey(attachedDocument);
        fndAttachedDocumentDao.updateEnabledFlag(attachedDocument);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FndAttachedDocument attachedDocumentNew) {
        FndAttachedDocument attachedDocumentInDb = Optional.ofNullable(fndAttachedDocumentDao.getByKey(attachedDocumentNew.getId())).orElseGet(FndAttachedDocument::new);
        ValidationUtil.isNull(attachedDocumentInDb.getId(), "AttachedDocument", "id", attachedDocumentNew.getId());
        attachedDocumentNew.setId(attachedDocumentInDb.getId());
        fndAttachedDocumentDao.updateAllColumnByKey(attachedDocumentNew);
    }

    @Override
    public FndAttachedDocumentDTO getByKey(Long id) {
        FndAttachedDocument attachedDocument = Optional.ofNullable(fndAttachedDocumentDao.getByKey(id)).orElseGet(FndAttachedDocument::new);
        ValidationUtil.isNull(attachedDocument.getId(), "AttachedDocument", "id", id);
        return fndAttachedDocumentMapper.toDto(attachedDocument);
    }

    @Override
    public List<FndAttachedDocumentDTO> listAll(FndAttachedDocumentQueryCriteria criteria) {
        return fndAttachedDocumentMapper.toDto(fndAttachedDocumentDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(FndAttachedDocumentQueryCriteria criteria, Pageable pageable) {
        Page<FndAttachedDocument> page = PageUtil.startPage(pageable);
        List<FndAttachedDocument> attachedDocuments = fndAttachedDocumentDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(fndAttachedDocumentMapper.toDto(attachedDocuments), page.getTotal());
    }

    @Override
    public List<FndAttachedDocumentVo> listAttachedDoc(FndAttachedDocumentQueryCriteria criteria) {
        List<FndAttachedDocument> attachedDocumentList = fndAttachedDocumentDao.listAllByCriteria(criteria);
        List<FndAttachedDocumentVo> voList = new ArrayList<>();
        attachedDocumentList.forEach(doc -> {
            String url = "/attachedDoc/" + doc.getSource() + "/" + doc.getSourceId() + "/" + doc.getType() + "/" + doc.getRealName();
            voList.add(new FndAttachedDocumentVo(doc.getRealName(), url, doc.getId()));
        });
        return voList;
    }

    @Override
    public void download(List<FndAttachedDocumentDTO> attachedDocumentDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FndAttachedDocumentDTO attachedDocumentDTO : attachedDocumentDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("真实文件名", attachedDocumentDTO.getRealName());
            map.put("路径", attachedDocumentDTO.getPath());
            map.put("图像大小", attachedDocumentDTO.getAvaterSize());
            map.put("source", attachedDocumentDTO.getSource());
            map.put("sourceId", attachedDocumentDTO.getSourceId());
            map.put("type", attachedDocumentDTO.getType());
            map.put("id", attachedDocumentDTO.getId());
            map.put("updateTime", attachedDocumentDTO.getUpdateTime());
            map.put("updateBy", attachedDocumentDTO.getUpdateBy());
            map.put("createTime", attachedDocumentDTO.getCreateTime());
            map.put("createBy", attachedDocumentDTO.getCreateBy());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void uploadAttachedDoc(MultipartFile multipartFile, String source, Long sourceId, String type) {
        String filePath = attachedDoc + source + "\\" + sourceId + "\\" + type + "\\";
        File file = FileUtil.upload(multipartFile, filePath);

        FndAttachedDocument newAttachedDocument = new FndAttachedDocument();
        newAttachedDocument.setPath(file.getPath());//路径
        newAttachedDocument.setRealName(file.getName());//照片名称
        newAttachedDocument.setAvaterSize(FileUtil.getSize(multipartFile.getSize()));//图片大小
        newAttachedDocument.setSource(source);
        newAttachedDocument.setSourceId(sourceId);
        newAttachedDocument.setType(type);
        newAttachedDocument.setEnabledFlag(true);
        //插入附件
        fndAttachedDocumentDao.insertAllColumn(newAttachedDocument);
    }
}
