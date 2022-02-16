package com.kindminds.drs;

public enum UserTaskType {
	ReviewP2MApplication(1,"Review P2M Application");

	private int key;
	private String name;

	//private static final Map<Integer, UserTask> keyToLocaleMap = new HashMap<Integer, UserTask>();
	//private static final Map<String, UserTask> codeToLocaleMap = new HashMap<String, UserTask>();

	UserTaskType(int key, String name){
		this.key = key;
		this.name = name;
	}
	public int getKey(){ return this.key; }

	public String getName(){ return this.name; }


}
