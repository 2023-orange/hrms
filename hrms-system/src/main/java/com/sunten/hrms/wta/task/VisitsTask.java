package com.sunten.hrms.wta.task;

import com.sunten.hrms.fnd.service.FndVisitService;
import org.springframework.stereotype.Component;

/**
 * @author batan
 * @since 2018-12-25
 */
@Component
public class VisitsTask {

    private final FndVisitService fndVisitService;

    public VisitsTask(FndVisitService fndVisitService) {
        this.fndVisitService = fndVisitService;
    }

    public void run(){
        fndVisitService.save();
    }
}
