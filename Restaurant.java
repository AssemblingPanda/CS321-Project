//Restaurant class
public class Restaurant {
	//declared variables
	private String name;
	private double numStars;
	private String address;
	private int numReviews;
	private String restaurantType;
	private String phoneNumber;
	private String website;
	private String operationalHours;
	
	//default constructor set default value for instance variable
	public Restaurant() {
		name = "";
		numStars = 0.00;
		address = "";
		numReviews = 0;
		restaurantType = "";
		phoneNumber = "";
		website = "";
		operationalHours = "";
	}
	
	//overloaded constructor that takes in values for all the instance variables
	public Restaurant(String name, double numStars, String address, int numReviews, 
			String restaurantType, String phoneNumber, String website, String operationalHours) {
			this.name = name;
			this.numStars = numStars;
			this.address = address;
			this.numReviews = numReviews;
			this.restaurantType = restaurantType;
			this.phoneNumber = phoneNumber;
			this.website = website;
			this.operationalHours = operationalHours;
			
	}
	
	
	//mutator and accessor methods
	//mutator or setter is used to set the values of the private field. 
	//accessor or getter is used to return the values of the private field

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNumStars() {
		return numStars;
	}

	public void setNumStars(double numStars) {
		this.numStars = numStars;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getNumReviews() {
		return numReviews;
	}

	public void setNumReviews(int numReviews) {
		this.numReviews = numReviews;
	}

	public String getRestaurantType() {
		return restaurantType;
	}

	public void setRestaurantType(String restaurantType) {
		this.restaurantType = restaurantType;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getOperationalHours() {
		return operationalHours;
	}

	public void setOperationalHours(String operationalHours) {
		this.operationalHours = operationalHours;
	}
	
	//ToString method return the string represent in the object. It is used to print the object as string.
	public String toString() {
		return "Name : " + name + "\n"
				+ "Address : " + address + "\n"
				+ "Restaurant Type : " + restaurantType + "\n"
				+ "Phone Number : " + phoneNumber + "\n"
				+ "Website : " + website + "\n"
				+ "Today's operational Hours : " + operationalHours + "\n";
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
	
	public static void main(String [] args){
		
	}
}
