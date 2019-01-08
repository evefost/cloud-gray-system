package com.xie.server.a.service.impl;

import static java.util.stream.Collectors.toList;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.server.a.bo.SiteParams;
import com.xie.server.a.constant.SiteDistanceMapping;
import com.xie.server.a.entity.TCyclingBuSite;
import com.xie.server.a.mapper.TCyclingBuSiteMapper;
import com.xie.server.a.service.ITCyclingBuSiteService;
import com.xie.server.a.util.Geohash;
import com.xie.server.a.vo.CyclingBuSiteVo;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 站点 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-01-08
 */
@Service
public class TCyclingBuSiteServiceImpl extends ServiceImpl<TCyclingBuSiteMapper, TCyclingBuSite> implements
    ITCyclingBuSiteService {
    Geohash geohash = new Geohash();


    @Override
    public List<CyclingBuSiteVo> queryList(SiteParams params) {

        double srcLon = Double.parseDouble(params.getLongitude());
        double srcLat = Double.parseDouble(params.getLatitude());
        String encode = geohash
            .encode(Double.parseDouble(params.getLatitude()), Double.parseDouble(params.getLongitude()));
        SiteDistanceMapping transform = SiteDistanceMapping.transform(params.getDistance());
        String gHash = encode.substring(0, transform.getLevel());
        QueryWrapper<TCyclingBuSite> wrappers = new QueryWrapper();
        wrappers.likeRight("geo_hash",gHash);
        List<TCyclingBuSite> list = list(wrappers);
        List<CyclingBuSiteVo> collect = convertCyclingBuSiteVos(srcLon, srcLat,list);
        return collect;
    }

    @Override
    public IPage<CyclingBuSiteVo> queryListByPage(SiteParams params) {
        double srcLon = Double.parseDouble(params.getLongitude());
        double srcLat = Double.parseDouble(params.getLatitude());
        IPage<TCyclingBuSite> page = new Page(params.getCurrent(),params.getSize());
        String encode = geohash .encode(Double.parseDouble(params.getLatitude()), Double.parseDouble(params.getLongitude()));
        SiteDistanceMapping transform = SiteDistanceMapping.transform(params.getDistance());
        String gHash = encode.substring(0, transform.getLevel());
        QueryWrapper<TCyclingBuSite> wrapper = new QueryWrapper();
        wrapper.likeRight("geo_hash",gHash);
        IPage<TCyclingBuSite> srcPage = page(page,wrapper);
        List<CyclingBuSiteVo> collect = convertCyclingBuSiteVos(srcLon, srcLat, srcPage.getRecords());
        IPage<CyclingBuSiteVo> result = new Page(params.getCurrent(),params.getSize());
        result.setRecords(collect);
        result.setTotal(srcPage.getTotal());
        return result;
    }

    @Override
    @Transactional
    public void addBatch(Collection<TCyclingBuSite> entityList) {
        saveBatch(entityList);
    }

    private List<CyclingBuSiteVo> convertCyclingBuSiteVos(double srcLon, double srcLat, List<TCyclingBuSite> srcList) {
        return srcList.stream().map((s) -> {
                CyclingBuSiteVo siteVo = new CyclingBuSiteVo();
                BeanUtils.copyProperties(s, siteVo);
                double desLon = Double.parseDouble(s.getLongitude());
                double desLat = Double.parseDouble(s.getLatitude());
                double distance = Geohash.getDistance(srcLon, srcLat, desLon, desLat);
                siteVo.setDistance(distance);
                return siteVo;
            }).sorted(Comparator.comparing(CyclingBuSiteVo::getDistance)).collect(toList());
    }
}
