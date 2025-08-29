package system.accounts;

import java.util.Objects;

public class Account {
	/*
	 * ATTRIBUTES
	 */
	private final int id;
	
	private String name;
	private String pin;
	
	/*
	 * CONSTRUCTOR
	 */
	public Account(int id, String name, String pin) {
		this.id = id;
		this.name = name;
		this.pin = pin;
	}
	
	/*
	 * SERVICE METHODS
	 */
	public boolean verify(String pin) {
		return this.pin.equals(pin);
	}
	
	/*
	 * GETTERS
	 */
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPin() {
		return this.pin;
	}
	
	/*
	 * SETTERS
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPin(String pin) {
		this.pin = pin;
	}
	
	/*
	 * OVERRIDDEN METHODS
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!this.getClass().equals(obj.getClass()))
			return false;
		
		Account other = (Account) obj;
		return this.id == other.id;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}
}
