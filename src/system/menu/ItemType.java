package system.menu;

public enum ItemType {
	MEAL,
	SIDE,
	DRINK;
	
	// Converter
	public static ItemType convert(String str) {
		for (ItemType type : ItemType.values()) {
			if (type.toString().toLowerCase().equals(str.toLowerCase()))
				return type;
		}
		throw new IllegalArgumentException("Item type cannot be converted: " + str);
	}
}
