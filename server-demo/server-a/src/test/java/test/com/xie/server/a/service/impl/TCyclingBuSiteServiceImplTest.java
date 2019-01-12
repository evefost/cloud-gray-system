package test.com.xie.server.a.service.impl;


import static java.util.stream.Collectors.toList;

import base.TestBaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.server.a.bo.SiteParams;
import com.xie.server.a.entity.TCyclingBuSite;
import com.xie.server.a.service.ITCyclingBuSiteService;
import com.xie.server.a.util.Circle;
import com.xie.server.a.util.Geohash;
import com.xie.server.a.util.Point;
import com.xie.server.a.vo.CyclingBuSiteVo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * TCyclingBuSiteServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jan 8, 2019</pre>
 */
public class TCyclingBuSiteServiceImplTest extends TestBaseService {

    @Autowired
    private ITCyclingBuSiteService cyclingBuSiteService;




    @Test
    public void addGeohash() {
        IPage<TCyclingBuSite> page = new Page(0, 50);
        IPage<TCyclingBuSite> page1 = cyclingBuSiteService.page(page);
        long pages = page1.getPages();
        Random rd = new Random();
        for (long p = 0; p < pages+1; p++) {
            page.setCurrent(p);
            IPage<TCyclingBuSite> pageLIst = cyclingBuSiteService.page(page);
            List<TCyclingBuSite> records = page1.getRecords();
            List<TCyclingBuSite> collect = records.stream().filter((r) -> {
                if (StringUtils.isEmpty(r.getLatitude()) || StringUtils.isEmpty(r.getLongitude())) {
                    return false;
                }
                try {
                    Double.parseDouble(r.getLatitude());
                    Double.parseDouble(r.getLongitude());
                } catch (Throwable throwable) {
                    logger.error("", throwable);
                    return false;
                }
                return true;
            }).map((s) -> {
                int lo = rd.nextInt(999999);
                int la = rd.nextInt(999999);
                int loIndex = s.getLongitude().indexOf(".");
                int laIndex = s.getLatitude().indexOf(".");
                String lon = s.getLongitude().substring(0, loIndex+1) + String.valueOf(lo);
                String lat = s.getLatitude().substring(0, laIndex+1) + String.valueOf(la);

                Geohash geohash = new Geohash();
                String encode = geohash
                    .encode(Double.parseDouble(lat), Double.parseDouble(lon));
                s.setGeoHash(encode);
                s.setDeviceId(UUID.randomUUID().toString());
                s.setSiteCode(UUID.randomUUID().toString());
                s.setId(null);
                return s;
            }).collect(toList());
            if (collect.isEmpty()) {
                continue;
            }
            cyclingBuSiteService.saveBatch(collect);
        }
        System.out.println("修改条数:" + page1.getRecords());
    }

    Random rd = new Random();
    /**
     * Method: queryList(SiteParams params)
     */
    @Test
    public void testQueryList() throws Exception {

        int total = 100;
        int containCount = 0;
        for(int i=0;i<total;i++){
            int lo = rd.nextInt(9999);
            int la = rd.nextInt(9999);
            String lon = "114.129014";
            String lat = "22.809434";
            lon = lon.substring(0, lon.length()-4) + String.valueOf(lo);
            lat = lat.substring(0, lat.length()-4) + String.valueOf(la);
            SiteParams params = new SiteParams();
            params.setDistance(5000);
            params.setLongitude(lon);
            params.setLatitude(lat);
            List<CyclingBuSiteVo> siteVos1 = cyclingBuSiteService.queryList(params);
            List<CyclingBuSiteVo> siteVos2 = cyclingBuSiteService.queryListByLocation(params);
            if(contain(siteVos1,siteVos2)){
                containCount++;
            }
        }

        System.out.println("total:"+total+"/"+containCount);
    }

    @Test
    public void testPont(){
        int lo = rd.nextInt(9999);
        int la = rd.nextInt(9999);
        String lon = "114.129014";
        String lat = "22.809434";
        lon = lon.substring(0, lon.length()-4) + String.valueOf(lo);
        lat = lat.substring(0, lat.length()-4) + String.valueOf(la);
        double x = Double.parseDouble(lon);
        double y = Double.parseDouble(lat);
        Circle person = new Circle(x,y,5000*Geohash.lonUnit);
        float unit = 360/6;
        List<Point> points = new ArrayList<>();
        for(int i=0;i<6;i++){
            float angle = unit*i;
            Point point = person.computeCoordinates(angle);
            points.add(point);
        }

        System.out.println(points);

    }


    boolean contain( List<CyclingBuSiteVo> siteVos1, List<CyclingBuSiteVo> siteVos2){
        Map<String,CyclingBuSiteVo> geohashMap = new HashMap<>();
        for(CyclingBuSiteVo bs:siteVos1){
            geohashMap.put(bs.getGeoHash(),bs);
        }
        for(CyclingBuSiteVo bs:siteVos2){
            CyclingBuSiteVo siteVo = geohashMap.get(bs.getGeoHash());
            if(siteVo == null){
                return false;
            }
        }
        return true;
    }

    @Test
    public void testQueryListByLocation() throws Exception {
        SiteParams params = new SiteParams();
        params.setDistance(50000);
        params.setLongitude("114.129014");
        params.setLatitude("22.809434");
        List<CyclingBuSiteVo> tCyclingBuSites = cyclingBuSiteService.queryListByLocation(params);
        Assert.assertNotNull(tCyclingBuSites);
    }


}


