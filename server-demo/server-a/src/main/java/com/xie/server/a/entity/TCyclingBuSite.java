package com.xie.server.a.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
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
public class TCyclingBuSite implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
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

    protected Serializable pkVal() {
        return this.id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(Integer healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getActivateStatus() {
        return activateStatus;
    }

    public void setActivateStatus(Integer activateStatus) {
        this.activateStatus = activateStatus;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Integer getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(Integer updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(Integer updateStatus) {
        this.updateStatus = updateStatus;
    }

    public LocalDateTime getUpdateStatusTime() {
        return updateStatusTime;
    }

    public void setUpdateStatusTime(LocalDateTime updateStatusTime) {
        this.updateStatusTime = updateStatusTime;
    }

    public LocalDateTime getRestartTime() {
        return restartTime;
    }

    public void setRestartTime(LocalDateTime restartTime) {
        this.restartTime = restartTime;
    }

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }

    public Integer getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSinglechipVersion() {
        return singlechipVersion;
    }

    public void setSinglechipVersion(String singlechipVersion) {
        this.singlechipVersion = singlechipVersion;
    }

    public String getVisionVersion() {
        return visionVersion;
    }

    public void setVisionVersion(String visionVersion) {
        this.visionVersion = visionVersion;
    }

    public Boolean getModelType() {
        return modelType;
    }

    public void setModelType(Boolean modelType) {
        this.modelType = modelType;
    }

    public String getNameplateCode() {
        return nameplateCode;
    }

    public void setNameplateCode(String nameplateCode) {
        this.nameplateCode = nameplateCode;
    }

    public String getRouterCode() {
        return routerCode;
    }

    public void setRouterCode(String routerCode) {
        this.routerCode = routerCode;
    }

    public String getCameraCode() {
        return cameraCode;
    }

    public void setCameraCode(String cameraCode) {
        this.cameraCode = cameraCode;
    }

    public String getIpcCode() {
        return ipcCode;
    }

    public void setIpcCode(String ipcCode) {
        this.ipcCode = ipcCode;
    }

    public Integer getAddressType() {
        return addressType;
    }

    public void setAddressType(Integer addressType) {
        this.addressType = addressType;
    }

    public String getOtherAddressType() {
        return otherAddressType;
    }

    public void setOtherAddressType(String otherAddressType) {
        this.otherAddressType = otherAddressType;
    }

    public Boolean getTempStatus() {
        return tempStatus;
    }

    public void setTempStatus(Boolean tempStatus) {
        this.tempStatus = tempStatus;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public LocalDateTime getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(LocalDateTime onlineTime) {
        this.onlineTime = onlineTime;
    }

    public LocalDateTime getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(LocalDateTime offlineTime) {
        this.offlineTime = offlineTime;
    }

    public LocalDateTime getFactoryModelTime() {
        return factoryModelTime;
    }

    public void setFactoryModelTime(LocalDateTime factoryModelTime) {
        this.factoryModelTime = factoryModelTime;
    }

    public LocalDateTime getProductModelTime() {
        return productModelTime;
    }

    public void setProductModelTime(LocalDateTime productModelTime) {
        this.productModelTime = productModelTime;
    }

    public Integer getTypeNum() {
        return typeNum;
    }

    public void setTypeNum(Integer typeNum) {
        this.typeNum = typeNum;
    }

    public Boolean getTrace() {
        return trace;
    }

    public void setTrace(Boolean trace) {
        this.trace = trace;
    }

    public LocalDateTime getLastProductTime() {
        return lastProductTime;
    }

    public void setLastProductTime(LocalDateTime lastProductTime) {
        this.lastProductTime = lastProductTime;
    }

    public String getSiteAddressStreet() {
        return siteAddressStreet;
    }

    public void setSiteAddressStreet(String siteAddressStreet) {
        this.siteAddressStreet = siteAddressStreet;
    }

    public String getSiteAddressApartment() {
        return siteAddressApartment;
    }

    public void setSiteAddressApartment(String siteAddressApartment) {
        this.siteAddressApartment = siteAddressApartment;
    }

    public String getSiteAddressDetail() {
        return siteAddressDetail;
    }

    public void setSiteAddressDetail(String siteAddressDetail) {
        this.siteAddressDetail = siteAddressDetail;
    }

    public Integer getCheckSign() {
        return checkSign;
    }

    public void setCheckSign(Integer checkSign) {
        this.checkSign = checkSign;
    }

    public String getProtoVersion() {
        return protoVersion;
    }

    public void setProtoVersion(String protoVersion) {
        this.protoVersion = protoVersion;
    }

    public Integer getSpecialShowType() {
        return specialShowType;
    }

    public void setSpecialShowType(Integer specialShowType) {
        this.specialShowType = specialShowType;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }
}
