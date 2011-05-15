package com.github.gardentree.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author garden_tree
 * @since 2011/05/01
 */
public class Entirety {

	public static String getFromFile(final URL url) {
		try {
			return getFromFileSensitive(url);
		}
		catch (final IOException cause) {
			throw new IllegalArgumentException(cause);
		}
		catch (final URISyntaxException cause) {
			throw new IllegalArgumentException(cause);
		}
	}
	public static String getFromFileSensitive(final URL url) throws IOException, URISyntaxException {
		final BufferedReader reader = new BufferedReader(new FileReader(new File(url.toURI())));
		final StringBuilder builder = new StringBuilder();
		int value;
		while ((value = reader.read()) >= 0) {
			builder.append((char)value);
		}
		return builder.toString();
	}
}
