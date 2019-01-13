package com.xie.server.a.service.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.stream.Collectors.toList;

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
    private  final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TCyclingBuSiteMapper cyclingBuSiteMapper;

    @Override
    public List<CyclingBuSiteVo> queryList(SiteParams params) {
        double srcLon = Double.parseDouble(params.getLongitude());
        double srcLat = Double.parseDouble(params.getLatitude());
        String extendGeoHash = getGeoHashes(srcLon, srcLat, params.getDistance());
        List<CyclingBuSiteVo> list = cyclingBuSiteMapper.queryListByGeoHash(extendGeoHash);
        List<CyclingBuSiteVo> collect = convertCyclingBuSiteVos(srcLon, srcLat,params.getDistance(),list);
        logger.debug("result size:{}",collect.size());
        return collect;
    }



    public String  getGeoHashes(double longitude,double latitude, int radius) {

        SiteDistanceMapping transform = SiteDistanceMapping.transform(radius);
        String srcCenter = geohash.encode(longitude,latitude).substring(0, transform.getLevel());
        Set<String> geohashes = getCircleGeohashes(longitude, latitude, (int) (radius*transform.getScale()));

        geohashes.add(srcCenter);
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(String gh:geohashes){
            sb.append(" geo_hash like '").append(gh).append("%' or");
        }
        String s1 = sb.toString();
        String result = s1.substring(0,s1.length()-3)+")";
        return result;

    }

    private Set<String> getCircleGeohashes(double longitude,double latitude, int radius){
        SiteDistanceMapping transform = SiteDistanceMapping.transform(radius);
        double r = 6371000.79;
        int numpoints = 360;
        double phase = 2 * Math.PI / numpoints;
        int points = 12;
        int unit = 360/points;
        Set<String>  geohashes = new HashSet<>();
        for (int i = 0; i < points; i++) {
            //计算坐标点
            double dx = (radius * Math.cos(i*unit * phase));
            //乘以1.6 椭圆比例
            double dy = (radius * Math.sin(i*unit * phase));

            double dlng = dx / (r * Math.cos(latitude * Math.PI / 180) * Math.PI / 180);
            double dlat = dy / (r * Math.PI / 180);
            double newlng = longitude + dlng;
            double newLat = latitude+dlat;
            String geoHash = geohash.encode(newlng,newLat).substring(0, transform.getLevel());
            geohashes.add(geoHash);
        }

        return geohashes;
    }


    @Override
    public List<CyclingBuSiteVo> queryListByLocation(SiteParams params) {
        double srcLon = Double.parseDouble(params.getLongitude());
        double srcLat = Double.parseDouble(params.getLatitude());
        List<CyclingBuSiteVo> list = cyclingBuSiteMapper
            .queryListByLocation(params.getLongitude(), params.getLatitude(), params.getDistance());
        List<CyclingBuSiteVo> collect = convertCyclingBuSiteVos(srcLon, srcLat,params.getDistance(),list);
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

    private List<CyclingBuSiteVo> convertCyclingBuSiteVos(double srcLon, double srcLat,long radius, List<CyclingBuSiteVo> srcList) {
        return srcList.stream().map((s) -> {
                double desLon = Double.parseDouble(s.getLongitude());
                double desLat = Double.parseDouble(s.getLatitude());
                double distance = Geohash.getDistance(srcLon, srcLat, desLon, desLat);
                s.setDistance(distance);
                return s;
            }).filter((s)->{
            return s.getDistance()<=radius;
        }).sorted(Comparator.comparing(CyclingBuSiteVo::getDistance)).collect(toList());
    }
}
