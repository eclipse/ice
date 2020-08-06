package org.eclipse.ice.dev.annotations.processors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Types {

	/**
	 * Type matching regex used in shortening types. Can also be used for getting
	 * the last element of a type hierarchy (through capturing group 1).
	 */
	private static final Pattern TYPE_SHORTENER = Pattern.compile("(?:\\w+\\.)*([\\w\\$]+)\\b");

	/**
	 * Lookup table for fully qualified types to their shortened types.
	 *
	 * This is used to record collisions by mapping fully qualified types to
	 * themselves when another type would have conflicted with the shortened
	 * version.
	 */
	private Map<String, String> fullToShort;

	/**
	 * Lookup table for shortened types to their fully qualified types.
	 *
	 * This is used to determine what types must be imported.
	 */
	private Map<String, String> shortToFull;

	/**
	 * The set of all types present on the fields accounted for by this instance
	 * of Types.
	 */
	private Set<String> allTypes;

	public Types(Iterable<Field> fields) {
		this.fullToShort = new HashMap<>();
		this.shortToFull = new HashMap<>();
		this.allTypes = new HashSet<>();

		// Extract types
		for (Field field : fields) {
			Matcher matcher = TYPE_SHORTENER.matcher(field.getType());
			while (matcher.find()) {
				allTypes.add(matcher.group());
			}
		}

		// Determine shortened names, populating the look up tables while
		// detecting collisions
		Set<String> collisions = new HashSet<>();
		for (String type : allTypes) {
			String shortened = getShortenedType(type);
			if (collisions.contains(shortened)) {
				fullToShort.put(type, type);
			} else if (shortToFull.containsKey(shortened)) {
				fullToShort.put(type, type);
				shortToFull.put(type, type);
				String previous = shortToFull.remove(shortened);
				shortToFull.put(previous, previous);
				fullToShort.put(previous, previous);
				collisions.add(shortened);
			} else {
				fullToShort.put(type, shortened);
				shortToFull.put(shortened, type);
			}
		}
	}

	/**
	 * Return the short name of this field's type.
	 * @return the short name of this field's type.
	 */
	public static String getShortenedType(String type) {
		StringBuffer shortenedType = new StringBuffer();
		Matcher matcher = TYPE_SHORTENER.matcher(type);
		while (matcher.find()) {
			matcher.appendReplacement(shortenedType, "$1");
		}
		matcher.appendTail(shortenedType);
		return shortenedType.toString().replace("$", ".");
	}

	/**
	 * Return set of strings representing the required imports of this field.
	 * @return set of strings to import
	 */
	public Set<String> getImports() {
		return shortToFull.entrySet().stream()
			.filter(entry -> !entry.getKey().equals(entry.getValue()))
			.map(entry -> entry.getValue())
			.filter(type -> !type.startsWith("java.lang"))
			.collect(Collectors.toSet());
	}

	/**
	 * Resolve the type and its type parameters, shortening all types that
	 * can be shortened.
	 * @param type to look up
	 * @return shortened type if no collisions, full type if it would collide
	 */
	public String resolve(String type) {
		StringBuffer resolved = new StringBuffer();
		Matcher matcher = TYPE_SHORTENER.matcher(type);
		while (matcher.find()) {
			matcher.appendReplacement(
				resolved,
				fullToShort.get(matcher.group())
			);
		}
		matcher.appendTail(resolved);
		return resolved.toString();
	}
}