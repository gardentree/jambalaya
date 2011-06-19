package com.github.gardentree.colors.azure;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.github.gardentree.jambalaya.SilenceException;
import com.github.gardentree.utilities.Entirety;

/**
 * @author garden_tree
 * @since 2011/04/29
 */
public class AzureRuntime {
	private final Context		m_context;
	private final Scriptable	m_scope;
	private final File			m_workspace;

	public AzureRuntime(final Context context,final Scriptable scope,final File workspace) {
		m_context	= context;
		m_scope		= scope;
		m_workspace	= workspace;
	}

	public static AzureRuntime newInstance() {
		return newInstance("");
	}
	public static AzureRuntime newInstance(final String workspace) {
		final Context context = new ContextFactory().enterContext();
//		context.setLanguageVersion(Context.VERSION_1_7);

		return new AzureRuntime(context,context.initStandardObjects(),new File(workspace));
	}

	public Azure<?> evaluate(final CharSequence script) {
		return wrap(m_context.evaluateString(m_scope,script.toString(),"azure",0,null));
	}

	public Azure<?> wrap(final Object object) {
		if (object instanceof ScriptableObject) {
			return new AzureBasicObject((ScriptableObject)object,this);
		}
		if (object instanceof Scriptable) {
			return new AzureObject<Scriptable>((Scriptable)object,this);
		}
		if (object instanceof String) {
			return new AzurePrimitiveObject(new NativeString(object.toString(),m_scope));
		}

		return new AzurePrimitiveObject(object);
	}
	public AzureBasicObject getTopObject() {
		return new AzureBasicObject((ScriptableObject)getNativeScope(),this);
	}

	public Context getNativeContext() {
		return m_context;
	}
	public Scriptable getNativeScope() {
		return m_scope;
	}

	public Object require(final String pathname,final String specifier) {
		try {
			final StringBuilder builder = new StringBuilder();
			builder.append("(function(exports){");
			builder.append(Entirety.getFromFile(new File(pathname).toURI().toURL()));
			builder.append(String.format("return %s;",specifier));
			builder.append("})({})");

			final Azure<?> object = evaluate(builder);

			return object.getNativeObject();
		}
		catch (final Throwable cause) {
			cause.printStackTrace();

			throw new SilenceException(cause);
		}
//		finally {
//			Context.exit();
//		}
	}

	public Object[] deriveJavaFromAll(final Object[] objects) {
		final Object[] values = new Object[objects.length];
		for (int i = 0;i < values.length;i++) {
			values[i] = deriveJavaFrom(objects[i]);
		}

		return values;
	}
	public Object deriveJavaFrom(final Object object) {
		if (object instanceof NativeArray) {
			final NativeArray array = (NativeArray)object;
			final Object[] values = new Object[(int)array.getLength()];
			for (int i = 0;i < array.getLength();i++) {
				values[i] = Context.jsToJava(array.get(i,array),Object.class);
			}

			return values;
		}
		else if (object instanceof NativeObject) {
			final Map<Object,Object> hash = new LinkedHashMap<Object,Object>();
			final NativeObject real = (NativeObject)object;
			for (final Object key:real.getAllIds()) {
				hash.put(key,real.get(key.toString(),m_scope));
			}
			final Scriptable prototype = real.getPrototype();
			for (final Object key:prototype.getIds()) {
				hash.put(key,prototype.get(key.toString(),m_scope));
			}

			return hash;
		}
		else if (object instanceof NativeString) {
			return object.toString();
		}

		return Context.jsToJava(object,Object.class);
	}

	public Scriptable newObject() {
		return getNativeContext().newObject(getNativeScope());
	}


//	public Object deriveJavaFrom(final Object value) {
//		return deriveJavaFromAll(value)[0];
//	}
//	public Object[] deriveJavaFromAll(final Object... values) {
//		final List<Object> list = new ArrayList<Object>();
//		for (final Object value:values) {
//			if (value instanceof NativeJavaObject) {
//				list.add(((NativeJavaObject)value).unwrap());
//			}
//			else if (value instanceof NativeArray) {
//				final NativeArray array = (NativeArray)value;
//				final List<Object> work = new ArrayList<Object>();
//				for (int i = 0;i < array.getLength();i++) {
//					work.add(array.get(i,array));
//				}
//
//				list.add(deriveJavaFromAll(work.toArray()));
//			}
//			else if (value instanceof Undefined) {
//				list.add(null);
//			}
//			else if (value instanceof NativeObject) {
//				final Map<Object,Object> hash = new LinkedHashMap<Object,Object>();
//				final NativeObject object = (NativeObject)value;
//				for (final Object key:object.getAllIds()) {
//					hash.put(key,object.get(key.toString(),m_scope));
//				}
//				final Scriptable prototype = object.getPrototype();
//				for (final Object key:prototype.getIds()) {
//					hash.put(key,prototype.get(key.toString(),m_scope));
//				}
//
//				list.add(hash);
//			}
//			else {
////				System.out.println(value.getClass());
//				list.add(Context.jsToJava(value,Object.class));
//			}
//		}
//
//		return list.toArray();
//	}
	public Object deriveJavaScriptFrom(final Object value) {
		return deriveJavaScriptFromAll(value)[0];
	}
	public Object[] deriveJavaScriptFromAll(final Object... values) {
		final List<Object> list = new ArrayList<Object>();
		for (final Object value:values) {
			if (value == null) {
				list.add(null);
				continue;
			}

			if (value.getClass().isArray()) {
				final Object[] array = deriveJavaScriptFromAll((Object[])value);
				list.add(m_context.newArray(m_scope,array));
			}
			else if (value instanceof Map) {
				final NativeObject object = new NativeObject();

				for (Map.Entry<Object,Object> entry:((Map<Object,Object>)value).entrySet()) {
					object.defineProperty(entry.getKey().toString(),entry.getValue(),ScriptableObject.PERMANENT);
				}

				list.add(object);
			}
			else {
				list.add(Context.javaToJS(value,m_scope));
//				if (value instanceof Long) {
//					list.add(Double.valueOf((Long)value));
//				}
//				else {
//					System.err.println(value);
//					list.add(value);
//				}
			}
		}

		return list.toArray();
	}

	public Object inspect(Scriptable object,Object id) {
		if (id instanceof String) {
			return object.get((String)id,m_scope);
		}
		else {
			return object.get(((Number)id).intValue(),m_scope);
		}
	}
}
