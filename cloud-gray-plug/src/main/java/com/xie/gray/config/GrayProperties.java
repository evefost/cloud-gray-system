package com.xie.gray.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;


public class GrayProperties {


    private Set<String> skipPaths = new HashSet<>();


    @Value("${gray.route.skip-paths:/gray/test/**,/gray/query/users/byPage}")
    private String paths;

    @PostConstruct
    private void init() {

        String[] pks = paths.split(",");
        for (String pk : pks) {
            if (!StringUtils.isEmpty(pk)) {
                skipPaths.add(pk);
            }
        }
        skipPaths.add("/gray/test/**");
    }



    public Set<String> getSkipPaths() {
        return skipPaths;
    }

    public void setSkipPaths(Set<String> skipPaths) {
        this.skipPaths = skipPaths;
    }

}
