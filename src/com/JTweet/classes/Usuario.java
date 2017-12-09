package com.JTweet.classes;

import java.util.GregorianCalendar;

public class Usuario {
	private String user,psw,name,surename,eMail,born;
	private int ID;
	public Usuario(String user,String psw,String name,String surename,String eMail,String born) {
		this.user=user;
		this.psw=psw;
		this.name=name;
		this.surename=surename;
		this.eMail=eMail;
		this.born=born;
	}
	public void setID(int ID) {
		this.ID=ID;
	}
	public String getUser() {
		return user;
	}
	public String getPsw() {
		return psw;
	}
	public String getName() {
		return name;
	}
	public String getSurename() {
		return surename;
	}
	public String geteMail() {
		return eMail;
	}
	public String getBorn() {
		return born;
	}
	public int getID() {
		return ID;
	}
	
}