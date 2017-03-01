package com.cw360.intranet.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Cursor;

public class ListCursor<T> {

	private List<T> lista = new ArrayList<T>();
	private Cursor cursor = null;
	
	public ListCursor() {
	}
	
	public ListCursor(List<T> lista, Cursor cursor) {
		this.lista = lista;
		this.cursor = cursor;
	}

	public List<T> getList() {
		return lista;
	}
	
	public void addBean(T bean) {
		this.lista.add(bean);
	}

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}
}