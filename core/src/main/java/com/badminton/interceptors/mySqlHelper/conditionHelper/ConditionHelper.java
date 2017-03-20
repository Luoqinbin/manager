package com.badminton.interceptors.mySqlHelper.conditionHelper;

import com.badminton.entity.BaseEntity;
import com.badminton.utils.ReflectUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by Ethan.Yuan on 2016/12/19.
 * 设置自定义查询条件
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class}),
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class,Object.class}
        )
})
//@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class, Integer.class})})
@Component
public class ConditionHelper implements Interceptor{
        @Override
        public Object intercept(Invocation invocation) throws Throwable {
            Object[] args = invocation.getArgs();
            Object sqlArg = args[1];
            //当参数为baseEntity且带有条件参数的时候
            if(sqlArg instanceof BaseEntity && null != ((BaseEntity) sqlArg).getCondition()){
                MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
                SqlSource sqlSource = SqlUtils.getsqlSource(ms,ms.getSqlSource(),sqlArg, ((BaseEntity) sqlArg).getCondition());
                ReflectUtil.setFieldValue(ms, "sqlSource", sqlSource);
                args[0] = ms;
            }
            return invocation.proceed();
        }

        @Override
        public Object plugin(Object target) {
            if(target instanceof Executor){
                    return Plugin.wrap(target, this);
            }else{
                    return target;
            }
        }

        @Override
        public void setProperties(Properties properties) {
            // TODO Auto-generated method stub
        }
}
