package org.eclipse.ice.data;

import java.util.UUID;

public interface IPersistenceHandler<T> {
	/**
	 * Save the DataElement.
	 * @param element Object extending {@code IDataElement}
	 * @throws Exception thrown on failure to save
	 */
	public void save(T element) throws Exception;

	/**
	 * Clear all DataElements from the collection.
	 * @return number of deleted elements
	 * @throws Exception thrown on failure to clear the collection
	 */
	public long clear() throws Exception;

	/**
	 * Find and retrieve all DataElements from the collection.
	 * @return an iterable of the retrieved elements.
	 * @throws Exception thrown on failure to retrieve all elements
	 */
	public Iterable<T> findAll() throws Exception;

	/**
	 * Find DataElement by UUID.
	 * @param uuid to find
	 * @return found element or null
	 * @throws Exception thrown on failure to retrieve element
	 */
	public T findByUUID(UUID uuid) throws Exception;

	/**
	 * Find DataElement by id.
	 * @param id of elements to find
	 * @return Iterator of results
	 * @throws Exception thrown on failure to retrieve elements
	 */
	public Iterable<T> findById(long id) throws Exception;

	/**
	 * Find DataElement by name.
	 * @param name of elements to find
	 * @return Iterator of results
	 * @throws Exception thrown on failure to retrieve elements
	 */
	public Iterable<T> findByName(String name) throws Exception;

	/**
	 * Find DataElement by description.
	 * @param description of elements to find
	 * @return Iterator of results
	 * @throws Exception thrown on failure to retrieve elements
	 */
	public Iterable<T> findByDescription(String description) throws Exception;

	/**
	 * Find DataElement by comment.
	 * @param comment of elements to find
	 * @return Iterator of results
	 * @throws Exception thrown on failure to retrieve elements
	 */
	public Iterable<T> findByComment(String comment) throws Exception;

	/**
	 * Find DataElement by context.
	 * @param context of elements to find
	 * @return Iterator of results
	 * @throws Exception thrown on failure to retrieve elements
	 */
	public Iterable<T> findByContext(String context) throws Exception;

	/**
	 * Find DataElement by required.
	 * @param required value to match
	 * @return Iterator of results
	 * @throws Exception thrown on failure to retrieve elements
	 */
	public Iterable<T> findByRequired(boolean required) throws Exception;

	/**
	 * Find DataElement by secret.
	 * @param secret value to match
	 * @return Iterator of results
	 * @throws Exception thrown on failure to retrieve elements
	 */
	public Iterable<T> findBySecret(boolean secret) throws Exception;
}
