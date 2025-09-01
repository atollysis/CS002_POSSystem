package system.menu;

import java.util.Map;
import java.util.Objects;

public class MenuItem {
	/*
	 * ATTRIBUTES
	 */
	private final int id;
	
	private String name;
	private ItemType itemType;
	private Map<ItemSize, Integer> sizePriceRange;
	
	/*
	 * CONSTRUCTOR
	 */
	public MenuItem(
			int id,
			String name,
			ItemType itemType,
			Map<ItemSize, Integer> sizePriceRange) {
		this.id = id;
		this.name = name;
		this.itemType = itemType;
		this.sizePriceRange = sizePriceRange;
	}

	/*
	 * GETTERS
	 */
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public Map<ItemSize, Integer> getSizePriceRange() {
		return sizePriceRange;
	}
	
	public Integer getPriceOf(ItemSize type) {
		return this.sizePriceRange.getOrDefault(type, null);
	}

	/*
	 * SETTERS
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public void setSizePriceRange(Map<ItemSize, Integer> sizePriceRange) {
		this.sizePriceRange = sizePriceRange;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		MenuItem other = (MenuItem) obj;
		return id == other.id;
	}
}
