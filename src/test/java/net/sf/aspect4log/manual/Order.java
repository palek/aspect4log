package net.sf.aspect4log.manual;

import java.math.BigDecimal;
import java.util.Arrays;

public class Order {

	private int id;
	private Customer customer;
	private String address;
	private Item[] items;

	public Order(Customer customer, String address, Item[] items) {
		this.customer = customer;
		// TODO Auto-generated constructor stub
		this.address = address;
		this.items = items;
	}

	public Customer getCustomer() {
		return customer;
	}

	public String getAddress() {
		return address;
	}

	public Item[] getItems() {
		return items;
	}

	public BigDecimal getPrice(){
		BigDecimal result = BigDecimal.ZERO;
		for (Item item : items){
			result=result.add(item.getPrice());
		}
		return result;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", customer=" + customer + ", address=" + address + ", items=" + Arrays.toString(items) + "]";
	}
	
}
