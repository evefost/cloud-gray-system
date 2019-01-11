package com.xie.server.a.web;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xie.server.a.bo.SiteParams;
import com.xie.server.a.service.ITCyclingBuSiteService;
import com.xie.server.a.vo.CyclingBuSiteVo;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 站点 前端控制器 </p>
 *
 * @author jobob
 * @since 2019-01-08
 */
@RestController
@RequestMapping("/site")
public class TCyclingBuSiteController {

    @Autowired
    private ITCyclingBuSiteService cyclingBuSiteService;

    @ApiOperation(value = "添加用户")
    @RequestMapping(value = "queryList", method = RequestMethod.GET)
    List<CyclingBuSiteVo> queryList(SiteParams params) {
        return cyclingBuSiteService.queryList(params);
    }

    @ApiOperation(value = "添加用户")
    @RequestMapping(value = "queryListByLocation", method = RequestMethod.GET)
    List<CyclingBuSiteVo> queryListByLocation(SiteParams params) {
        return cyclingBuSiteService.queryListByLocation(params);
    }

    @ApiOperation(value = "添加用户")
    @RequestMapping(value = "queryListByPage", method = RequestMethod.GET)
    IPage<CyclingBuSiteVo> queryListByPage(SiteParams params) {
        return cyclingBuSiteService.queryListByPage(params);
    }


}
