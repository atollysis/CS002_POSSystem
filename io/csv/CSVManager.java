package io.csv;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class CSVManager {
	/*
	 * ATTRIBUTES
	 */
	private static final Path DATA_DIR = Paths.get("data");
	
	/*
	 * SUPPORT METHODS
	 */
	private static Path buildPath(String filename) {
		return DATA_DIR.resolve(filename);
	}
	
	/*
	 * SERVICE METHODS
	 */
	public static <T> List<T> loadAll(
			String filename,
			CSVConverter<T> converter) throws IOException {
		List<String> rows = Files.readAllLines(buildPath(filename), StandardCharsets.UTF_8);
		rows.remove(0); // consume header
		return rows.stream()
				.map(converter::deserialize)
				.collect(Collectors.toList());
//		List<T> output = new ArrayList<>();
//		for (String row : rows) {
//			T obj = converter.deserialize(row);
//			output.add(obj);
//		}
//		return output;
	}
	
	public static <T> void saveAll(
			String filename,
			List<T> list,
			CSVConverter<T> converter) throws IOException {
//		List<String> rows = new ArrayList<>();
//		for (T obj : list) {
//			String row = converter.serialize(obj);
//			rows.add(row);
//		}
		List<String> rows = list
				.stream()
				.map(converter::serialize)
				.collect(Collectors.toList());
		rows.add(0, converter.getHeader());
		Files.write(buildPath(filename), rows);
	}
}
