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
import com.xie.server.a.util.Circle;
import com.xie.server.a.util.Geohash;
import com.xie.server.a.util.Point;
import com.xie.server.a.vo.CyclingBuSiteVo;
import java.util.ArrayList;
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
        //String extendGeoHash = getExtendGeoHash(srcLon, srcLat, params.getDistance());
        String extendGeoHash = test2(srcLon, srcLat, params.getDistance());
        List<CyclingBuSiteVo> list = cyclingBuSiteMapper.queryListByGeoHash(extendGeoHash);
        List<CyclingBuSiteVo> collect = convertCyclingBuSiteVos(srcLon, srcLat,list);
        return collect;
    }

    private String getExtendGeoHash(double srcLon,double srcLat,long distance){
        SiteDistanceMapping transform = SiteDistanceMapping.transform(distance);
        String srcCenter = geohash.encode(srcLon,srcLat).substring(0, transform.getLevel());

        String left = geohash.encode(srcLon-distance/2 * Geohash.lonUnit,srcLat).substring(0, transform.getLevel());
        String leftTop = geohash.encode( srcLon-distance/2 * Geohash.lonUnit,srcLat+distance/2 * Geohash.latUnit).substring(0, transform.getLevel());
        String leftBottom = geohash.encode( srcLon-distance/2 * Geohash.lonUnit,srcLat-distance/2 * Geohash.latUnit).substring(0, transform.getLevel());

        String right = geohash.encode(srcLon+distance/2 * Geohash.lonUnit,srcLat).substring(0, transform.getLevel());
        String rightTop = geohash.encode(srcLon+distance/2 * Geohash.lonUnit,srcLat+distance/2 * Geohash.latUnit).substring(0, transform.getLevel());
        String rightBottom = geohash.encode(srcLon+distance/2 * Geohash.lonUnit,srcLat-distance/2 * Geohash.latUnit).substring(0, transform.getLevel());

        String top = geohash.encode(srcLon,srcLat+distance/2 * Geohash.latUnit).substring(0, transform.getLevel());
        String bottom = geohash.encode(srcLon,srcLat-distance/2 * Geohash.latUnit).substring(0, transform.getLevel());


        String lefTopInner = geohash.encode(srcLon-distance/4 * Geohash.lonUnit,srcLat+distance/4 * Geohash.latUnit).substring(0, transform.getLevel());
        String rightTopInner = geohash.encode(srcLon+distance/4 * Geohash.lonUnit,srcLat+distance/4 * Geohash.latUnit).substring(0, transform.getLevel());
        String lefBottomInner = geohash.encode(srcLon-distance/4 * Geohash.lonUnit,srcLat-distance/4 * Geohash.latUnit).substring(0, transform.getLevel());
        String rightBottomInner = geohash.encode(srcLon+distance/4 * Geohash.lonUnit,srcLat-distance/4 * Geohash.latUnit).substring(0, transform.getLevel());

        Set<String>  geohashes = new HashSet<>();

        geohashes.add(srcCenter);

        geohashes.add(left);
        geohashes.add(leftTop);
        geohashes.add(leftBottom);

        geohashes.add(right);
        geohashes.add(rightTop);
        geohashes.add(rightBottom);

        geohashes.add(top);
        geohashes.add(bottom);

        geohashes.add(lefTopInner);
        geohashes.add(rightTopInner);
        geohashes.add(lefBottomInner);
        geohashes.add(rightBottomInner);


        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(String gh:geohashes){
            sb.append(" geo_hash like '").append(gh).append("%' or");
        }
        String s1 = sb.toString();
        String result = s1.substring(0,s1.length()-3)+")";
        return result;
    }

    private String test2(double srcLon,double srcLat,long distance){
        SiteDistanceMapping transform = SiteDistanceMapping.transform(distance);
        Circle person = new Circle(srcLon,srcLat,distance*Geohash.lonUnit);
        float unit = 360/6;
        Set<String>  geohashes = new HashSet<>();
        for(int i=0;i<6;i++){
            float angle = unit*i;
            Point point = person.computeCoordinates(angle);
            String srcCenter = geohash.encode(point.getX(),point.getY()).substring(0, transform.getLevel());
            geohashes.add(srcCenter);
        }
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
