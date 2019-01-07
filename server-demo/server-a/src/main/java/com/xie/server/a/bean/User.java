package com.xie.server.a.bean;

import javax.validation.constraints.NotBlank;

public class User {

    @NotBlank
    private String name;

    @NotBlank
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
