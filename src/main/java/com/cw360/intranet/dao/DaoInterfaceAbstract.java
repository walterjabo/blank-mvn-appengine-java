package com.cw360.intranet.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;


public abstract class DaoInterfaceAbstract<T> extends BaseDaoAbstract<T> {
	
	protected static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	
	public static DatastoreService getDatastoreService() {
		return datastoreService;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public String getDataStoreKind() {
        Class clazz = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return clazz.getSimpleName();
	}
	
	public DaoInterfaceAbstract() {
	}
	
	public DaoInterfaceAbstract(DatastoreService dsService) {
		datastoreService = dsService;
	}
	
	public DatastoreService getService() {
		return datastoreService;
	}
	
	public Transaction beginTransaction() {
		return getService().beginTransaction();
	}

	public Transaction beginTransaction(TransactionOptions to) {
		return getService().beginTransaction(to);
	}
	
	public Key saveBean(T bean) {
		return datastoreService.put(buildEntity(bean));
	}
	
	public Key saveBean(Transaction txn, T bean) {
		return datastoreService.put(txn, buildEntity(bean));
	}

	public T getBean(Object keyValue) {
		return getBean(null, keyValue);
	}
	
	public T getBean(Transaction txn, Object keyValue) {
		if (keyValue == null) { return null; }
		try {
			Key key = createKey(keyValue);
			Entity entity = txn == null ? datastoreService.get(key) : datastoreService.get(txn, key);
			return (T) buildBean(entity);
		} catch (EntityNotFoundException e) {
			return null;
		}
	}
	
	public List<T> getBeanList(List<Key> keysList) {
		Map<Key, Entity> map = datastoreService.get(keysList);
		List<T> lista = new ArrayList<T>();
		for(Key key: map.keySet()) {
			lista.add(buildBean(map.get(key)));
		}
		return lista;
	}
	
	public void deleteBean(Object keyValue) {
		datastoreService.delete(createKey(keyValue));
	}

	public void deleteBeanList(List<Key> keysList) {
		datastoreService.delete(keysList);
	}
	
	public void deleteBean(Transaction txn, Object keyValue) {
		datastoreService.delete(txn, createKey(keyValue));
	}
	
	@Override
	public PreparedQuery getPreparedQuery(Query query) {
		PreparedQuery pq = datastoreService.prepare(query);
		return pq;
	}
	
	
}