package com.badminton.interceptors.mySqlHelper.conditionHelper.sqlSource;

import com.badminton.interceptors.mySqlHelper.conditionHelper.SqlUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.List;

/**
 * Created by Ethan.Yuan on 2017/1/17.
 */
public class ConditionStaticSqlSource extends CustomerSqlSource implements SqlSource {
    private String sql;
    private Configuration configuration;
    private List<ParameterMapping> parameterMappings;
    private SqlUtils sqlUtils;

    public ConditionStaticSqlSource(String sql, Object conditionParams, Configuration configuration, List<ParameterMapping> parameterMappings){
        this.sql = sql;
        this.conditionParams = conditionParams;
        this.configuration = configuration;
        this.parameterMappings = parameterMappings;
        sqlUtils = new SqlUtils();
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(configuration, sqlUtils.parseCondition(sql, conditionParams), parameterMappings, parameterObject);
    }

    @Override
    public void setConditionParams(Object conditionParams) {
        this.conditionParams = conditionParams;
    }
}
