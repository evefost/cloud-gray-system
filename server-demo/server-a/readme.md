mvc 异常拦截
1.FilterChain
ApplicationFilterChain 
Filter 可能抛出异常
RequestContextFilter
2 DispatcherServlet 处理异常
processDispatchResult
3.HandlerInterceptor

Controller
BasicErrorController


org.apache.catalina.core.StandardWrapperValve
ApplicationFilterChain.doFilter
  private void exception(Request request, Response response,
                           Throwable exception) {
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, exception);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setError();
    }
    
    
StandardWrapperValve extend ValveBase
 标准的封装处理
 invoke
StandardContextValve extends ValveBase
  标准的上下文
 invoke
 
 
 
    
    ApplicationDispatcher
    
    forward
    invoke
    
    
        <!-- 查询指定范围内的回收机列表  -->
        <select id="querySiteListByLocation" resultType="com.xhg.device.pojo.CyclingBuSiteVo">
            SELECT
            s.geo_hash geoHash,
            s.id as id,s.device_id as deviceId,s.site_code as siteCode,s.site_address as siteAddress,s.link_phone as linkPhone,s.link_name as linkName,s.health_status as healthStatus,s.status as status,s.area_code as areaCode,s.city_code as cityCode,s.longitude as longitude,s.latitude as latitude,s.activate_status as activateStatus,s.update_status as updateStatus,s.box_num as boxNum,
            s.rent_type as rentType,s.restart_time as restartTime,s.app_version as appVersion,s.singlechip_version as singlechipVersion,s.vision_version as visionVersion,s.model_type as modelType,s.created_time as createdTime,s.temp_status as tempStatus
            ,(6378137.0*ACOS(SIN(#{latitude}/180*PI())*SIN(s.latitude/180*PI())+COS(#{latitude}/180*PI())*COS(s.latitude/180*PI())*COS((#{longitude}-s.longitude)/180*PI()))) as distance
            ,CONCAT(t2.area_name, REPLACE(s.site_address,'，', '')) as siteAddress
            FROM t_cycling_bu_site AS s
            INNER JOIN t_cycling_db_area t2 ON s.area_code = t2.area_code
            WHERE s.enable=1 and s.status=1 and s.model_type=2 and s.special_show_type =1 HAVING distance &lt;= #{distance}
            ORDER BY distance
        </select>