package com.xie.server.a.web;


import com.xie.server.a.entity.TbUser;
import com.xie.server.a.service.ITbUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController
@Api(value = "用户操作", description = "用户操作")
public class UserController {


    @Autowired
    private ITbUserService userService;

    @ApiOperation(value = "添加用户")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean add(TbUser user) {
        return userService.save(user);

    }



}
