package com.github.gardentree.color.crimson;

import org.jruby.runtime.Arity;
import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.callback.Callback;

public abstract class CrimsonCallback implements Callback {
	private final Arity m_arity;

	public CrimsonCallback(final Arity arity) {
		m_arity = arity;
	}

	@Override
	public final Arity getArity() {
		return m_arity;
	}

	@Override
	public final IRubyObject execute(final IRubyObject receiver,final IRubyObject[] arguments,final Block block) {
		final Object[] argument = CrimsonRuntime.deriveJavaFromAll(arguments);

		final Object result = execute(argument,block.getBinding() != null ? new CrimsonProcedure(block):null);

		return new CrimsonRuntime(receiver.getRuntime()).deriveRubyFromAll(result)[0];
	}

	protected abstract Object execute(final Object[] arguments,final CrimsonProcedure procedure);
}