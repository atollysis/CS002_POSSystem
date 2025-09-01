package system.menu;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;

import io.csv.CSVConverter;
import io.csv.CSVManager;
import io.csv.converters.MenuCSVConverter;

public class MenuManager {
	/*
	 * ATTRIBUTES
	 */
	private static final Set<Character> INVALID_CHARS = Set.of('"', ',');
	private static final String CSV_FILENAME = "items.csv";
	private static final int IMG_DIMS = 100;
	
	private static CSVConverter<MenuItem> converter = new MenuCSVConverter();
	private static Map<MenuItem, Image> itemImgs;
	private static List<MenuItem> items;
	
	/*
	 * STATIC BLOCK
	 */
	static {
		loadCSV();
		loadImages();
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
	
	private static void loadImages() {
		itemImgs = new HashMap<>();
		for (MenuItem item : items) {
			ImageIcon i = new ImageIcon(String.format(
					"data/images/menu/%s/%s.jpg",
					item.getItemType().toString(),
					item.getName().toLowerCase().replaceAll(" ", "")));
			Image resized = i.getImage().getScaledInstance(IMG_DIMS, IMG_DIMS, Image.SCALE_SMOOTH);
			itemImgs.put(item, resized);
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
		for (MenuItem item : items) {
			if (item.getId() == id)
				return item;
		}
		return null;
	}
	
	public static MenuItem findItemByName(String name) {
		for (MenuItem item : items) {
			if (item.getName().equalsIgnoreCase(name))
				return item;
		}
		return null;
	}
	
	public static Image getItemImage(MenuItem item) {
		return itemImgs.get(item);
	}
	
	public static List<MenuItem> getAllItems() {
		return items;
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
		if (!isValidPrice(priceReg))
			return MenuOpResult.INVALID_PRICE_R;
		if (!isValidPrice(priceMed))
			return MenuOpResult.INVALID_PRICE_M;
		if (!isValidPrice(priceLrg))
			return MenuOpResult.INVALID_PRICE_L;
		
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
