package com.cw360.intranet.dao;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Text;

public class DatastoreUtilsDao {
	
	static Logger logger = Logger.getLogger(DatastoreUtilsDao.class.getName());
	
	@SuppressWarnings("rawtypes")
	public static long getCount(Class clasz) {
		Map<String, Object> map = getStatistics(clasz, null);
		Long longValue = map != null ? (Long) map.get("count") : -1;
		return longValue != null ? longValue : -1;
	}
	
	@SuppressWarnings("rawtypes")
	public static long getCount(Class clasz, Filter filter) {
		Map<String, Object> map = getStatistics(clasz, filter);
		Long longValue = map != null ? (Long) map.get("count") : -1;
		return longValue != null ? longValue : -1;
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, Object> getStatistics(Class clasz, Filter filter) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("__Stat_Kind__");
		Filter classFilter = new FilterPredicate("kind_name", FilterOperator.EQUAL, clasz.getSimpleName());
		Filter finalFilter = filter != null ? CompositeFilterOperator.and(classFilter, filter) : classFilter;
		query.setFilter(finalFilter);
		Entity globalStat = datastore.prepare(query).asSingleEntity();
		Map<String, Object> map = globalStat != null ? globalStat.getProperties() : null;
		
		if(map!=null) {
			Iterator<String> it = map.keySet().iterator();
			while(it.hasNext()) {
				String k = it.next();
				logger.info(String.format("[%s] => [%s]", k, (String) map.get(k)));
			}
		} else {
			logger.info("El map del count es nullo");
		}
		return map;
	}
	
	public static String getTextString(Entity entity, String propertyName) {
		Object obj = entity.getProperty(propertyName);
		if(obj instanceof Text) {
			return ((Text) obj).getValue();
		} else {
			return (String) obj;
		}
	}
	
	public static Object getTextOrStringEntity(String stringValue) {
		if (stringValue != null && stringValue.length() >= 500) {
			return new Text(stringValue);
		} else {
			return stringValue;
		}
	}
	
}
