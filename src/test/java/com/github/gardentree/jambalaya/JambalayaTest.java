package com.github.gardentree.jambalaya;

import org.junit.Test;

import com.github.gardentree.colors.crimson.CrimsonRuntime;
import com.github.gardentree.utilities.Resource;

/**
 * @author garden_tree
 * @since 2011/05/13
 */
public class JambalayaTest {
	@Test
	public void putToTop() {
		final CrimsonRuntime runtime = CrimsonRuntime.newInstance();
		runtime.evaluate(Resource.getUrl("put_to_top.rb"));
	}
}
