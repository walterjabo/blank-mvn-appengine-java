package com.cw360.intranet.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.Query.Filter;

/**
 * @author Walter Jabo B.
 *
 * @created 27/3/2015 9:24:25
 *
 */
public abstract class BaseDaoAbstract<T> {
	
	public abstract T buildBean(Entity entity);
	public abstract Entity buildEntity(T object);
	public abstract String getDataStoreKind();
	
	public abstract PreparedQuery getPreparedQuery(Query query);
	
	public Key createKey(Object keyValue) {
		Key key = null;
		if(keyValue instanceof Long) {
			key = KeyFactory.createKey(getDataStoreKind(), (Long) keyValue);
		} else {
			key = KeyFactory.createKey(getDataStoreKind(), (String) keyValue);
		}
		return key;
	}
	
	public Query getQuery(Filter filtro, boolean keysOnly) {
		Query q = new Query(getDataStoreKind());
		q = filtro != null ? q.setFilter(filtro) : q;
		q = keysOnly ? q.setKeysOnly() : q;
		return q;
	}
	public PreparedQuery getPreparedQuery(Filter filtro, boolean keysOnly) {
		PreparedQuery pq = getPreparedQuery(getQuery(filtro, keysOnly));
		return pq;
	}
	
	public FetchOptions getPageOptions(Cursor startCursor, Integer limit) {
		FetchOptions options = FetchOptions.Builder.withDefaults();
		options = limit != null ? options.limit(limit) : options;
		options = startCursor != null ? options.startCursor(startCursor) : options;
		return options;
	}
	public FetchOptions getPageOptions(Cursor startCursor, Cursor endCursor, Integer limit, Integer chunkSize, Integer offset, Integer prefetchSize) {
		FetchOptions options = FetchOptions.Builder.withDefaults();
		options = limit != null ? options.limit(limit) : options;
		options = startCursor != null ? options.startCursor(startCursor) : options;
		options = chunkSize != null ? options.chunkSize(chunkSize) : options;
		options = endCursor != null ? options.endCursor(endCursor) : options;
		options = offset != null ? options.offset(offset) : options;
		options = prefetchSize != null ? options.prefetchSize(prefetchSize) : options;
		return options;
	}
	
	public List<T> getBeanList(Filter filtro, Cursor cursor, Integer limit) {
		return getBeanList(filtro, cursor, limit, false);
	}

	public List<T> getBeanList(Filter filtro, Cursor cursor, Integer limit, boolean keysOnly) {
		Query q = getQuery(filtro, keysOnly);
		return getBeanListByQuery(q, cursor, limit);
	}
	public List<T> getBeanListByQuery(Query query, Cursor cursor, Integer limit) {
		return getBeanList(query, getPageOptions(cursor, limit));
	}
	public List<T> getBeanList(Query query, FetchOptions fetchOptions) {
		PreparedQuery pq = getPreparedQuery(query);
		List<T> list = new ArrayList<T>();
		Iterable<Entity> it = pq.asIterable(fetchOptions);
		for (Entity entity : it) {
			list.add(buildBean(entity));
		}
		return list;
	}
	
	public ListCursor<T> getListCursor(Filter filtro, Cursor cursor, Integer limit, boolean keysOnly) {
		Query query = getQuery(filtro, keysOnly);
		FetchOptions options = getPageOptions(cursor, limit);
		return getListCursor(query, options);
	}
	
	public ListCursor<T> getListCursor(Query query, FetchOptions fetchOptions) {
		PreparedQuery pq = getPreparedQuery(query);
		QueryResultIterator<Entity> it = (fetchOptions != null ? pq.asQueryResultIterator(fetchOptions) : pq.asQueryResultIterator());
		ListCursor<T> listaCursor = new ListCursor<T>();
		while(it.hasNext()) {
			listaCursor.addBean(buildBean(it.next()));
		}
		listaCursor.setCursor(it.getCursor());
		return listaCursor;
	}

	public List<Key> getKeysList(Filter filtro, Cursor cursor, Integer limit) {
		return getKeysCursorList(filtro, cursor, limit).getList();
	}
	
	public ListCursor<Key> getKeysCursorList(Filter filtro, Cursor cursor, Integer limit) {
		
		PreparedQuery pq =  getPreparedQuery(filtro, true);
		FetchOptions options = getPageOptions(cursor, limit);
				
		QueryResultIterator<Entity> it = (options != null ? pq.asQueryResultIterator(options) : pq.asQueryResultIterator());
				
		ListCursor<Key> list = new ListCursor<Key>();
		while(it.hasNext()) {
			list.addBean(it.next().getKey());
		}
		list.setCursor(it.getCursor());
		return list;
	}
	
	public long count(Filter filtro, Integer limit) {
		Cursor startCursor = null;
		ListCursor<T> list;
		long count = 0;
//		int i=0;
		do {
			list = getListCursor(filtro, startCursor, limit, true);
			startCursor = list.getCursor();
			count += list.getList().size();
//			i++;
//			System.out.println(String.format("Corrida i=[%d], Count=[%d]", i, count));
		} while (startCursor != null && !list.getList().isEmpty());
		return count;
	}

}
