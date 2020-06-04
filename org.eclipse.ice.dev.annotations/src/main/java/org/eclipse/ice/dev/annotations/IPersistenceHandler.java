package org.eclipse.ice.dev.annotations;

import java.util.UUID;

public interface IPersistenceHandler {
	public <T extends IDataElement> void save(T element) throws Exception;
	public <T extends IDataElement> T findByUUID(UUID uuid) throws Exception;
	public <T extends IDataElement> Iterable<T> findById(long id) throws Exception;
	public <T extends IDataElement> Iterable<T> findByName(String name) throws Exception;
	public <T extends IDataElement> Iterable<T> findByDescription(String description) throws Exception;
	public <T extends IDataElement> Iterable<T> findByComment(String comment) throws Exception;
	public <T extends IDataElement> Iterable<T> findByContext(String context) throws Exception;
	public <T extends IDataElement> Iterable<T> findByRequired(boolean required) throws Exception;
	public <T extends IDataElement> Iterable<T> findBySecret(boolean secret) throws Exception;
	public long clear() throws Exception;
}