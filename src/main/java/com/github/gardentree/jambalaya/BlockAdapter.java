package com.github.gardentree.jambalaya;

import java.lang.reflect.Member;

import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;

class BlockAdapter extends FunctionObject {
	private final Block m_block;
	private final Violet m_violet;

    public BlockAdapter(Scriptable scope,String name,Block block,Violet violet) {
        super(name, invokeMethod(), scope);

        m_block		= block;
        m_violet	= violet;
    }
    public static Object invoke(final Context context,final Scriptable self,final Object[] arguments,final Function function) throws Exception {
    	return ((BlockAdapter)function).invoke(context,self,arguments);
    }
    public Object invoke(final Context context,final Scriptable self,final Object[] arguments) throws Exception {
    	final IRubyObject[] rubies = m_violet.deriveCrimsonFromAll(arguments);
        try {
			return m_violet.deriveAzureFrom(m_block.call(m_violet.getCrimson().getNativeRuntime().getCurrentContext(),rubies,null));
		}
		catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
			throw ex;
		}
    }
    public static Member invokeMethod() {
    	try {
			return BlockAdapter.class.getMethod("invoke",Context.class,Scriptable.class,Object[].class,Function.class);
		}
		catch (SecurityException ex) {
			throw new Error(ex);
		}
		catch (NoSuchMethodException ex) {
			throw new Error(ex);
		}
    }

}