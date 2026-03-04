package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.fnd.service.FndVisitService;
import com.sunten.hrms.utils.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author batan
 * @since 2019-12-20
 */
@RestController
@RequestMapping("/api/fnd/visit")
@Api(tags = "系统:访问记录管理")
public class FndVisitController {

    private final FndVisitService fndVisitService;

    public FndVisitController(FndVisitService fndVisitService) {
        this.fndVisitService = fndVisitService;
    }

    @PostMapping
    @ApiOperation("创建访问记录")
    public ResponseEntity create() {
        fndVisitService.count(RequestHolder.getHttpServletRequest());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation("查询")
    public ResponseEntity get() {
        return new ResponseEntity<>(fndVisitService.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/chartData")
    @ApiOperation("查询图表数据")
    public ResponseEntity getChartData() {
        return new ResponseEntity<>(fndVisitService.getChartData(), HttpStatus.OK);
    }
}

