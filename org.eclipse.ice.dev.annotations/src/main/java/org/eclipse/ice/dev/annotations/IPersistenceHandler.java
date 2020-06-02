package org.eclipse.ice.dev.annotations;

public interface IPersistenceHandler {
	public <T extends IDataElement> void save(T element) throws Exception;
	public <T extends IDataElement> T findByID(String id) throws Exception;
}
