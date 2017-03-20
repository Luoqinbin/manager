package com.badminton.interceptors.mySqlHelper.conditionHelper.sqlSource;

/**
 * Created by Ethan.Yuan on 2017/2/4.
 */
public abstract class CustomerSqlSource {

    protected Object conditionParams;

    public abstract void setConditionParams(Object conditionParams);
}
