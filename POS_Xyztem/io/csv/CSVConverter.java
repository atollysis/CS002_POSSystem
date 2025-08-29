package io.csv;

public interface CSVConverter<T> {
	String getHeader();
	String serialize(T obj);
	T deserialize(String csvLine);
}
