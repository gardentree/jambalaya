package com.github.gardentree.color.crimson;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jruby.Ruby;
import org.jruby.RubyArray;
import org.jruby.RubyClass;
import org.jruby.RubyHash;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

public class CrimsonRuntime {
	private final Ruby m_runtime;

	public CrimsonRuntime(final Ruby runtime) {
		m_runtime = runtime;
	}
	public static CrimsonRuntime newInstance() {
		return new CrimsonRuntime(Ruby.newInstance());
	}
	public CrimsonObject getTopObject() {
		return new CrimsonObject(m_runtime.getTopSelf());
	}
	public IRubyObject evaluate(final String script) {
		return m_runtime.evalScriptlet(script);
	}

	public IRubyObject deriveRubyFrom(final Object value) {
		return deriveRubyFromAll(value)[0];
	}
	public IRubyObject[] deriveRubyFromAll(final Object... values) {
		final IRubyObject[] objects = new IRubyObject[values.length];
		for (int i = 0;i < objects.length;i++) {
			if (values[i] == null) {
				objects[i] = m_runtime.getNil();
				continue;
			}

			if (values[i].getClass().isArray()) {
				final IRubyObject[] array = deriveRubyFromAll((Object[])values[i]);
				objects[i] = RubyArray.newArray(m_runtime,array);
			}
			else {
				objects[i] = JavaEmbedUtils.javaToRuby(m_runtime,values[i]);
			}
		}
		return objects;
	}
	public RubyArray deriveRubyArrayFrom(final List<?> values) {
		final IRubyObject[] list = deriveRubyFromAll(values.toArray());

		return RubyArray.newArray(m_runtime,list);
	}
	public static Object deriveJavaFrom(final IRubyObject value) {
		return deriveJavaFromAll(value)[0];
	}
	@SuppressWarnings({"unchecked","rawtypes"})
	public static <T> T[] deriveJavaFromAll(final IRubyObject... values) {
		final Object[] objects = new Object[values.length];
		for (int i = 0;i < objects.length;i++) {
			final IRubyObject value = values[i];
			if (value == null) {
				objects[i] = null;
				continue;
			}

			if (value instanceof RubyArray) {
				final RubyArray array = (RubyArray)value;
				objects[i] = deriveJavaFromAll(array.toJavaArray());
			}
			else if (value instanceof RubyHash) {
				final Map hash = new LinkedHashMap();
				for (final Map.Entry entry:(Set<Entry>)((RubyHash)value).entrySet()) {
					if (entry.getValue() instanceof IRubyObject) {
						hash.put(entry.getKey(),deriveJavaFrom((IRubyObject)entry.getValue()));
					}
					else {
						hash.put(entry.getKey(),entry.getValue());
					}
					hash.put(entry.getKey(),entry.getValue());
				}

				objects[i] = hash;
			}
			else {
				objects[i] = JavaEmbedUtils.rubyToJava(value);
			}
		}
		return (T[])objects;
	}
	public static <T> List<T> deriveJavaListyFrom(final RubyArray array) {
		return Arrays.<T>asList(CrimsonRuntime.<T>deriveJavaFromAll(array.toJavaArray()));
	}

	public Ruby getNativeRuntime() {
		return m_runtime;
	}
}
