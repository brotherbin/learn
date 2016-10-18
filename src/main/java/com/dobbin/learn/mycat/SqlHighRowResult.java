package com.dobbin.learn.mycat;

public class SqlHighRowResult {
	
	private Integer id;
	
	private String user;
	
	private Integer frequency;
	
	private Integer avgTime;
	
	private Integer maxTime;
	
	private Integer minTime;
	
	private Integer executeTime;
	
	private Long lastTime;
	
	private String sql;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Integer getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(Integer avgTime) {
		this.avgTime = avgTime;
	}

	public Integer getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Integer maxTime) {
		this.maxTime = maxTime;
	}

	public Integer getMinTime() {
		return minTime;
	}

	public void setMinTime(Integer minTime) {
		this.minTime = minTime;
	}

	public Integer getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Integer executeTime) {
		this.executeTime = executeTime;
	}

	public Long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}
