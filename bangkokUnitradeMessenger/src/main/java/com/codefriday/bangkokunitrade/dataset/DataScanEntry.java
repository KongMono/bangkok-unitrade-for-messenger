package com.codefriday.bangkokunitrade.dataset;

public class DataScanEntry {
	private String scan;
	
	private static DataScanEntry instance;

	private DataScanEntry() {
		// Private constructor so nobody can create an instance of your class.
	}

	public static DataScanEntry getInstance() {
		if (instance == null) {
			instance = new DataScanEntry();
		}
		return instance;
	}

	public DataScanEntry(String scan) {
		super();
		this.scan = scan;
	}

	public String getScan() {
		return scan;
	}

	public void setScan(String scan) {
		this.scan = scan;
	}
}

