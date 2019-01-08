package com.xie.gray.user;


import com.xie.common.vo.Page;
import com.xie.common.vo.ResponseVo;
import com.xie.gray.user.service.GrayUserService;
import com.xie.gray.user.vo.GrayUser;
import com.xie.gray.user.vo.UserParams;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 路由链路测试
 *
 * @author xie
 */
@RestController
@RequestMapping("/gray")
public class GrayUserController {


    @Autowired
    HttpServletRequest request;

    @Autowired(required = false)
    private GrayUserService grayUserService;

    /**
     * xhg-eureka-server 获取某应所有信息
     */
    @PostMapping(value = "/query/users/byPage")
    public ResponseVo<Page<GrayUser>> queryUsersByPage(@RequestBody UserParams params) {
        if(grayUserService == null){
            return ResponseVo.failure("当前服务没实现该接口");
        }
        Page<GrayUser> page = grayUserService.queryUsersByPage(params, params.getCurrentPage(), params.getPageSize());
        return ResponseVo.success(page);
    }


    protected int getPageSize() {
        String pageSize = request.getHeader("pageSize");
        int size = 10;
        if (!StringUtils.isEmpty(pageSize)) {
            size = Integer.valueOf(pageSize);
        }
        return size;
    }

    protected int getCurrentPage() {
        String currentPage = request.getHeader("currentPage");
        int current = 1;
        if (!StringUtils.isEmpty(currentPage)) {
            current = Integer.valueOf(currentPage);
        }
        return current;
    }


}
