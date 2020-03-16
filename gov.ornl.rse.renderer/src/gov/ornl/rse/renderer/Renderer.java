/**
 * 
 */
package gov.ornl.rse.renderer;

import java.util.function.BiConsumer;
import javax.inject.Inject;

/**
 * @author Jay Jay Billings
 *
 */
public class Renderer<T, V> {

	@Inject
	private T viewer;
	
	@Inject
	private DataElement<V> dataElement;
	
	private BiConsumer<T, DataElement<V>> drawMethod;

	public void setViewer(T view) {
		viewer = view;
	}

	public void setDataElement(DataElement<V> data) {
		dataElement = data;
	}

	public void setDrawMethod(BiConsumer<T,DataElement<V>> drawFunction) {
		drawMethod = drawFunction;
	}

	public void render() {
		drawMethod.accept(viewer,dataElement);
	}
}
