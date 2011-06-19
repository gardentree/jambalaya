package com.github.gardentree.jambalaya;

/**
 * @author garden_tree
 * @since 2011/05/05
 */
public class SilenceException extends RuntimeException {
	public SilenceException(Throwable cause) {
		super(cause);
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return new StackTraceElement[0];
	}
}
