package com.codefriday.bangkokunitrade.dataset;

public class DetailEntry {
	private int id;
	private	String no;
	private	String sale;
	private	String hospital;
	
	public DetailEntry(){
		
	}

	public DetailEntry(int id, String no, String sale, String hospital) {
		super();
		this.id = id;
		this.no = no;
		this.sale = sale;
		this.hospital = hospital;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	
	
}
