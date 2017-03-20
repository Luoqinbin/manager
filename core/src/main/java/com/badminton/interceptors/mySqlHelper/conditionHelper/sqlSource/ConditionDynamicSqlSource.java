package com.badminton.interceptors.mySqlHelper.conditionHelper.sqlSource;

import com.badminton.interceptors.mySqlHelper.conditionHelper.SqlUtils;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

import java.util.Map;

/**
 * Created by Ethan.Yuan on 2017/1/5.
 * 自定义sqlSource,通过实现getBoundSql方法修改sql语句
 */
public class ConditionDynamicSqlSource extends CustomerSqlSource implements SqlSource{
    private Configuration configuration;
    private SqlNode rootSqlNode;
    private SqlUtils sqlUtils;

    public ConditionDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode, Object conditionParams){
        this.configuration = configuration;
        this.rootSqlNode = rootSqlNode;
        this.conditionParams = conditionParams;
        sqlUtils = new SqlUtils();
    }

    @Override
    public void setConditionParams(Object conditionParams) {
        this.conditionParams = conditionParams;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(configuration, parameterObject);
        rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
        //将新增的SQL语句片段加到最后
        SqlSource sqlSource = sqlSourceParser.parse(sqlUtils.parseCondition(context.getSql(), conditionParams), parameterType, context.getBindings());
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }
        return boundSql;
    }

}
