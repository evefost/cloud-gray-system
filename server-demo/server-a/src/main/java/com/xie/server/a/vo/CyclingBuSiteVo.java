package com.xie.server.a.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 站点
 * </p>
 *
 * @author jobob
 * @since 2019-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CyclingBuSiteVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 站点编码
     */
    private String siteCode;

    /**
     * 站点地址
     */
    private String siteAddress;

    /**
     * 维护人电话
     */
    private String linkPhone;

    /**
     * 维护人
     */
    private String linkName;

    /**
     * 站点状态：1上线 2下线
     */
    private Boolean status;

    /**
     * 状态：1正常 2故障 3断网 4维护中
     */
    private Integer healthStatus;

    /**
     * 区编码
     */
    private String areaCode;

    /**
     * 坐标经度
     */
    private String longitude;

    /**
     * 坐标纬度
     */
    private String latitude;

    /**
     * 激活状态：0未激活 1激活 
     */
    private Integer activateStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 创建人
     */
    private Integer createdUserId;

    /**
     * 更新人
     */
    private Integer updatedUserId;

    /**
     * 是否可用1：可用0：不可用
     */
    private Boolean enable;

    /**
     * 终端更新状态，0为默认非更新状态，1为更新中状态，2为更新成功，3为更新失败，4为未知状态
     */
    private Integer updateStatus;

    /**
     * 更新状态更新时间
     */
    private LocalDateTime updateStatusTime;

    /**
     * 最近重启时间
     */
    private LocalDateTime restartTime;

    /**
     * 租赁方式：1-自营 2-加盟商租赁
     */
    private Integer rentType;

    /**
     * 箱子数量
     */
    private Integer boxNum;

    /**
     * 上位机版本号
     */
    private String appVersion;

    /**
     * 下位机版本号
     */
    private String singlechipVersion;

    /**
     * 视觉模块版本号
     */
    private String visionVersion;

    /**
     * 站点模式状态：1工厂模式 2投产模式
     */
    private Boolean modelType;

    /**
     * 机器铭牌号
     */
    private String nameplateCode;

    /**
     * 路由器编号
     */
    private String routerCode;

    /**
     * 安防摄像头编号
     */
    private String cameraCode;

    /**
     * 工控机编号
     */
    private String ipcCode;

    /**
     * 地址类型：0居民小区、1商务办公区、2商业中心、3市政建筑、4学校、5医院、6公园、7交通中心、8其它
     */
    private Integer addressType;

    /**
     * 其他地址类型
     */
    private String otherAddressType;

    /**
     * 温度状态：0正常 1高温预警
     */
    private Boolean tempStatus;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 上线时间
     */
    private LocalDateTime onlineTime;

    /**
     * 下线时间
     */
    private LocalDateTime offlineTime;

    /**
     * 工厂模式激活时间
     */
    private LocalDateTime factoryModelTime;

    /**
     * 投产模式激活时间
     */
    private LocalDateTime productModelTime;

    /**
     * 品类数量
     */
    private Integer typeNum;

    /**
     * 是否有追溯功能:0没有 1有
     */
    private Boolean trace;

    /**
     * 上次投产激活时间
     */
    private LocalDateTime lastProductTime;

    /**
     * 街道
     */
    private String siteAddressStreet;

    /**
     * 小区
     */
    private String siteAddressApartment;

    /**
     * 地址详细描述
     */
    private String siteAddressDetail;

    /**
     * 是否需要校验签名 0不需要 1需要
     */
    private Integer checkSign;

    /**
     * MQTT协议版本号
     */
    private String protoVersion;

    /**
     * 特殊类型显示 1、白名单 2、军事基地
     */
    private Integer specialShowType;

    /**
     * geohash
     */
    private String geoHash;

    private double distance;

}
