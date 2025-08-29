package io.csv.converters;

import java.util.Arrays;
import io.csv.CSVConverter;
import src.system.accounts.Account;

public class AccountCSVConverter implements CSVConverter<Account> {

	@Override
	public String getHeader() {
		return String.join(",",
				"id",
				"name",
				"pin")
				+ '\n';
	}
	
	@Override
	public String serialize(Account obj) {
		return String.join(",",
				Integer.toString(obj.getID()),
				obj.getName(),
				obj.getPin())
				+ '\n';
//		return String.format(
//				"%d,%s,%s,%s\n",
//				obj.getID(),
//				obj.getName(),
//				obj.getPin(),
//				obj.getType().toString());
	}

	@Override
	public Account deserialize(String csvLine) {
		String[] fields = Arrays.stream(csvLine.split(","))
				.map(String::trim) // mostly to remove the '\n'
				.toArray(String[]::new);
		return new Account(
				Integer.parseInt(fields[0]),	// ID
				fields[1],						// Name
				fields[2]);						// Pin
	}
	
}
