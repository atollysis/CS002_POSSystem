package system.menu;

public enum ItemSize {
	REGULAR,
	MEDIUM,
	LARGE;
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
	
	public String toName() {
		return this.toString().charAt(0) + this.toString().substring(1);
	}
	
	public static ItemSize convert(String str) {
		for (ItemSize type : ItemSize.values()) {
			if (str.equalsIgnoreCase(type.toString()))
				return type;
		}
		return null;
	}
}
