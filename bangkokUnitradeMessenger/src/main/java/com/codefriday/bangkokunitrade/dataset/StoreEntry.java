package com.codefriday.bangkokunitrade.dataset;

public class StoreEntry {
	private int id;
	private	String request_no;
	private	String status;
	
	public StoreEntry(){
		
	}
	
	public StoreEntry(int id, String request_no, String status) {
		super();
		this.id = id;
		this.request_no = request_no;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRequest_no() {
		return request_no;
	}

	public void setRequest_no(String request_no) {
		this.request_no = request_no;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

	
}
