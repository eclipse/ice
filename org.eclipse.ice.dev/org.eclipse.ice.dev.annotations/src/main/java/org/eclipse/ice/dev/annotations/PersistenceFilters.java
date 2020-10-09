package org.eclipse.ice.dev.annotations;

import java.util.UUID;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

/**
 * Customized filtering behavior for MongoDB persistence.
 *
 * @author Daniel Bluhm
 */
public class PersistenceFilters {
	
	/* 
	 * Explicit private constructor to hide implicit public constructor, otherwise this would be a code smell
	 */
	private PersistenceFilters() {
		
	}
	/**
	 * Create a filter for a UUID.
	 *
	 * MongoDB stores UUIDs in binary representation. We store UUIDs as Strings.
	 * This method solves this special case.
	 * @param key UUID key to match
	 * @param val UUID value to be converted to string before filtering
	 * @return MongoDB Filter
	 */
	public static Bson eq(String key, UUID val) {
		return Filters.eq(key, val.toString());
	}

	/**
	 * Treat all non-UUID values as normal.
	 * @param key to match
	 * @param val to match
	 * @return MongoDB Filter
	 */
	public static Bson eq(String key, Object val) {
		return Filters.eq(key, val);
	}
}
