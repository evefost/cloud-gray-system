package com.xie.gateway.gray.dao;

import com.xie.gray.strategy.BaseDao;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * Created by xieyang on 18/12/6.
 */
@Component
public class IpDao extends BaseDao {

    public IpDao() {
        container = new HashSet<>();
    }
}