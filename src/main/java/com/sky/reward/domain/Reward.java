package com.sky.reward.domain;

import java.io.Serializable;

public class Reward implements Serializable {
	
	private String name;
	
	public Reward() {
	}
	
	public Reward(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
