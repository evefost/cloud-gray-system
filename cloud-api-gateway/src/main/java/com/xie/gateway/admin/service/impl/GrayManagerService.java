package com.xie.gateway.admin.service.impl;

import com.xie.gateway.admin.bo.GrayIpBo;
import com.xie.gateway.admin.bo.GrayTokenBo;
import com.xie.gateway.gray.dao.IpDao;
import com.xie.gateway.gray.dao.TokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xieyang on 18/12/7.
 */
@Service
public class GrayManagerService {


    @Autowired
    private IpDao ipDao;

    @Autowired
    private TokenDao tokenDao;


    public void addIp(GrayIpBo params) {
        ipDao.add(params.getServiceId().toUpperCase()+params.getIp());
    }

    public void deleteIp(GrayIpBo params) {
        ipDao.delete(params.getServiceId().toUpperCase()+params.getIp());
    }

    public void addToken(GrayTokenBo params) {
        tokenDao.add(params.getServiceId().toUpperCase()+params.getToken());
    }

    public void deleteToken(GrayTokenBo params) {
        tokenDao.delete(params.getServiceId().toUpperCase()+params.getToken());
    }


}
