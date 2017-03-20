package com.badminton.entity.system;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/16.
 */
public class Authority  implements Serializable{

    private String id;
    private String resource_name;
    private String resource_path;
    private String role_auth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public String getResource_path() {
        return resource_path;
    }

    public void setResource_path(String resource_path) {
        this.resource_path = resource_path;
    }

    public String getRole_auth() {
        return role_auth;
    }

    public void setRole_auth(String role_auth) {
        this.role_auth = role_auth;
    }
}
