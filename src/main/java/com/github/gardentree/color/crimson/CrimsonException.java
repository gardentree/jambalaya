package com.github.gardentree.color.crimson;

public class CrimsonException extends RuntimeException {
	public CrimsonException(final String message) {
		super(message);
	}
	public CrimsonException(final Throwable cause) {
		super(cause);
	}
	public CrimsonException(final String message,final Throwable cause) {
		super(message,cause);
	}
}
