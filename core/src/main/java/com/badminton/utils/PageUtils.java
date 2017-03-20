package com.badminton.utils;

import com.alibaba.fastjson.JSONArray;
import com.badminton.interceptors.mySqlHelper.pagehelper.PageHelper;
import com.badminton.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zoudong on 2016/8/3.
 */
public class PageUtils<T extends BaseEntity> {
    /**
     * 单表、多表datatable组件自动排序分页细节处理，与具体排序字段名称和数组下标解耦。
     * @param tableAlias
     * @param defaultSortCloum
     * @param aoData
     * @return
     **/
    public static String pageSort(String tableAlias,String defaultSortCloum, String aoData) {
        //支持复合多表排序分页
        if(tableAlias==null||tableAlias.equals(StringUtils.EMPTY)){
            tableAlias= StringUtils.SPACE;
        }
        if(tableAlias!=null&&!tableAlias.equals(StringUtils.SPACE)&&tableAlias.indexOf(".")==-1){
            tableAlias=tableAlias+".";
        }
        String orderByClause = tableAlias+defaultSortCloum + StringUtils.SPACE;
        try {
            JSONArray jsonArray = JSONArray.parseArray(aoData);
            System.out.println("收到DATATABLE原始数据:" + jsonArray.toJSONString());
            int iDisplayStart = jsonArray.getJSONObject(3).getIntValue("value") / jsonArray.getJSONObject(4).getIntValue("value") + 1;
            int iDisplayLength = jsonArray.getJSONObject(4).getIntValue("value");
            PageHelper.startPage(iDisplayStart, iDisplayLength);
            //排序获取单列排序的策略desc、asc
            //不等于"0"就是有排序被点
            String iSortCol_0 = jsonArray.getJSONObject(jsonArray.size() - 3).getString("value");
            if (!"0".equals(iSortCol_0)) {
                String orderCloum = "mDataProp_";
                for (int i = 0; i < jsonArray.size(); i++) {
                    String name = jsonArray.getJSONObject(i).getString("name");
                    if ((orderCloum + iSortCol_0).equals(name)) {
                        defaultSortCloum = jsonArray.getJSONObject(i).getString("value");
                        String sortStrategy = jsonArray.getJSONObject(jsonArray.size() - 2).getString("value");
                        orderByClause = tableAlias+defaultSortCloum + StringUtils.SPACE + sortStrategy;
                        return orderByClause;
                    }
                }
            }
            return orderByClause;
        } catch (Exception e) {
            e.printStackTrace();
            return orderByClause;
        }
    }

    public T sort(T t, HttpServletRequest request,String defaultSortCloum,String defaultSortDir,String cols[]){
        //获取客户端需要那一列排序
        String orderColumn = request.getParameter("order[0][column]");
        String length = request.getParameter("length");
        String start = request.getParameter("start");
        String draw = request.getParameter("draw");
        
        t.setLength(StringUtils.isBlank(length) ? 10 : Integer.parseInt(length));
        t.setStart(StringUtils.isBlank(start) ? 0 : Integer.parseInt(start));
        t.setDraw(StringUtils.isBlank(draw) ? 0 : Integer.parseInt(draw));
        
        if (StringUtils.isBlank(orderColumn) || orderColumn.equals("0")) {
            t.setOrderColumn(defaultSortCloum);
            t.setOrderDir(defaultSortDir);
        } else {
            if (cols != null && cols.length > 0) {
                orderColumn = cols[Integer.parseInt(orderColumn)];
                String orderDir = request.getParameter("order[0][dir]");
                t.setOrderColumn(orderColumn);
                t.setOrderDir(orderDir);
            } else {
                orderColumn = request.getParameter("columns[" + orderColumn + "][data]");
                String orderDir = request.getParameter("order[0][dir]");
                t.setOrderColumn(orderColumn);
                t.setOrderDir(orderDir);
            }
        }
        
        // datatables 发送为offset而并非pageNum，需要再此做转换
        int pageNum = t.getStart() == 0 ? 1 : t.getStart() / 10 + 1;
        PageHelper.startPage(pageNum, t.getLength());
        return t;
    }

}
