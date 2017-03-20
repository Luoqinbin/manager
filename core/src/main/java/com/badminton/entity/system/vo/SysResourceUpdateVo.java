package com.badminton.entity.system.vo;

import com.badminton.order.BaseOrder;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 资源
 */
public class SysResourceUpdateVo extends  BaseOrder  implements Serializable {
    @NotNull
    String idUpdate;
    @NotBlank
    private String resource_type_update;
    @NotBlank
    private String resource_name_update;
    @NotBlank
    private String resource_desc_update;
    @NotBlank
    private String resource_path_update;
    @NotBlank
    private String resource_parent_update;
    @NotNull
    private int enable_update;
    @NotBlank
    private String resource_icon_update;
    private int order_no_update;
    private int resource_level_update;
    private String btn_style_update;
    private Integer btn_index_update;

    public String getIdUpdate() {
        return idUpdate;
    }

    public void setIdUpdate(String idUpdate) {
        this.idUpdate = idUpdate;
    }

    public String getResource_type_update() {
        return resource_type_update;
    }

    public void setResource_type_update(String resource_type_update) {
        this.resource_type_update = resource_type_update;
    }

    public String getResource_name_update() {
        return resource_name_update;
    }

    public void setResource_name_update(String resource_name_update) {
        this.resource_name_update = resource_name_update;
    }

    public String getResource_desc_update() {
        return resource_desc_update;
    }

    public void setResource_desc_update(String resource_desc_update) {
        this.resource_desc_update = resource_desc_update;
    }

    public String getResource_path_update() {
        return resource_path_update;
    }

    public void setResource_path_update(String resource_path_update) {
        this.resource_path_update = resource_path_update;
    }

    public String getResource_parent_update() {
        return resource_parent_update;
    }

    public void setResource_parent_update(String resource_parent_update) {
        this.resource_parent_update = resource_parent_update;
    }

    public int getEnable_update() {
        return enable_update;
    }

    public void setEnable_update(int enable_update) {
        this.enable_update = enable_update;
    }

    public String getResource_icon_update() {
        return resource_icon_update;
    }

    public void setResource_icon_update(String resource_icon_update) {
        this.resource_icon_update = resource_icon_update;
    }

    public int getOrder_no_update() {
        return order_no_update;
    }

    public void setOrder_no_update(int order_no_update) {
        this.order_no_update = order_no_update;
    }

    public int getResource_level_update() {
        return resource_level_update;
    }

    public void setResource_level_update(int resource_level_update) {
        this.resource_level_update = resource_level_update;
    }

    public String getBtn_style_update() {
        return btn_style_update;
    }

    public void setBtn_style_update(String btn_style_update) {
        this.btn_style_update = btn_style_update;
    }

    public Integer getBtn_index_update() {
        return btn_index_update;
    }

    public void setBtn_index_update(Integer btn_index_update) {
        this.btn_index_update = btn_index_update;
    }
}
