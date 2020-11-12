package com.github.assemblingpanda;

//Restaurant class
public class Restaurant {
	//declared variables
	private String name;
	private String numStars;
	private String address;
	private String numReviews;
	private String phoneNumber;
	private String website;
	private String operationalHours;
	private String dollars;
	
	//default constructor set default value for instance variable
	public Restaurant() {
		name = "N/A";
		numStars = "N/A";
		address = "N/A";
		numReviews = "N/A";
		phoneNumber = "N/A";
		website = "N/A";
		operationalHours = "N/A";
		dollars = "N/A";
	}

	//overloaded constructor that takes in values for all the instance variables
	public Restaurant(String name, String numStars, String address, String numReviews,
					  String dollars, String phoneNumber, String website, String operationalHours) {
		this.setName(name);
		this.setNumStars(numStars);
		this.setAddress(address);
		this.setNumReviews(numReviews);
		this.setDollars(dollars);
		this.setPhoneNumber(phoneNumber);
		this.setWebsite(website);
		this.setOperationalHours(operationalHours);
	}

	
	//mutator and accessor methods
	//mutator or setter is used to set the values of the private field. 
	//accessor or getter is used to return the values of the private field

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name.equals("")) return;
		this.name = name;
	}

	public String getNumStars() {
		return numStars;
	}

	// Check to make sure numStars is all numbers, plus the " . "
	public void setNumStars(String numStars) {
		if(numStars.equals("") || !(numStars.matches("[0-9][.][0-9]"))) return;
		this.numStars = numStars;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if(address.equals("")) return;
		if(address.contains("Hours")){
			this.address = address.split("Hours")[0];
			return;
		}
		this.address = address;
	}

	public String getNumReviews() {
		return numReviews;
	}

	// Check to make sure numReviews is all numbers, plus the " , "
	public void setNumReviews(String numReviews) {
		if(numReviews.equals("") || !(numReviews.matches("[0-9,]+"))) return;
		this.numReviews = numReviews;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	// Check for all numbers, -, (, )
	public void setPhoneNumber(String phoneNumber) {
		if(phoneNumber.equals("") || !(numReviews.matches("[(][0-9][0-9][0-9][)][ ][0-9][0-9][0-9][-][0-9][0-9][0-9][0-9]"))) return;
		this.phoneNumber = phoneNumber;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		if(website.equals("") || !(website.contains(".com"))) return;
		this.website = website;
	}

	public String getOperationalHours() {
		return operationalHours;
	}

	// Strutured formatting for operational hours and additional information
	public void setOperationalHours(String operationalHours) {
		if(operationalHours.equals("")) return;
		operationalHours = operationalHours.replace("More hours", "\nMore hours:")
				.replace("Delivery", "\nDelivery")
				.replace("Takeout", "\nTakeout")
				.replace("Monday", "\nMonday")
				.replace("Tuesday", "\nTuesday")
				.replace("Wednesday", "\nWednesday")
				.replace("Thursday", "\nThursday")
				.replace("Friday", "\nFriday")
				.replace("Saturday", "\nSaturday")
				.replace("Sunday", "\nSunday");
		this.operationalHours = operationalHours;
	}

	public String getDollars(){
		return dollars;
	}

	public void setDollars(String dollars){
		if(dollars.equals("")) return;
		this.dollars = dollars;
	}

	//ToString method return the string represent in the object. It is used to print the object as string.
	public String toString() {
		return "Name: " + name + "\n"
				+ "Address: " + address + "\n"
				+ "Ratings: " + numStars + "\n"
				+ "Number of Reviews: " + numReviews + "\n"
				+ "Cost: " + dollars + "\n"
				+ "Phone Number: " + phoneNumber + "\n"
				+ "Website: " + website + "\n"
				+ "Today's Operational Hours: " + operationalHours + "\n";
	}
	
	//Two restaurants are equals if and only if they have the same name and same address
	public boolean equals(Object otherObject) {
		//set isEqual to false. If name and address of the restaurant are the same, isEqual will be true.
		boolean isEqual = false;
		if(otherObject != null && otherObject instanceof Restaurant) {
			Restaurant otherRestaurant = (Restaurant)otherObject;
			if(super.equals(otherRestaurant) && this.name.equals(otherRestaurant.name) 
					&& this.address.equals(otherRestaurant.address)){
					isEqual = true;
			}
		}
		return isEqual;
	}
}
