package system.transactions;

public enum DineType {
	DINE_IN,
	TAKEOUT;
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
	
	public static DineType convert(String str) {
		for (DineType type : DineType.values()) {
			if (str.equalsIgnoreCase(type.toString()))
				return type;
		}
		return null;
	}
}
