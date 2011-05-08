package com.github.gardentree.utilities;

/**
 * @author garden_tree
 * @since 2011/05/03
 */
public class Calling {
	public static Class<?> fromClass(final int depth) {
		final StackTraceElement[] traces = new Exception().getStackTrace();

		try {
			return ClassLoader.getSystemClassLoader().loadClass(traces[depth + 1].getClassName());
		}
		catch (final ClassNotFoundException ex) {
			throw new Error(ex);
		}
	}
	public static String fromMethodName(final int depth) {
		final StackTraceElement[] traces = new Exception().getStackTrace();

		return traces[depth + 1].getMethodName();
	}
}
