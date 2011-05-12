package com.github.gardentree.colors.crimson;

import org.jruby.runtime.builtin.IRubyObject;

public class CrimsonObject extends Crimson<IRubyObject> {
	public CrimsonObject(final IRubyObject target) {
		super(target);
	}

	public CrimsonClass getMetaClass() {
		return new CrimsonClass(getNativeObject().getMetaClass());
	}
}