package com.badminton.entity.system.vo;

import java.io.Serializable;

/**
 * Created by Luoqb on 2017/2/18.
 */
public class JsTreeState implements Serializable{

    private boolean opened;
    private boolean disabled;
    private boolean selected;

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
