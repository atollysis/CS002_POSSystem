package io.csv.converters;

import java.util.Arrays;

import io.csv.CSVConverter;
import system.menu.ItemSize;
import system.ordering.CartDetails;

public class CartItemCSVConverter implements CSVConverter<CartDetails> {

	@Override
	public String getHeader() {
		return String.join(",",
				"transactionID",
				"itemID",
				"itemSize",
				"itemPrice",
				"itemQuantity",
				"subtotal")
				+ '\n';
	}

	@Override
	public String serialize(CartDetails obj) {
		return String.join(",",
				Integer.toString(obj.getTransactionID()),
				Integer.toString(obj.getItemID()),
				obj.getItemSize().toString(),
				Integer.toString(obj.getItemPrice()),
				Integer.toString(obj.getItemQuantity()),
				Integer.toString(obj.getSubtotal())
				);
	}

	@Override
	public CartDetails deserialize(String csvLine) {
		String[] fields = Arrays.stream(csvLine.split(","))
				.map(String::trim) // mostly to remove the '\n'
				.toArray(String[]::new);
		return new CartDetails(
				Integer.parseInt(fields[0]),
				Integer.parseInt(fields[1]),
				ItemSize.convert(fields[2]),
				Integer.parseInt(fields[3]),
				Integer.parseInt(fields[4]),
				Integer.parseInt(fields[5])
				);
	}

}
