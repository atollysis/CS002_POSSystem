package src.system.menu;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import io.csv.CSVConverter;
import io.csv.CSVManager;
import io.csv.converters.MenuCSVConverter;

public class MenuManager {
	/*
	 * ATTRIBUTES
	 */
	private static final Set<Character> INVALID_CHARS = Set.of('"', ',');
	private static final String CSV_FILENAME = "items.csv";
	
	private static CSVConverter<MenuItem> converter = new MenuCSVConverter();
	private static List<MenuItem> items;
	
	/*
	 * STATIC BLOCK
	 */
	static {
		loadCSV();
	}
	
	/*
	 * SUPPORT METHODS
	 */
	private static void saveCSV() {
		try {
			CSVManager.saveAll(CSV_FILENAME, items, converter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void loadCSV() {
		try {
			items = CSVManager.loadAll(CSV_FILENAME, converter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int findMaxID() {
		return items.stream()
				.mapToInt(MenuItem::getId)
				.max()
				.orElse(0);
	}
	
	private static boolean isValidName(String name) {
		if (name.isBlank())
			return false;
		if (name.chars().anyMatch(ch -> INVALID_CHARS.contains((char) ch)))
			return false;
		
		return true;
	}
	
	private static boolean isValidPrice(String price) {
		if (price.isBlank())
			return false;
		if (!price.chars().allMatch(Character::isDigit))
			return false;
		
		return true;
	}
	
	/*
	 * SERVICE METHODS
	 */
	public static MenuItem findItemById(int id) {
		/*
		 * TODO: Iterate over this.items and return
		 * the current menu item if the IDs match.
		 * If no match, return null.
		 */
		return null;
	}
	
	public static MenuItem findItemByName(String name) {
		/*
		 * TODO: Iterate over this.items and return
		 * the current menu item if the names match.
		 * If no match, return null.
		 */
		return null;
	}
	
	public static MenuOpResult addNewItem(
			String name,
			ItemType itemType,
			String priceReg,
			String priceMed,
			String priceLrg) {
		// Guard clauses
		if (!isValidName(name))
			return MenuOpResult.INVALID_NAME;
		/*
		 * TODO:
		 * Make similar guard clauses for invalid prices
		 * (use isValidPrice() above)
		 * See MenuOpResult for the appropriate return values.
		 */
		
		Map<ItemSize, Integer> sizePriceRange = Map.ofEntries(
				Map.entry(ItemSize.REGULAR, Integer.parseInt(priceReg)),
				Map.entry(ItemSize.MEDIUM, Integer.parseInt(priceMed)),
				Map.entry(ItemSize.LARGE, Integer.parseInt(priceLrg)));
		for (var size : sizePriceRange.keySet()) {
			if (sizePriceRange.get(size) <= 0)
				sizePriceRange.remove(size);
		}
		items.add(new MenuItem(
				findMaxID() + 1,
				name,
				itemType,
				sizePriceRange));
		saveCSV();
		return MenuOpResult.SUCCESS;
	}
	
	public static MenuOpResult deleteItemById(int id) {
		MenuItem item = findItemById(id);
		if (item == null)
			return MenuOpResult.INVALID_ITEM_INDEX;
		
		items.remove(item);
		saveCSV();
		return MenuOpResult.SUCCESS;
	}
}
