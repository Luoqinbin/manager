package com.badminton.interceptors.mySqlHelper.pagehelper.dialect;

import com.badminton.interceptors.mySqlHelper.pagehelper.Constant;
import com.badminton.interceptors.mySqlHelper.pagehelper.Page;
import com.badminton.interceptors.mySqlHelper.pagehelper.util.MetaObjectUtil;
import com.badminton.interceptors.mySqlHelper.pagehelper.util.SqlUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuzh
 */
public class InformixDialect extends AbstractDialect {
    public InformixDialect(SqlUtil sqlUtil) {
        super(sqlUtil);
    }

    @Override
    public Object processPageParameter(MappedStatement ms, Map<String, Object> paramMap, Page page, BoundSql boundSql, CacheKey pageKey) {
        paramMap.put(Constant.PAGEPARAMETER_FIRST, page.getStartRow());
        paramMap.put(Constant.PAGEPARAMETER_SECOND, page.getPageSize());
        //处理pageKey
        pageKey.update(page.getStartRow());
        pageKey.update(page.getPageSize());
        //处理参数配置
        if (boundSql.getParameterMappings() != null) {
            List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>();
            newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), Constant.PAGEPARAMETER_FIRST, Integer.class).build());
            newParameterMappings.add(new ParameterMapping.Builder(ms.getConfiguration(), Constant.PAGEPARAMETER_SECOND, Integer.class).build());
            if (boundSql != null && boundSql.getParameterMappings() != null) {
                newParameterMappings.addAll(boundSql.getParameterMappings());
            }
            MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
            metaObject.setValue("parameterMappings", newParameterMappings);
        }
        return paramMap;
    }

    @Override
    public String getPageSql(String sql, Page page, RowBounds rowBounds, CacheKey pageKey) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 40);
        sqlBuilder.append("select skip ? first ? * from ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) temp_t");
        return sqlBuilder.toString();
    }

}
