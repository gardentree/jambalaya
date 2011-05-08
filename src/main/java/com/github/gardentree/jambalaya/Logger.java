package com.github.gardentree.jambalaya;

import java.lang.reflect.Member;
import java.util.Arrays;

import org.jruby.RubyException;
import org.jruby.RubyLocalJumpError;
import org.jruby.exceptions.JumpException;
import org.jruby.exceptions.RaiseException;
import org.jruby.runtime.Block;
import org.jruby.runtime.builtin.IRubyObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;

import com.github.gardentree.color.azure.AzureRuntime;

class Logger extends FunctionObject {
	private final AzureRuntime m_azure = AzureRuntime.newInstance();

    public Logger(String name,Scriptable scope) {
        super(name, invokeMethod(), scope);
    }
    public static Object invoke(final Context context,final Scriptable self,final Object[] arguments,final Function function) throws Exception {
    	System.out.println(Arrays.asList(arguments));

    	return null;
    }
    public static Member invokeMethod() {
    	try {
//			return FunctionAdapter.class.getMethod("invoke",Context.class,Object[].class,Function.class,boolean.class);
			return Logger.class.getMethod("invoke",Context.class,Scriptable.class,Object[].class,Function.class);
		}
		catch (Exception ex) {
			throw new Error(ex);
		}
    }

}