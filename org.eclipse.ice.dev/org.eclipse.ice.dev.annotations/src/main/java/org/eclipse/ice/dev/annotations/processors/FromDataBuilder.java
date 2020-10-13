/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daniel Bluhm - Initial implementation
 *******************************************************************************/

package org.eclipse.ice.dev.annotations.processors;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Construct used for building classes from a pool of data.
 *
 * Given a set of classes to attempt to build and a pool of data, the
 * FromDataBuilder will check for constructors on those classes that it can
 * satisfy with the data found in the pool.
 *
 * Repeat instances of the same data type in the pool will result in whichever
 * instance that happened to come first in the pool to be ignored when building
 * objects.
 *
 * @author Daniel Bluhm
 */
public class FromDataBuilder<T> {
	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(FromDataBuilder.class);

	/**
	 * Classes to potentially instantiate.
	 */
	private Set<Class<? extends T>> classes;

	/**
	 * Create FromDataBuilder.
	 * @param classes set of classes that can be instantiated.
	 */
	public FromDataBuilder(
		Set<Class<? extends T>> classes
	) {
		this.classes = classes;
	}

	/**
	 * Create instances of {@link #classes} that can be created from the given
	 * data pool.
	 * @param dataPool pool of data from which instances are created.
	 * @return created objects.
	 */
	public Set<T> create(Object... dataPool) {
		Map<Class<?>, Object> dataPoolMap = Arrays.stream(dataPool)
			.collect(Collectors.toMap(
				Object::getClass,
				o -> o
			));
		Set<T> objects = classes.stream()
			.map(cls -> create(cls, dataPoolMap))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toSet());
		return objects;
	}

	/**
	 * Create instances of {@link #classes} that can be created from the given
	 * data pool.
	 * @param dataPool pool of data from which instances are created.
	 * @return created objects.
	 */
	public Set<T> create(List<Object> dataPool) {
		return create(dataPool.toArray());
	}

	/**
	 * Create an instance of class from the data pool if possible, otherwise
	 * return empty.
	 * @param cls type to attempt to create.
	 * @param dataPoolMap pool of data from which instance will be created.
	 * @return created object wrapped in optional or empty.
	 */
	private Optional<T> create(
		Class<? extends T> cls,
		Map<Class<?>, Object> dataPoolMap
	) {
		Constructor<?>[] constructors = cls.getConstructors();
		for (Constructor<?> cons : constructors) {
			Class<?>[] parameters = cons.getParameterTypes();
			Optional<Object[]> objects = getAll(dataPoolMap, parameters);
			if (objects.isPresent()) {
				try {
					return Optional.of(
						cls.cast(cons.newInstance(objects.get()))
					);
				} catch (
					InstantiationException | IllegalAccessException |
					IllegalArgumentException | InvocationTargetException e
				) {
					logger.debug(
						"Failed to instantiate {} from data pool:",
						cls.getSimpleName(),
						e
					);
					return Optional.empty();
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * Get all values for given keys from dataPool if all keys are present,
	 * otherwise return empty.
	 * @param dataPoolMap from which data is retrieved.
	 * @param keys types to look up in data pool.
	 * @return Objects gathered wrapped in Optional or empty.
	 */
	private Optional<Object[]> getAll(
		Map<Class<?>, Object> dataPoolMap, Class<?>... keys
	) {
		List<Object> parameters = new ArrayList<>();
		for (Class<?> key : keys) {
			Object retrieved = dataPoolMap.get(key);
			if (retrieved == null) {
				return Optional.empty();
			}
			parameters.add(retrieved);
		}
		return Optional.of(parameters.toArray());
	}
}