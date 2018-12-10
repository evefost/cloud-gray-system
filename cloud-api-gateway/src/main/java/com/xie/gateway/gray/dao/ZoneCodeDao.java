package com.xie.gateway.gray.dao;

import com.xie.gateway.gray.bo.ZoneBo;
import com.xie.gray.strategy.GrayDao;
import org.springframework.stereotype.Component;

/**
 * Created by xieyang on 18/12/6.
 */
@Component
public class ZoneCodeDao implements GrayDao<ZoneBo> {


    @Override
    public boolean exist(ZoneBo params) {
        return false;
    }
}
