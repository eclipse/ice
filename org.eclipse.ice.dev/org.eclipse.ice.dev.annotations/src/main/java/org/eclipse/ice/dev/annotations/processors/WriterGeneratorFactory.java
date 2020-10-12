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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for WriterGenerators. Create method parameters represent dependencies
 * of a set of generators.
 * @author Daniel Bluhm
 */
public class WriterGeneratorFactory {
	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(WriterGeneratorFactory.class);

	/**
	 * Generators to potentially create.
	 */
	private Set<Class<? extends WriterGenerator>> generators;

	/**
	 * Create WriterGeneratorFactory.
	 * @param generators set of generators that can be created.
	 */
	public WriterGeneratorFactory(
		Set<Class<? extends WriterGenerator>> generators
	) {
		this.generators = generators;
	}

	/**
	 * Create all writer generators that can be created from the given data
	 * pool.
	 * @param dataPool pool of data from which writer generators are created.
	 * @return created writer generators.
	 */
	public Set<WriterGenerator> create(Map<Class<?>, Object> dataPool) {
		return generators.stream()
			.map(cls -> create(cls, dataPool))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toSet());
	}

	/**
	 * Get a writer generator instance of given type from data pool if possible,
	 * null otherwise.
	 * @param cls type of writer generator to attempt creating.
	 * @param dataPool pool of data from which writer generator will be created.
	 * @return created writer generator or null.
	 */
	private Optional<WriterGenerator> create(
		Class<? extends WriterGenerator> cls,
		Map<Class<?>, Object> dataPool
	) {
		Constructor<?>[] constructors = cls.getConstructors();
		for (Constructor<?> cons : constructors) {
			Class<?>[] parameters = cons.getParameterTypes();
			Optional<Object[]> objects = getAll(dataPool, parameters);
			if (objects.isPresent()) {
				try {
					return Optional.of(
						(WriterGenerator) cons.newInstance(objects.get())
					);
				} catch (
					InstantiationException | IllegalAccessException |
					IllegalArgumentException | InvocationTargetException e
				) {
					logger.debug(
						"Failed to instantiate WriterGenerator from data pool:",
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
	 * @param dataPool from which data is retrieved.
	 * @param keys types to look up in data pool.
	 * @return Objects gathered wrapped in Optional or empty.
	 */
	private Optional<Object[]> getAll(
		Map<Class<?>, Object> dataPool, Class<?>... keys
	) {
		List<Object> parameters = new ArrayList<>();
		for (Class<?> key : keys) {
			Object retrieved = dataPool.get(key);
			if (retrieved == null) {
				return Optional.empty();
			}
			parameters.add(retrieved);
		}
		return Optional.of(parameters.toArray());
	}
}