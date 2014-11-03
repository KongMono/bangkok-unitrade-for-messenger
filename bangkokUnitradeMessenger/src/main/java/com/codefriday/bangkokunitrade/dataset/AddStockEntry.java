package com.codefriday.bangkokunitrade.dataset;

public class AddStockEntry {
	private String status;
	private String message;
	private String order_id;
	private	String no;
	private	String type;
	private Boolean Checked;
	
	public AddStockEntry(){
		
	}

	public AddStockEntry(String status, String message, String order_id,
			String no, String type, Boolean checked) {
		super();
		this.status = status;
		this.message = message;
		this.order_id = order_id;
		this.no = no;
		this.type = type;
		Checked = checked;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getChecked() {
		return Checked;
	}

	public void setChecked(Boolean checked) {
		Checked = checked;
	}
	
	
	
}
