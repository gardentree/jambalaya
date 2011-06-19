package com.github.gardentree.colors.azure.function;

import java.io.File;
import java.lang.reflect.Member;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;

import com.github.gardentree.colors.azure.AzureRuntime;

/**
 * @author garden_tree
 * @since 2011/05/22
 */
public class Require extends FunctionObject {
	public Require(final String name,final Scriptable scope) {
		super(name,getInvokeMethod(),scope);
	}

	public static void setup(final AzureRuntime azure,final String workspace) {
    	azure.getNativeScope().put("require",azure.getNativeScope(),new Require(workspace,azure.getNativeScope()));
	}

	public static Object invoke(final Context context,final Scriptable self,final Object[] arguments,final Function function) throws Exception {
		final String current = ((Require)function).getFunctionName();
    	final String pathname = (String)arguments[0];

    	final File source = new File(current,pathname + ".js");
		final String workspace = source.getParent();
		final AzureRuntime azure = AzureRuntime.newInstance(workspace);
    	setup(azure,workspace);

    	return azure.require(source.getCanonicalPath(),"exports");
    }
    public static Member getInvokeMethod() {
    	try {
//			return FunctionAdapter.class.getMethod("invoke",Context.class,Object[].class,Function.class,boolean.class);
			return Require.class.getMethod("invoke",Context.class,Scriptable.class,Object[].class,Function.class);
		}
		catch (Exception ex) {
			throw new Error(ex);
		}
    }
}
