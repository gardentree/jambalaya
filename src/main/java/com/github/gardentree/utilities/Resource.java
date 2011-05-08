package com.github.gardentree.utilities;

import java.net.URL;

/**
 * @author garden_tree
 * @since 2011/05/01
 */
public class Resource {
	public static URL getUrl(final String path) {
		final StackTraceElement[] traces = new Exception().getStackTrace();

		try {
			return ClassLoader.getSystemClassLoader().loadClass(traces[1].getClassName()).getResource(path);
		}
		catch (ClassNotFoundException ex) {
			throw new Error(ex);
		}
	}
}
