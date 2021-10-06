package com.example;

public class bank {
	int id;
	String owner1;
	String owner2;
	String type;
	double funds;
	boolean active;
	
	public bank() {
	}
	
	public bank(int id, String owner1, String owner2, String type, double funds, boolean active) {
		super();
		this.id = id;
		this.owner1 = owner1;
		this.owner2 = owner2;
		this.type = type;
		this.funds = funds;
		this.active = active;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOwner1() {
		return owner1;
	}

	public void setOwner1(String owner1) {
		this.owner1 = owner1;
	}

	public String getOwner2() {
		return owner2;
	}

	public void setOwner2(String owner2) {
		this.owner2 = owner2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getFunds() {
		return funds;
	}

	public void setFunds(double funds) {
		this.funds = funds;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "bank [id=" + id + ", owner1=" + owner1 + ", owner2=" + owner2 + ", type=" + type + ", funds=" + funds
				+ ", active=" + active + "]";
	}
	
	
	
	
	

}
