package org.eclipse.ice.client.widgets.jme;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.jme3.app.Application;

/**
 * This interface is for objects that can perform some rendering task. Normally,
 * this is the job of a jME {@link Application}, but in this case we have a
 * different hierarchy and only need to know that an object can queue a task for
 * a jME rendering thread.
 * 
 * @see IRenderQueue#enqueue(Callable)
 * 
 * @author Jordan
 * 
 */
public interface IRenderQueue {

	/**
	 * Queues a rendering task. If non-null, the <code>Callable</code> should be
	 * run on the <code>IRenderQueue</code>'s associated rendering thread.
	 * 
	 * @param callable
	 *            The operation to execute on the associated rendering thread.
	 * @return The result of the asynchronous operation, or null if either the
	 *         <code>Callable</code> is null or otherwise cannot be executed.
	 */
	public <T> Future<T> enqueue(Callable<T> callable);

}
