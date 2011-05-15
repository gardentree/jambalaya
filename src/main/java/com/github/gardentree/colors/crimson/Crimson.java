package com.github.gardentree.colors.crimson;

import org.jruby.Ruby;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

public class Crimson<T extends IRubyObject> {
	private final T m_object;

	public Crimson(final T object) {
		m_object = object;
	}

	public CrimsonRuntime getRuntime() {
		return new CrimsonRuntime(m_object.getRuntime());
	}

	public Crimson<?> callMethod(final String name,final IRubyObject... arguments) {
		return new Crimson(m_object.callMethod(getNativeContext(),name,arguments));
	}
//	public <E> E callMethod(final String name,final Object... arguments) {
//		final IRubyObject result = m_object.callMethod(getNativeContext(),name,getRuntime().deriveRubyFromAll(arguments));
//
//		return (E)JavaEmbedUtils.rubyToJava(result);
//	}

	///////////////////////////////////////////////////////////////////////////
	public T getNativeObject() {
		return m_object;
	}
	protected Ruby getNativeRuntime() {
		return m_object.getRuntime();
	}
	protected ThreadContext getNativeContext() {
		return getNativeRuntime().getCurrentContext();
	}
}
