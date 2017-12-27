package org.txemasv.mycharacters.apiserver.util;

import java.util.List;

import org.txemasv.mycharacters.apiserver.model.Character;

public class ResponseType {

	private long count;
	private long limit;
	private long page;
	private String next;
	private String previous;
	private String info;
	private List<Character> characters;
	
	public ResponseType() {}
	
	public ResponseType(long count, long limit, long page, String next, String previous, String info, List<Character> characters) {
		this.count = count;
		this.limit = limit;
		this.page = page;
		this.next = next;
		this.previous = previous;
		this.info = info;
		this.characters = characters;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public List<Character> getCharacters() {
		return characters;
	}

	public void setCharacters(List<Character> characters) {
		this.characters = characters;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
	
}
