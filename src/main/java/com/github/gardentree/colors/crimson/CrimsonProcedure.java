package com.github.gardentree.colors.crimson;

import org.jruby.RubyProc;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;

public class CrimsonProcedure extends Crimson<RubyProc> {
	public static final CrimsonProcedure NULL = new CrimsonProcedure((RubyProc)null) {
		@Override
		public <E> E proceed(@SuppressWarnings("unused") final Object... arguments) {
			return null;
		}
		@Override
		public String toString() {
			return "NullCrimsonProcedure";
		}
	};

	public CrimsonProcedure(final Block block) {
		super(RubyProc.newProc(block.getBinding().getSelf().getRuntime(),block,block.type));
	}
	public CrimsonProcedure(final RubyProc procedure) {
		super(procedure);
	}
	@SuppressWarnings("unchecked")
	public <E> E proceed(final Object... arguments) {
		final IRubyObject result = getNativeObject().call(getNativeContext(),getRuntime().deriveRubyFromAll(arguments));

		return (E)JavaEmbedUtils.rubyToJava(result);
	}
}