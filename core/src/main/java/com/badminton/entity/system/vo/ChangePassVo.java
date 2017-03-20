package com.badminton.entity.system.vo;

import com.badminton.order.BaseOrder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2016/8/17.
 */
@Data
public class ChangePassVo extends BaseOrder {
    //旧密码
    @NotBlank
    private String oldPassword;
    //新密码
    @NotNull
    @NotBlank
    @Length(min=6, max=20)
    private String newPassword;
    //确认新密码
    @NotNull
    @NotBlank
    @Length(min=6, max=20)
    private String confirmNewPassword;
}
