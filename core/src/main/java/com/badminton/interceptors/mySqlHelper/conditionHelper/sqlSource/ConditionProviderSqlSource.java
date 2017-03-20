package com.badminton.interceptors.mySqlHelper.conditionHelper.sqlSource;

import com.badminton.interceptors.mySqlHelper.conditionHelper.SqlUtils;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

/**
 * Created by Ethan.Yuan on 2017/1/9.
 * 自定义的provider类型的sqlsource
 */
public class ConditionProviderSqlSource extends CustomerSqlSource implements SqlSource{
    private Configuration configuration;
    private ProviderSqlSource providerSqlSource;
    private SqlUtils sqlUtils;

    public ConditionProviderSqlSource(Configuration configuration, ProviderSqlSource providerSqlSource, Object conditionParams) {
        this.configuration = configuration;
        this.providerSqlSource = providerSqlSource;
        this.conditionParams = conditionParams;
        this.sqlUtils = new SqlUtils();
    }

    @Override
    public void setConditionParams(Object conditionParams) {
        this.conditionParams = conditionParams;
    }


    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = null;
        boundSql = providerSqlSource.getBoundSql(parameterObject);
        return new BoundSql(
                configuration,
                sqlUtils.parseCondition(boundSql.getSql(), conditionParams),
                boundSql.getParameterMappings(),
                parameterObject);
    }
}
