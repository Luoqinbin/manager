package com.badminton.interceptors.mySqlHelper.conditionHelper;


import com.badminton.entity.BaseEntity;
import javassist.Modifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ethan
 * @date 下午9:30:10
 * @email windofdusk@gmail.com
 * 类说明
 */
public class EntityHelper {

	public static class EntityColumn{
		private String property;
		private String column;
		
		public String getProperty(){
			return property;
		}
		
		public void setProperty(String property){
			this.property = property;
		}
		
		public String getColumn(){
			return column;
		}
		
		public void setColumn(String column){
			this.column = column;
		}
	}
	
	/**
	 * 获取所有column
	 * @param entityClass
	 * @return
	 */
	public static List<EntityColumn> getAllColumns(Class<?> entityClass){
		//初始化column
		initFiledMap(entityClass);
		//获取所有的column
		return entityFieldMap.get(entityClass.getName());
	}
	
	private static final Map<String, List<EntityColumn>> entityFieldMap = new ConcurrentHashMap<String, List<EntityColumn>>();
	
	/**
	 * 初始化实体类的字段Map
	 * @param entityClass
	 */
	public static synchronized void initFiledMap(Class<?> entityClass){
		if(entityFieldMap.containsKey(entityClass.getName()) == false){
			List<Field> fieldList = getAllField(entityClass, null);
			List<EntityColumn> columnList = new ArrayList<EntityColumn>();
			for(Field field : fieldList){
				EntityColumn column = new EntityColumn();
				column.setProperty(field.getName());
				column.setColumn(ColumnHelper.toColumn(field.getName()));
				columnList.add(column);
			}
			entityFieldMap.put(entityClass.getName(), columnList);
		}
	}
	
	private static List<Field> getAllField(Class<?> entityClass, List<Field> fieldList){
		if(fieldList == null){
			fieldList = new ArrayList<Field>();
		}
		if(entityClass.equals(BaseEntity.class)){
			//基类不读取
			return fieldList;
		}
		Field[] fields = entityClass.getDeclaredFields();
		for(Field field : fields){
			//静态字段不会作为数据库属性
			if(!Modifier.isStatic(field.getModifiers())){
				fieldList.add(field);
			}
		}
		if(entityClass.getSuperclass() != null
				&& !entityClass.getSuperclass().equals(Object.class)
				&& !Map.class.isAssignableFrom(entityClass.getSuperclass())
				&& !Collection.class.isAssignableFrom(entityClass.getSuperclass())){
			return getAllField(entityClass.getSuperclass(), fieldList);
		}
		return fieldList;
	}
}




















