package com.xie.server.a.bo;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SiteParams {

    private long size = 10;
    /**
     * 当前页
     */
    private long current = 1;


    private int distance;

    /**
     * 坐标经度
     */
    @NotBlank
    private String longitude;

    /**
     * 坐标纬度
     */
    private String latitude;


}
