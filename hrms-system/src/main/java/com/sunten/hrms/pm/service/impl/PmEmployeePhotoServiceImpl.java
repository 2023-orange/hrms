package com.sunten.hrms.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunten.hrms.pm.dao.PmEmployeeDao;
import com.sunten.hrms.pm.dao.PmEmployeePhotoDao;
import com.sunten.hrms.pm.dao.PmPhotoDao;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.domain.PmEmployeePhoto;
import com.sunten.hrms.pm.domain.PmPhoto;
import com.sunten.hrms.pm.dto.PmEmployeePhotoDTO;
import com.sunten.hrms.pm.dto.PmEmployeePhotoQueryCriteria;
import com.sunten.hrms.pm.dto.PmPhotoQueryCriteria;
import com.sunten.hrms.pm.mapper.PmEmployeePhotoMapper;
import com.sunten.hrms.pm.service.PmEmployeePhotoService;
import com.sunten.hrms.pm.service.PmEmployeeService;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.StringUtils;
import com.sunten.hrms.utils.ValidationUtil;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 人员图像表 服务实现类
 * </p>
 *
 * @author xukai
 * @since 2020-09-09
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PmEmployeePhotoServiceImpl extends ServiceImpl<PmEmployeePhotoDao, PmEmployeePhoto> implements PmEmployeePhotoService {
    private final PmEmployeePhotoDao pmEmployeePhotoDao;
    private final PmEmployeePhotoMapper pmEmployeePhotoMapper;
    private final PmEmployeeService pmEmployeeService;
    private final PmEmployeeDao pmEmployeeDao;
    private final PmPhotoDao pmPhotoDao;

    @Value("${file.pmPhoto}")
    private String pmPhoto;
    public PmEmployeePhotoServiceImpl(PmEmployeePhotoDao pmEmployeePhotoDao, PmEmployeePhotoMapper pmEmployeePhotoMapper,
                                      PmEmployeeService pmEmployeeService, PmEmployeeDao pmEmployeeDao, PmPhotoDao pmPhotoDao) {
        this.pmEmployeePhotoDao = pmEmployeePhotoDao;
        this.pmEmployeePhotoMapper = pmEmployeePhotoMapper;
        this.pmEmployeeService = pmEmployeeService;
        this.pmEmployeeDao = pmEmployeeDao;
        this.pmPhotoDao = pmPhotoDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmEmployeePhotoDTO insert(PmEmployeePhoto employeePhotoNew) {
        pmEmployeePhotoDao.insertAllColumn(employeePhotoNew);
        return pmEmployeePhotoMapper.toDto(employeePhotoNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PmEmployeePhoto employeePhoto = new PmEmployeePhoto();
        employeePhoto.setId(id);
        this.delete(employeePhoto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(PmEmployeePhoto employeePhoto) {
        // TODO    确认删除前是否需要做检查
        pmEmployeePhotoDao.deleteByEntityKey(employeePhoto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmEmployeePhoto employeePhotoNew) {
        PmEmployeePhoto employeePhotoInDb = Optional.ofNullable(pmEmployeePhotoDao.getByKey(employeePhotoNew.getId())).orElseGet(PmEmployeePhoto::new);
        ValidationUtil.isNull(employeePhotoInDb.getId(), "EmployeePhoto", "id", employeePhotoNew.getId());
        employeePhotoNew.setId(employeePhotoInDb.getId());
        pmEmployeePhotoDao.updateAllColumnByKey(employeePhotoNew);
    }

    @Override
    public PmEmployeePhotoDTO getByKey(Long id) {
        PmEmployeePhoto employeePhoto = Optional.ofNullable(pmEmployeePhotoDao.getByKey(id)).orElseGet(PmEmployeePhoto::new);
        ValidationUtil.isNull(employeePhoto.getId(), "EmployeePhoto", "id", id);
        return pmEmployeePhotoMapper.toDto(employeePhoto);
    }

    @Override
    public List<PmEmployeePhotoDTO> listAll(PmEmployeePhotoQueryCriteria criteria) {
        return pmEmployeePhotoMapper.toDto(pmEmployeePhotoDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(PmEmployeePhotoQueryCriteria criteria, Pageable pageable) {
        Page<PmEmployeePhoto> page = PageUtil.startPage(pageable);
        List<PmEmployeePhoto> employeePhotos = pmEmployeePhotoDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(pmEmployeePhotoMapper.toDto(employeePhotos), page.getTotal());
    }

    @Override
    public void download(List<PmEmployeePhotoDTO> employeePhotoDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmEmployeePhotoDTO employeePhotoDTO : employeePhotoDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("真实文件名", employeePhotoDTO.getRealName());
            map.put("路径", employeePhotoDTO.getPath());
            map.put("图像大小", employeePhotoDTO.getAvaterSize());
            map.put("id", employeePhotoDTO.getId());
            map.put("updateBy", employeePhotoDTO.getUpdateBy());
            map.put("createBy", employeePhotoDTO.getCreateBy());
            map.put("updateTime", employeePhotoDTO.getUpdateTime());
            map.put("createTime", employeePhotoDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPhotoAuto() throws IOException{
        PmPhotoQueryCriteria pmPhotoQueryCriteria = new PmPhotoQueryCriteria();
//        pmPhotoQueryCriteria.setWorkCard("0260");
        List<PmPhoto> PmPhotos = pmPhotoDao.listAllByCriteria(pmPhotoQueryCriteria);
        for (PmPhoto ph : PmPhotos
        ) {
            //获取旧路径信息
            PmEmployee pmEmployee = pmEmployeeDao.getByKey(ph.getEmployeeId(), null);
//            PmEmployeePhoto employeePhoto = pmEmployee.getEmployeePhoto();
//            String oldPath = "";
            if (pmEmployee.getPhotoId() != null && pmEmployee.getPhotoId() != -1) { // 跳过有旧的
                continue;
//                oldPath = employeePhoto.getPath();
            }

            String filePath = pmPhoto + ph.getEmployeeId() + "\\";
            InputStream inputStream = new ByteArrayInputStream(ph.getPhoto());  //将字节流转换为输入流
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmssS");
            String fileName = "00" + ph.getWorkCard() + "hr照片.jpg";
            String name = FileUtil.getFileNameNoEx(fileName);
            String suffix = FileUtil.getExtensionName(fileName);
            String nowStr = "-" + now.format(formatter);

            String targetFileName = name + nowStr + "." + suffix;
            String path = filePath + targetFileName;
            System.out.println("filePath:" + filePath);
            // 检测是否存在目录
            File dest = new File(path).getCanonicalFile();
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            // 文件写入
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream is = new FileOutputStream(file.getAbsoluteFile());
            IOUtils.copy(inputStream, is);

            // 下面开始进库

            PmEmployeePhoto newPhoto = new PmEmployeePhoto();
            newPhoto.setPath(path);//路径
            newPhoto.setRealName(targetFileName);//照片名称
//            ByteBuffer buffer = ByteBuffer.wrap(ph.getPhoto(), 0,8);
            newPhoto.setAvaterSize(FileUtil.getSize(ph.getImageSize()));//图片大小
            //插入照片信息
            pmEmployeePhotoDao.insertAllColumn(newPhoto);
            //修改人员照片路径
            pmEmployee.setEmployeePhoto(newPhoto);
            pmEmployeeService.updatePhotoPath(pmEmployee);
//            //删除照片物理路径
//            if (StringUtils.isNotBlank(oldPath)) {
//                FileUtil.del(oldPath);
//            }
        }

    }
}
