package com.coffee;

import java.io.Serializable;

public class Coffee implements Serializable {

    public String id;

    public String name;

    public int price;

    public Coffee(){

    }

    public Coffee(String id, String name, int price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
