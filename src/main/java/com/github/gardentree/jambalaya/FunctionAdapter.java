package com.github.gardentree.jambalaya;

import java.lang.reflect.Member;
import java.util.Arrays;

import org.jruby.RubyProc;
import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;

class FunctionAdapter extends FunctionObject {
	private final AzureCallback m_callback;

    public FunctionAdapter(Scriptable scope,String name,AzureCallback callback) {
        super(name, invokeMethod(), scope);

        m_callback		= callback;
    }
    public static Object invoke(final Context context,final Scriptable self,final Object[] arguments,final Function function) throws Exception {
    	return ((FunctionAdapter)function).invoke(context,self,arguments);
    }
    public Object invoke(final Context context,final Scriptable self,final Object[] arguments) throws Exception {
        try {
        	return m_callback.call(self, arguments);
		}
		catch (RuntimeException ex) {
			throw new RuntimeException(ex);
		}
    }
    public static Member invokeMethod() {
		try {
			return FunctionAdapter.class.getMethod("invoke",Context.class,Scriptable.class,Object[].class,Function.class);
		}
		catch (SecurityException ex) {
			throw new Error(ex);
		}
		catch (NoSuchMethodException ex) {
			throw new Error(ex);
		}
    }

}