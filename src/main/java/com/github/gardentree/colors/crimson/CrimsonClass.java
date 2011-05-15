package com.github.gardentree.colors.crimson;

import java.util.Map.Entry;
import java.util.Set;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.internal.runtime.methods.DynamicMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.callback.Callback;

public class CrimsonClass extends Crimson<RubyClass> {
	public CrimsonClass(final String name,final Ruby runtime) {
		super(runtime.getClass(name));
	}
	public CrimsonClass(final RubyClass type) {
		super(type);
	}

	public CrimsonObject newInstance(final Object... arguments) {
		final IRubyObject target = getNativeObject().newInstance(getNativeContext(),getRuntime().deriveRubyFromAll(arguments),Block.NULL_BLOCK);

		return new CrimsonObject(target);
	}

	public void defineMethod(final String name,final Callback callback) {
		getNativeObject().defineMethod(name,callback);
	}

	public Set<Entry<String,DynamicMethod>> getMethods() {
		return getNativeObject().getMethods().entrySet();
	}
}