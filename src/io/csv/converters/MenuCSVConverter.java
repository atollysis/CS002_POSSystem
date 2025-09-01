package io.csv.converters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.csv.CSVConverter;
import system.menu.ItemSize;
import system.menu.ItemType;
import system.menu.MenuItem;

public class MenuCSVConverter implements CSVConverter<MenuItem> {

	@Override
	public String getHeader() {
		return String.join(",",
				"id",
				"type",
				"priceRegular",
				"priceMedium",
				"priceLarge")
				+ '\n';
	}
	
	@Override
	public String serialize(MenuItem obj) {
		String rangeString = String.join(",",
				getPriceString(obj, ItemSize.REGULAR),
				getPriceString(obj, ItemSize.MEDIUM),
				getPriceString(obj, ItemSize.LARGE));
		return String.join(",",
				Integer.toString(obj.getId()),
				obj.getName(),
				obj.getItemType().toString().toLowerCase(),
				rangeString)
				+ '\n';
	}
	
	private String getPriceString(MenuItem item, ItemSize size) {
		Integer price = item.getSizePriceRange().get(size);
		return (price == null || price <= 0) ? "" : Integer.toString(price);
	}

	@Override
	public MenuItem deserialize(String csvLine) {
		String[] fields = Arrays.stream(csvLine.split(","))
				.map(String::trim) // mostly to remove the '\n'
				.toArray(String[]::new);
		Map<ItemSize, Integer> sizePriceRange = new HashMap<>();
		sizePriceRange.put(ItemSize.REGULAR,	parsePrice(fields[3]));
		sizePriceRange.put(ItemSize.MEDIUM,		parsePrice(fields[4]));
		sizePriceRange.put(ItemSize.LARGE,		parsePrice(fields[5]));
		return new MenuItem(
				Integer.parseInt(fields[0]),	// ID
				fields[1],						// name
				ItemType.convert(fields[2]),	// type
				sizePriceRange);				// size price range
	}
	
	private Integer parsePrice(String price) {
		int output;
		try {
			output = Integer.parseInt(price);
		} catch (NumberFormatException e) {
			return null;
		}
		if (output <= 0)
			return null;
		return output;
	}

}
