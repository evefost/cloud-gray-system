package test.com.xie.server.a.service.impl;


import static java.util.stream.Collectors.toList;

import base.TestBaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.server.a.bo.SiteParams;
import com.xie.server.a.entity.TCyclingBuSite;
import com.xie.server.a.service.ITCyclingBuSiteService;
import com.xie.server.a.util.Geohash;
import com.xie.server.a.vo.CyclingBuSiteVo;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.bytebuddy.implementation.bytecode.Throw;
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

    /**
     * Method: queryList(SiteParams params)
     */
    @Test
    public void testQueryList() throws Exception {
        SiteParams params = new SiteParams();
        params.setDistance(6);
        params.setLongitude("113.786162");
        params.setLatitude("23.056432");
        List<CyclingBuSiteVo> tCyclingBuSites = cyclingBuSiteService.queryList(params);
        Assert.assertNotNull(tCyclingBuSites);
    }


}


