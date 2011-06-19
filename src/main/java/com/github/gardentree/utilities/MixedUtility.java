package com.github.gardentree.utilities;

/**
 * @author garden_tree
 * @since 2011/05/22
 */
public class MixedUtility {
	public static String repeat(final String text,final int times) {
		final StringBuilder builder = new StringBuilder();

		for (int i = 0;i < times;i++) {
			builder.append(text);
		}

		return builder.toString();
	}
}
