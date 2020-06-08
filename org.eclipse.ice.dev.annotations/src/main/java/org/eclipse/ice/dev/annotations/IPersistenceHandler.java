package org.eclipse.ice.dev.annotations;

import java.util.UUID;

public interface IPersistenceHandler<T> {
	/**
	 * Save the DataElement.
	 * @param <T> Object extending IDataElement
	 * @param element
	 * @throws Exception
	 */
	public void save(T element) throws Exception;

	/**
	 * Clear all DataElements from the collection.
	 * @return
	 * @throws Exception
	 */
	public long clear() throws Exception;

	/**
	 * Retrieve all DataElements from the collection.
	 * @param <T>
	 * @return an iterable of the retrieved elements.
	 * @throws Exception
	 */
	public Iterable<T> find() throws Exception;

	/**
	 * Find DataElement by UUID.
	 * @param UUID
	 * @return found element or null
	 */
	public T findByUUID(UUID uuid) throws Exception;

	/**
	 * Find DataElement by id.
	 * @param id
	 * @return Iterator of results
	 */
	public Iterable<T> findById(long id) throws Exception;

	/**
	 * Find DataElement by name.
	 * @param name
	 * @return Iterator of results
	 */
	public Iterable<T> findByName(String name) throws Exception;

	/**
	 * Find DataElement by description.
	 * @param description
	 * @return Iterator of results
	 */
	public Iterable<T> findByDescription(String description) throws Exception;

	/**
	 * Find DataElement by comment.
	 * @param comment
	 * @return Iterator of results
	 */
	public Iterable<T> findByComment(String comment) throws Exception;

	/**
	 * Find DataElement by context.
	 * @param context
	 * @return Iterator of results
	 */
	public Iterable<T> findByContext(String context) throws Exception;

	/**
	 * Find DataElement by required.
	 * @param required
	 * @return Iterator of results
	 */
	public Iterable<T> findByRequired(boolean required) throws Exception;

	/**
	 * Find DataElement by secret.
	 * @param secret
	 * @return Iterator of results
	 */
	public Iterable<T> findBySecret(boolean secret) throws Exception;
}