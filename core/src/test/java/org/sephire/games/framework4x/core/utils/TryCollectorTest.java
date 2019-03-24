package org.sephire.games.framework4x.core.utils;

import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.sephire.games.framework4x.core.utils.FunctionalUtils.Collectors.toTry;

public class TryCollectorTest {

	@Test
	@DisplayName("Given a stream of tries, should collect them into a try sequence")
	public void should_collect_tries(){
		var tryCollectorTry = Try.of(()-> List.range(0,5)
		  .map((i)-> Try.of(()->Long.valueOf(i)))
		  .collect(toTry()));

		assertTrue(tryCollectorTry.isSuccess());
		assertTrue(tryCollectorTry.get().isSuccess());
	}

	@Test
	@DisplayName("Given a stream of failing tries, should produce a failed try")
	public void should_produce_failing_try_when_collecting_failing_tries(){
		var tryCollectorTry = Try.of(()-> List.range(0,5)
		  .map((i)-> Try.of(()->{throw new IllegalArgumentException("asdsa");}))
		  .collect(toTry()));

		assertTrue(tryCollectorTry.isSuccess());
		assertTrue(tryCollectorTry.get().isFailure());
	}
}
