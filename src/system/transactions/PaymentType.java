package system.transactions;

public enum PaymentType {
	CASH,
	CARD,
	ONLINE;
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
	
	public static PaymentType convert(String str) {
		for (PaymentType type : PaymentType.values()) {
			if (str.equalsIgnoreCase(type.toString()))
				return type;
		}
		return null;
	}
	
	public boolean isNotNullAndCash() {
		return this != null && this != CASH;
	}
	
	public String toName() {
		return this.toString().charAt(0) + this.toString().substring(1).toLowerCase();
	}
}
