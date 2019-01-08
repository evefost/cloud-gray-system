package com.xie.gateway.gray.dao;

import com.xie.gray.strategy.BaseDao;
import java.util.HashSet;
import org.springframework.stereotype.Component;

/**
 * Created by xieyang on 18/12/6.
 */
@Component
public class TokenDao extends BaseDao {

    public TokenDao() {
        container = new HashSet<>();
    }
}
