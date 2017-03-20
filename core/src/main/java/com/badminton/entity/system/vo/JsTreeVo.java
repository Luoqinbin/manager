package com.badminton.entity.system.vo;

import java.io.Serializable;
import java.util.List;

/**
 * JsTreeVo
 */
public class JsTreeVo implements Serializable{
    private String id;
    private String text;
    private String icon;
    private JsTreeState state;
    private boolean checkbox;
    private List<JsTreeVo> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public JsTreeState getState() {
        return state;
    }

    public void setState(JsTreeState state) {
        this.state = state;
    }

    public List<JsTreeVo> getChildren() {
        return children;
    }

    public void setChildren(List<JsTreeVo> children) {
        this.children = children;
    }

    public boolean getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }
}
