package com.badminton.interceptors.mySqlHelper.conditionHelper;

import com.badminton.interceptors.mySqlHelper.conditionHelper.query.*;
import com.badminton.interceptors.mySqlHelper.conditionHelper.sqlSource.ConditionDynamicSqlSource;
import com.badminton.interceptors.mySqlHelper.conditionHelper.sqlSource.ConditionProviderSqlSource;
import com.badminton.interceptors.mySqlHelper.conditionHelper.sqlSource.ConditionStaticSqlSource;
import com.badminton.interceptors.mySqlHelper.conditionHelper.sqlSource.CustomerSqlSource;
import com.badminton.interceptors.mySqlHelper.pagehelper.util.StringUtil;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan.Yuan on 2017/1/6.
 */
public class SqlUtils {
    //sql语句转换器
    private WhereCondition whereCondition = new WhereCondition();
    private OrderCondition orderCondition = new OrderCondition();
    /**
     * 获取新的sqlSource
     *
     * @param ms
     * @param sqlSource
     * @param parameterObject
     * @return
     */
    public static SqlSource getsqlSource(MappedStatement ms, SqlSource sqlSource, Object parameterObject, Object conditionParams) {
        if(sqlSource instanceof CustomerSqlSource){
            ((CustomerSqlSource) sqlSource).setConditionParams(conditionParams);
        }
        if (sqlSource instanceof DynamicSqlSource) {//动态sql
            MetaObject msObject = SystemMetaObject.forObject(ms);
            SqlNode sqlNode = (SqlNode) msObject.getValue("sqlSource.rootSqlNode");
            MixedSqlNode mixedSqlNode;
            if (sqlNode instanceof MixedSqlNode) {
                mixedSqlNode = (MixedSqlNode) sqlNode;
            } else {
                List<SqlNode> contents = new ArrayList<SqlNode>(1);
                contents.add(sqlNode);
                mixedSqlNode = new MixedSqlNode(contents);
            }
            return new ConditionDynamicSqlSource(ms.getConfiguration(), mixedSqlNode, conditionParams);
        } else if (sqlSource instanceof ProviderSqlSource) {//注解式sql
            return new ConditionProviderSqlSource(ms.getConfiguration(), (ProviderSqlSource) sqlSource, conditionParams);
        } else{//staticSqlSource
            MetaObject msObject = SystemMetaObject.forObject(ms);
            String sqlString = "";
            try{
                if(sqlSource instanceof RawSqlSource){
                    sqlString = String.valueOf(msObject.getValue("sqlSource.sqlSource.sql"));
                }else{
                    sqlString = String.valueOf(msObject.getValue("sqlSource.sql"));
                }
            }catch (Exception e){
                return sqlSource;
            }
            return new ConditionStaticSqlSource(sqlString,conditionParams, ms.getConfiguration() , sqlSource.getBoundSql(parameterObject).getParameterMappings());
        }
    }

    /**
     * 转换带前缀的where条件
     * @param sqlString, conditionParams
     * if parameter is not instanceof a Condition, do nothing
     * @return
     */
    @SuppressWarnings("unchecked")
    public String parseCondition(String sqlString, Object conditionParams){
        StringBuilder sqlBuilder = new StringBuilder(sqlString);
        sqlString = sqlString.toLowerCase();
        //如果为定义的查询条件类，转换order by 和 自定义的where 条件
        if(conditionParams instanceof Condition && null != conditionParams){
            Condition parameter = (Condition) conditionParams;
            Map<String, Object> params = (Map<String,Object>)Condition.format(parameter);
            //转换where条件
            String conditionString = whereCondition.toSQL((List<WhereItem>)params.get(Condition.CONDITION_FILTER));
            //转换order by条件
            String orderString = orderCondition.toSQL((List<OrderItem>)params.get(Condition.ORDER_FILTER));
            //在boundSql中添加查询条件和排序条件
            if(StringUtil.isNotEmpty(conditionString)){
                if(sqlString.indexOf("where") < 0){
                    sqlBuilder.append(" where 1=1 ");
                }
                sqlBuilder.append(conditionString);
            }
            if(StringUtil.isNotEmpty(orderString)){
                sqlBuilder.append(" ").append(orderString);
            }
        }

        return sqlBuilder.toString();
    }
}
