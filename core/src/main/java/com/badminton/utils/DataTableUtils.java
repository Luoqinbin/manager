package com.badminton.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DataTableUtils{

	public static TableParams paramJson(String aoData){
		TableParams tp = new TableParams();
		JSONArray ja = JSONArray.fromObject(aoData);
		// 分别为关键的参数赋值
		for (int i = 0; i < ja.size(); i++) {
			JSONObject obj = (JSONObject) ja.get(i);
			if (obj.get("name").equals("sEcho")){
				tp.setsEcho(obj.get("value").toString());
			}else if (obj.get("name").equals("iDisplayStart")){
				tp.setiDisplayStart(obj.get("value").toString());
			}else if (obj.get("name").equals("iDisplayLength")){
				tp.setiDisplayLength(obj.get("value").toString());
			}
		}
		return tp;
	}
	
	public static Map<String,Object> queryTableContent(Long count,List<?> list,String sEcho){
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		
		int  initEcho = Integer.parseInt(sEcho)+1;
		dataMap.put("iTotalRecords", count);
		dataMap.put("sEcho", initEcho);
		dataMap.put("iTotalDisplayRecords", count);
		dataMap.put("aaData", list);
		
		return dataMap;
	}
}
