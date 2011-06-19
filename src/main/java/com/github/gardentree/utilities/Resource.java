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
			final Class<?> target = ClassLoader.getSystemClassLoader().loadClass(traces[1].getClassName());
			final URL url = target.getResource(path);
			if (url == null) {
				throw new IllegalArgumentException(target.getResource("") + path + " is not exist");
			}

			return url;
		}
		catch (ClassNotFoundException ex) {
			throw new Error(ex);
		}
	}
}
