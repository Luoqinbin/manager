package com.badminton.utils;

public class TableParams {
	String sEcho = "";// 记录操作的次数  每次加1
	String iDisplayStart = "";// 起始
	String iDisplayLength = "";// 相当于pageSize
	public String getsEcho() {
		return sEcho;
	}
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
	public String getiDisplayStart() {
		return iDisplayStart;
	}
	public void setiDisplayStart(String iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}
	public String getiDisplayLength() {
		return iDisplayLength;
	}
	public void setiDisplayLength(String iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}
	
	
}
