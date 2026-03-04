package com.sunten.hrms.td.service.impl;

import com.sunten.hrms.td.domain.TdTrainingEvaluationScore;
import com.sunten.hrms.td.dao.TdTrainingEvaluationScoreDao;
import com.sunten.hrms.td.service.TdTrainingEvaluationScoreService;
import com.sunten.hrms.td.dto.TdTrainingEvaluationScoreDTO;
import com.sunten.hrms.td.dto.TdTrainingEvaluationScoreQueryCriteria;
import com.sunten.hrms.td.mapper.TdTrainingEvaluationScoreMapper;
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
 * 培训评价分数表 服务实现类
 * </p>
 *
 * @author liangjw
 * @since 2022-03-10
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TdTrainingEvaluationScoreServiceImpl extends ServiceImpl<TdTrainingEvaluationScoreDao, TdTrainingEvaluationScore> implements TdTrainingEvaluationScoreService {
    private final TdTrainingEvaluationScoreDao tdTrainingEvaluationScoreDao;
    private final TdTrainingEvaluationScoreMapper tdTrainingEvaluationScoreMapper;

    public TdTrainingEvaluationScoreServiceImpl(TdTrainingEvaluationScoreDao tdTrainingEvaluationScoreDao, TdTrainingEvaluationScoreMapper tdTrainingEvaluationScoreMapper) {
        this.tdTrainingEvaluationScoreDao = tdTrainingEvaluationScoreDao;
        this.tdTrainingEvaluationScoreMapper = tdTrainingEvaluationScoreMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TdTrainingEvaluationScoreDTO insert(TdTrainingEvaluationScore trainingEvaluationScoreNew) {
        tdTrainingEvaluationScoreDao.insertAllColumn(trainingEvaluationScoreNew);
        return tdTrainingEvaluationScoreMapper.toDto(trainingEvaluationScoreNew);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TdTrainingEvaluationScore trainingEvaluationScore = new TdTrainingEvaluationScore();
        trainingEvaluationScore.setId(id);
        this.delete(trainingEvaluationScore);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(TdTrainingEvaluationScore trainingEvaluationScore) {
        // TODO    确认删除前是否需要做检查
        tdTrainingEvaluationScoreDao.deleteByEntityKey(trainingEvaluationScore);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TdTrainingEvaluationScore trainingEvaluationScoreNew) {
        TdTrainingEvaluationScore trainingEvaluationScoreInDb = Optional.ofNullable(tdTrainingEvaluationScoreDao.getByKey(trainingEvaluationScoreNew.getId())).orElseGet(TdTrainingEvaluationScore::new);
        ValidationUtil.isNull(trainingEvaluationScoreInDb.getId() ,"TrainingEvaluationScore", "id", trainingEvaluationScoreNew.getId());
        trainingEvaluationScoreNew.setId(trainingEvaluationScoreInDb.getId());
        tdTrainingEvaluationScoreDao.updateAllColumnByKey(trainingEvaluationScoreNew);
    }

    @Override
    public TdTrainingEvaluationScoreDTO getByKey(Long id) {
        TdTrainingEvaluationScore trainingEvaluationScore = Optional.ofNullable(tdTrainingEvaluationScoreDao.getByKey(id)).orElseGet(TdTrainingEvaluationScore::new);
        ValidationUtil.isNull(trainingEvaluationScore.getId() ,"TrainingEvaluationScore", "id", id);
        return tdTrainingEvaluationScoreMapper.toDto(trainingEvaluationScore);
    }

    @Override
    public List<TdTrainingEvaluationScoreDTO> listAll(TdTrainingEvaluationScoreQueryCriteria criteria) {
        return tdTrainingEvaluationScoreMapper.toDto(tdTrainingEvaluationScoreDao.listAllByCriteria(criteria));
    }

    @Override
    public Map<String, Object> listAll(TdTrainingEvaluationScoreQueryCriteria criteria, Pageable pageable) {
        Page<TdTrainingEvaluationScore> page = PageUtil.startPage(pageable);
        List<TdTrainingEvaluationScore> trainingEvaluationScores = tdTrainingEvaluationScoreDao.listAllByCriteriaPage(page, criteria);
        return PageUtil.toPage(tdTrainingEvaluationScoreMapper.toDto(trainingEvaluationScores), page.getTotal());
    }

    @Override
    public void download(List<TdTrainingEvaluationScoreDTO> trainingEvaluationScoreDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TdTrainingEvaluationScoreDTO trainingEvaluationScoreDTO : trainingEvaluationScoreDTOS) {
            Map<String, Object> map = new LinkedHashMap<>();
            // TODO
            map.put("课程内容组成及编排", trainingEvaluationScoreDTO.getCourseContent());
            map.put("活动、讨论、练习及案例", trainingEvaluationScoreDTO.getActivityDiscussionExercise());
            map.put("讲师对课程内容的理解程度", trainingEvaluationScoreDTO.getUnderstandingCourse());
            map.put("引导学员思考、鼓励参与", trainingEvaluationScoreDTO.getGuideThink());
            map.put("各种培训方法运用得当，与内容结合紧密", trainingEvaluationScoreDTO.getCombinedWithContent());
            map.put("对现场的把控", trainingEvaluationScoreDTO.getControlOfScene());
            map.put("培训时间安排合理程度", trainingEvaluationScoreDTO.getReasonableTrainingSchedule());
            map.put("课程适合工作需要程度", trainingEvaluationScoreDTO.getJobNeeds());
            map.put("对此课程的吸收与理解程度", trainingEvaluationScoreDTO.getAbsorptionUnderstanding());
            map.put("达到预期目标的程度", trainingEvaluationScoreDTO.getGoalsAchieved());
            map.put("培训实施id", trainingEvaluationScoreDTO.getPlanImplementId());
            map.put("averageScore", trainingEvaluationScoreDTO.getAverageScore());
            map.put("enabledFlag", trainingEvaluationScoreDTO.getEnabledFlag());
            map.put("id", trainingEvaluationScoreDTO.getId());
            map.put("createBy", trainingEvaluationScoreDTO.getCreateBy());
            map.put("updateBy", trainingEvaluationScoreDTO.getUpdateBy());
            map.put("createTime", trainingEvaluationScoreDTO.getCreateTime());
            map.put("updateTime", trainingEvaluationScoreDTO.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
