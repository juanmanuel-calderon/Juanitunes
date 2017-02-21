package com.jmc.juanitunes.cli.api;

public interface Context {
	
	public Context open(int child);
	public Context close();
	public String pwd();
	public String list();
	public String detail(int child);
	
	public default void help() {
		System.out.println("List of available commands:");
		System.out.println("\topen <num>: go to child");
		System.out.println("\tclose: go back one level");
		System.out.println("\tpwd: show current path");
		System.out.println("\tlist: list elements in this node");
		System.out.println("\tdetail <num>: show detailed info about child");
		System.out.println("\thelp: display this message");
		System.out.println("\texit: exit the application");
	}
}
