package com.github.gardentree.color.azure;

/**
 * @author garden_tree
 * @since 2011/05/02
 */
public class AzurePrimitiveObject implements Azure<Object> {
	private final Object m_object;

	public AzurePrimitiveObject(final Object object) {
		m_object = object;
	}

	@Override
	public Object getNativeObject() {
		return m_object;
	}

	@Override
	public String toString() {
		return m_object.toString();
	}
}
