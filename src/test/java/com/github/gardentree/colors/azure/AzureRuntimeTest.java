package com.github.gardentree.colors.azure;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.mozilla.javascript.Scriptable;

import com.github.gardentree.colors.azure.Azure;
import com.github.gardentree.colors.azure.AzureBasicObject;
import com.github.gardentree.colors.azure.AzureObject;
import com.github.gardentree.colors.azure.AzureRuntime;
import com.github.gardentree.utilities.Entirety;
import com.github.gardentree.utilities.Resource;

/**
 * @author garden_tree
 * @since 2011/05/02
 */
public class AzureRuntimeTest {
	private AzureRuntime m_runtime = AzureRuntime.newInstance();
	@Test
	public void testDeriveJavaScriptFrom() {
		final Object actual = m_runtime.deriveJavaScriptFromAll(new Object[]{1L,2L,3L});

		assertArrayEquals(new Object[]{1L,2L,3L},(Object[])actual);
	}
	@Test
	public void testDeriveJavaFromAll() {
		final Azure object = m_runtime.evaluate("[1,2,3]");
		final Object actual = m_runtime.deriveJavaFrom(((AzureObject)object).getNativeObject());

		assertArrayEquals(new Object[]{1.0,2.0,3.0},(Object[])actual);//TODO 1.0?
	}
	@Test
	public void testHash() {
		final AzureObject object = (AzureObject)m_runtime.evaluate("(function() {var hash = {one: 1,two: 2,three: 3};return hash;})()");
		final Object java = m_runtime.deriveJavaFrom(object.getNativeObject());

		final Map<Object,Object> expected = new LinkedHashMap<Object,Object>();
		expected.put("one"	,1.0);
		expected.put("two"  ,2);
		expected.put("three",3);
		assertEquals(expected,java);
	}
	@Test
	public void testIds() {
		final Map<Object,Object> object = new LinkedHashMap<Object,Object>();
		object.put("one"	,1);
		object.put("two"	,2);
		object.put("three"	,3);

		final Scriptable acutal = (Scriptable)m_runtime.deriveJavaScriptFrom(object);

		assertArrayEquals(new Object[]{"one","two","three"},acutal.getIds());
	}
	@Test
	public void testGetTopObject() {
		final AzureRuntime runtime = AzureRuntime.newInstance();

		final AzureBasicObject object = (AzureBasicObject)runtime.evaluate(Entirety.getFromFile(Resource.getUrl("global.js")));

		assertEquals(Arrays.asList("before","after"),object.getKeys());
		assertEquals(Arrays.asList("before"),runtime.getTopObject().getKeys());
	}

	@Test
	public void testWrapForString() {
		final Azure<?> actual = m_runtime.wrap("abc");

		assertEquals(NativeString.class,actual.getNativeObject().getClass());
	}
}
