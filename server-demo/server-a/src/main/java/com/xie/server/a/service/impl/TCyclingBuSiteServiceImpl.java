package com.xie.server.a.service.impl;

import static java.util.stream.Collectors.toList;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TCyclingBuSiteMapper cyclingBuSiteMapper;

    @Override
    public List<CyclingBuSiteVo> queryList(SiteParams params) {

        double srcLon = Double.parseDouble(params.getLongitude());
        double srcLat = Double.parseDouble(params.getLatitude());
        String extendGeoHash = getExtendGeoHash(srcLon, srcLat, params.getDistance());
        List<CyclingBuSiteVo> list = cyclingBuSiteMapper.queryListByGeoHash(extendGeoHash);
        List<CyclingBuSiteVo> collect = convertCyclingBuSiteVos(srcLon, srcLat,list);
        return collect;
    }

    private String getExtendGeoHash(double srcLon,double srcLat,long distance){
        SiteDistanceMapping transform = SiteDistanceMapping.transform(distance);
        String srcCenter = geohash.encode(srcLon-distance/2 * Geohash.lonUnit,srcLat-distance/2 * Geohash.latUnit).substring(0, transform.getLevel());
        String srcCenter2 = geohash.encode(srcLon+distance/2 * Geohash.lonUnit,srcLat+distance/2 * Geohash.latUnit).substring(0, transform.getLevel());
        String srcCenter3 = geohash.encode(srcLon+distance/2 * Geohash.lonUnit,srcLat-distance/2 * Geohash.latUnit).substring(0, transform.getLevel());
        String srcCenter4 = geohash.encode(srcLon-distance/2 * Geohash.lonUnit,srcLat+distance/2 * Geohash.latUnit).substring(0, transform.getLevel());
        String srcLeftTop = geohash.encode( srcLon+distance/2 * Geohash.lonUnit,srcLat).substring(0, transform.getLevel());
        String srcRightTop = geohash.encode( srcLon-distance/2 * Geohash.lonUnit,srcLat).substring(0, transform.getLevel());
        String srcLeftBottom = geohash.encode(srcLon,srcLat+distance/2 * Geohash.latUnit ).substring(0, transform.getLevel());
        String srcRightBottom = geohash.encode( srcLon,srcLat-distance/2 * Geohash.latUnit).substring(0, transform.getLevel());
        Set<String>  geohashes = new HashSet<>();
        geohashes.add(srcCenter);
        geohashes.add(srcCenter2);
        geohashes.add(srcCenter3);
        geohashes.add(srcCenter4);

        geohashes.add(srcLeftTop);
        geohashes.add(srcRightTop);
        geohashes.add(srcLeftBottom);
        geohashes.add(srcRightBottom);
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(String gh:geohashes){
            sb.append(" geo_hash like '").append(gh).append("%' or");
        }
        String s1 = sb.toString();
        String result = s1.substring(0,s1.length()-3)+")";
        return result;
    }

    private String getExtendGeoHash2(double srcLon,double srcLat,long distance){
        String srcCenter = geohash.encode( srcLon+distance/2 * Geohash.lonUnit,srcLat-distance/2 * Geohash.latUnit);

        SiteDistanceMapping transform = SiteDistanceMapping.transform(distance);
        String  center = srcCenter.substring(0, transform.getLevel());

        String s = center+"%  ";
        return s;
    }

    @Override
    public List<CyclingBuSiteVo> queryListByLocation(SiteParams params) {
        double srcLon = Double.parseDouble(params.getLongitude());
        double srcLat = Double.parseDouble(params.getLatitude());
        List<CyclingBuSiteVo> list = cyclingBuSiteMapper
            .queryListByLocation(params.getLongitude(), params.getLatitude(), params.getDistance());
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
//        List<CyclingBuSiteVo> collect = convertCyclingBuSiteVos(srcLon, srcLat, srcPage.getRecords());
        IPage<CyclingBuSiteVo> result = new Page(params.getCurrent(),params.getSize());
//        result.setRecords(collect);
        result.setTotal(srcPage.getTotal());
        return result;
    }





    @Override
    @Transactional
    public void addBatch(Collection<TCyclingBuSite> entityList) {
        saveBatch(entityList);
    }

    private List<CyclingBuSiteVo> convertCyclingBuSiteVos(double srcLon, double srcLat, List<CyclingBuSiteVo> srcList) {
        return srcList.stream().map((s) -> {
                double desLon = Double.parseDouble(s.getLongitude());
                double desLat = Double.parseDouble(s.getLatitude());
                double distance = Geohash.getDistance(srcLon, srcLat, desLon, desLat);
                s.setDistance(distance);
                return s;
            }).sorted(Comparator.comparing(CyclingBuSiteVo::getDistance)).collect(toList());
    }
}
