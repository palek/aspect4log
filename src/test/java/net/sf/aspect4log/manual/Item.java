package net.sf.aspect4log.manual;

import java.math.BigDecimal;

public class Item {
	int id;
	String name;
	BigDecimal price;
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public Item(int id, String name, BigDecimal price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
}
