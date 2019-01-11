package com.xie.server.a.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.server.a.entity.TCyclingBuSite;
import com.xie.server.a.vo.CyclingBuSiteVo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 站点 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2019-01-08
 */
public interface TCyclingBuSiteMapper extends BaseMapper<TCyclingBuSite> {

    List<CyclingBuSiteVo> queryListByLocation(@Param("longitude")String longitude, @Param("latitude")String latitude, @Param("distance") long distance);

    List<CyclingBuSiteVo> queryListByGeoHash(@Param("geoHash") String geoHash);
}
