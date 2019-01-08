package com.xie.server.a.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.server.a.bo.SiteParams;
import com.xie.server.a.entity.TCyclingBuSite;
import com.xie.server.a.vo.CyclingBuSiteVo;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 站点 服务类
 * </p>
 *
 * @author jobob
 * @since 2019-01-08
 */
public interface ITCyclingBuSiteService extends IService<TCyclingBuSite> {


    List<CyclingBuSiteVo> queryList(SiteParams params);

    IPage<CyclingBuSiteVo> queryListByPage(SiteParams params);

    void addBatch(Collection<TCyclingBuSite> entityList);
}
