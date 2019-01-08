package com.xie.server.a.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.server.a.entity.TbUser;
import com.xie.server.a.mapper.TbUserMapper;
import com.xie.server.a.service.ITbUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-01-07
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ITbUserService {

}
