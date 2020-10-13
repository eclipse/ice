package org.eclipse.ice.tests.dev.annotations.processors;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.ice.dev.annotations.processors.FromDataBuilder;
import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;

class FromDataBuilderTest {
	interface Data {
		public String getData();
	}

	public static class Data1 implements Data {
		private static final String VALUE = "Data1";
		private String data = VALUE;
		@Override
		public String getData() {
			return data;
		}
	}

	public static class Data2 implements Data {
		private static final String VALUE = "Data2";
		private String data = VALUE;
		@Override
		public String getData() {
			return data;
		}
	}

	interface Thing {
		public void assertValues();
	}

	@AllArgsConstructor
	public static class Thing1 implements Thing {
		private Data1 data1;
		@Override
		public void assertValues() {
			assertEquals(Data1.VALUE, data1.getData());
		}
	}

	@AllArgsConstructor
	public static class Thing2 implements Thing {
		private Data2 data2;

		@Override
		public void assertValues() {
			assertEquals(Data2.VALUE, data2.getData());
		}
	}

	@AllArgsConstructor
	public static class Thing3 implements Thing {
		private Data1 data1;
		private Data2 data2;

		@Override
		public void assertValues() {
			assertEquals(Data1.VALUE, data1.getData());
			assertEquals(Data2.VALUE, data2.getData());
		}
	}

	@Test
	void testCreate() {
		FromDataBuilder<Thing> builder = new FromDataBuilder<>(
			Set.of(Thing1.class, Thing2.class, Thing3.class)
		);
		Set<Thing> things = builder.create(new Data1());
		assertEquals(1, things.size());
		assertTrue(things.stream().findFirst().get() instanceof Thing1);

		things = builder.create(new Data2());
		assertEquals(1, things.size());
		assertTrue(things.stream().findFirst().get() instanceof Thing2);

		things = builder.create(new Data1(), new Data2());
		assertEquals(3, things.size());
		assertEquals(1, things.stream()
			.filter(thing -> thing instanceof Thing1)
			.collect(Collectors.toSet())
			.size()
		);
		assertEquals(1, things.stream()
			.filter(thing -> thing instanceof Thing2)
			.collect(Collectors.toSet())
			.size()
		);
		assertEquals(1, things.stream()
			.filter(thing -> thing instanceof Thing3)
			.collect(Collectors.toSet())
			.size()
		);
		things.stream().forEach(Thing::assertValues);
	}
}