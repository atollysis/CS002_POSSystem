package src.system.menu;

public enum ItemSize {
	REGULAR,
	MEDIUM,
	LARGE;
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
	
	public static ItemSize convert(String str) {
		for (ItemSize type : ItemSize.values()) {
			if (str.equalsIgnoreCase(type.toString()))
				return type;
		}
		return null;
	}
}
