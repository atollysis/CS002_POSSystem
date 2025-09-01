package io.csv.converters;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import io.csv.CSVConverter;
import system.transactions.DineType;
import system.transactions.PaymentType;
import system.transactions.Transaction;

public class TransactionCSVConverter implements CSVConverter<Transaction> {

	@Override
	public String getHeader() {
		return String.join(",",
				"id",
				"timestamp",
				"accountId",
				"paymentType",
				"dineType",
				"totalPrice",
				"payment",
				"change",
				"referenceNumber")
				+ '\n';
	}

	@Override
	public String serialize(Transaction obj) {
		return String.join(",",
				Integer.toString(obj.getID()),
				obj.getTimestamp().toString(),
				Integer.toString(obj.getEmployeeID()),
				obj.getPaymentType().toString(),
				obj.getDineType().toString(),
				Integer.toString(obj.getTotalPrice()),
				Integer.toString(obj.getPayment()),
				Integer.toString(obj.getChange()),
				obj.getReferenceNumber() == null ? "" : obj.getReferenceNumber())
				+ '\n';
	}

	@Override
	public Transaction deserialize(String csvLine) {
		String[] fields = Arrays.stream(csvLine.split(","))
				.map(String::trim) // mostly to remove the '\n'
				.toArray(String[]::new);
		return new Transaction(
				Integer.parseInt(fields[0]),
				LocalDateTime.parse(fields[1]),
				Integer.parseInt(fields[2]),
				new ArrayList<>(),
				PaymentType.convert(fields[3]),
				DineType.convert(fields[4]),
				Integer.parseInt(fields[5]),
				Integer.parseInt(fields[6]),
				Integer.parseInt(fields[7]),
				fields[8]);
	}

}
