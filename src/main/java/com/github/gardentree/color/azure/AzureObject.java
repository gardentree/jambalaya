package com.github.gardentree.color.azure;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Scriptable;

/**
 * @author garden_tree
 * @since 2011/04/30
 */
public class AzureObject<T extends Scriptable> implements Azure<T> {
	private final T m_object;
	private final AzureRuntime m_runtime;

	public AzureObject(final T object,final AzureRuntime runtime) {
		m_object 	= object;
		m_runtime	= runtime;
	}

	public List<String> getKeys() {
		final List<String> list = new ArrayList<String>();

		for (final Object id:m_object.getIds()) {
			list.add(id.toString());
		}

		return list;
	}

	public AzureObject<? extends Scriptable> getSelf() {
		return get(m_object);
	}
	public AzureObject<? extends Scriptable> get(final String name) {
		return get(m_object.get(name,m_runtime.getNativeScope()));
	}
	private AzureObject<? extends Scriptable> get(final Object property) {

		if (property instanceof BaseFunction) {
			final BaseFunction function = (BaseFunction)property;
//			System.out.println(String.format("%s:%d",name,function.getArity()));

			return new AzureFunction(function,this,m_runtime);
		}

		//TODO
//		throw new UnsupportedOperationException(name + ":" + property.getClass().toString());
//		System.out.println("unsupported:" + name + ":" + Arrays.asList(property.getClass().getSuperclass()));
		return null;
	}

	public T getNativeObject() {
		return m_object;
	}
	public AzureRuntime getNativeRuntime() {
		return m_runtime;
	}

	@Override
	public String toString() {
		return m_object.toString();
	}
}
