package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndUserAvatar;
import com.sunten.hrms.fnd.dto.FndUserAvatarDTO;
import com.sunten.hrms.fnd.dto.FndUserAvatarQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
public interface FndUserAvatarService extends IService<FndUserAvatar> {

    FndUserAvatarDTO insert(FndUserAvatar userAvatarNew);

    void delete(Long id);

    void update(FndUserAvatar userAvatarNew);

    FndUserAvatarDTO getByKey(Long id);

    List<FndUserAvatarDTO> listAll(FndUserAvatarQueryCriteria criteria);

    Map<String, Object> listAll(FndUserAvatarQueryCriteria criteria, Pageable pageable);

    void download(List<FndUserAvatarDTO> userAvatarDTOS, HttpServletResponse response) throws IOException;
}
