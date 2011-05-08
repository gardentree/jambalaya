package com.github.gardentree.jambalaya;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jruby.RubyArray;
import org.jruby.RubyBoolean;
import org.jruby.RubyClass;
import org.jruby.RubyHash;
import org.jruby.RubyMethod;
import org.jruby.RubyNil;
import org.jruby.RubyNumeric;
import org.jruby.RubyObject;
import org.jruby.RubyProc;
import org.jruby.RubyString;
import org.jruby.internal.runtime.methods.DynamicMethod;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.Arity;
import org.jruby.runtime.Block;
import org.jruby.runtime.BlockBody;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.callback.Callback;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.github.gardentree.color.azure.AzureRuntime;
import com.github.gardentree.color.crimson.CrimsonRuntime;

/**
 * @author garden_tree
 * @since 2011/05/03
 */
public class Violet {
	private static final Set<Class<? extends RubyObject>> BASIC_RUBY_CLASS;
	private final CrimsonRuntime	m_crimson;
	private final AzureRuntime		m_azure;

	static {
		final Set<Class<? extends RubyObject>> work = new HashSet<Class<? extends RubyObject>>();
		work.add(RubyString.class);
		work.add(RubyNumeric.class);
		work.add(RubyBoolean.class);

		BASIC_RUBY_CLASS = Collections.unmodifiableSet(work);
	}

	public Violet(final AzureRuntime azure,final CrimsonRuntime crimson) {
		m_azure		= azure;
		m_crimson	= crimson;
	}

	public CrimsonRuntime getCrimson() {
		return m_crimson;
	}
	public AzureRuntime getAzure() {
		return m_azure;
	}

	public Object[] deriveAzureFromAll(final IRubyObject[] objects) {
		final Object[] values = new Object[objects.length];
		for (int i = 0;i < values.length;i++) {
			values[i] = deriveAzureFrom(objects[i]);
		}

		return values;
	}
	public Object deriveAzureFrom(final Object object) {
		if (object instanceof IRubyObject) {
			return deriveAzureFrom((IRubyObject)object);
		}

		return object;
	}
	public Object deriveAzureFrom(final IRubyObject object) {
		if (object instanceof RubyArray) {
			final RubyArray array = (RubyArray)object;
			final Object[] values = new Object[array.size()];
			for (int i = 0;i < values.length;i++) {
				final Object value = array.get(i);

				if (value instanceof IRubyObject) {
					values[i] = deriveAzureFrom((IRubyObject)value);
				}
				else {
					values[i] = Context.javaToJS(value,m_azure.getNativeScope());
				}
			}

			return m_azure.getNativeContext().newArray(m_azure.getNativeScope(),values);//TODO Test!!!!!!!!!!!
		}
		else if (object instanceof RubyHash) {
			//TODO Test!!!!!!!!!!!
			final ScriptableObject value = (ScriptableObject)m_azure.newObject();
			final RubyHash hash = (RubyHash)object;
			for (Map.Entry entry:(Set<Entry>)hash.entrySet()) {
				value.defineProperty(entry.getKey().toString(),deriveAzureFrom(entry.getValue()),ScriptableObject.PERMANENT);
			}
			return value;
		}
		else if (object instanceof RubyProc) {//TODO Test!!!!!!!!!!!!!!!!!
			return new FunctionAdapter(m_azure.getNativeScope(),"AzureCallback",new AzureCallback() {
				@Override
				public Object call(final Scriptable self,final Object[] arguments) {
					final IRubyObject[] ruby = deriveCrimsonFromAll(arguments);
					final IRubyObject result = ((RubyProc)object).call(m_crimson.getNativeRuntime().getCurrentContext(),ruby,deriveCrimsonFrom(self),Block.NULL_BLOCK);
					return deriveAzureFrom(result);
				}
			});
		}
		else if (object instanceof RubyMethod) {//TODO Test!!!!!!!!!!!!!!!!!
			return new FunctionAdapter(m_azure.getNativeScope(),"AzureCallback",new AzureCallback() {
				@Override
				public Object call(final Scriptable self,final Object[] arguments) {
					final IRubyObject[] ruby = deriveCrimsonFromAll(arguments);
					final IRubyObject result = ((RubyMethod)object).call(m_crimson.getNativeRuntime().getCurrentContext(),ruby,Block.NULL_BLOCK);
					return deriveAzureFrom(result);
				}
			});
		}
		else if (object instanceof RubyNil) {
			return null;
		}
		else if (isBasicClass(object)) {
			return JavaEmbedUtils.rubyToJava(object);
		}
		else if (object instanceof RubyObject) {
			final RubyObject real = (RubyObject)object;

			final Set<String> exclusion = new HashSet<String>(Arrays.asList("initialize"));
			final ScriptableObject value = (ScriptableObject)m_azure.newObject();
			for (final Entry<String,DynamicMethod> entry:real.getMetaClass().getMethods().entrySet()) {
				if (exclusion.contains(entry)) {
					continue;
				}
				final DynamicMethod method = entry.getValue();
				final Function function = new FunctionAdapter(m_azure.getNativeScope(),"AzureCallback",new AzureCallback() {
					@Override
					public Object call(final Scriptable self,final Object[] arguments) {
						final IRubyObject[] ruby = deriveCrimsonFromAll(arguments);
						final IRubyObject result = method.call(m_crimson.getNativeRuntime().getCurrentContext(),object,object.getMetaClass(),"DynamicMethod",ruby);
						return deriveAzureFrom(result);
					}
				});

				value.defineProperty(entry.getKey().toString(),function,ScriptableObject.PERMANENT);
			}

			return value;
		}

		System.err.println(object.getClass());
		return null;
	}

	/**
	 * @param object
	 * @return
	 */
	private boolean isBasicClass(final IRubyObject object) {
		for (final Class<? extends RubyObject> target:BASIC_RUBY_CLASS) {
			if (target.isInstance(object)) {
				return true;
			}
		}

		return false;
	}

	private final Map<IRubyObject,Scriptable> m_repository = new HashMap<IRubyObject,Scriptable>();
	public IRubyObject[] deriveCrimsonFromAll(final Object[] objects) {
		final IRubyObject[] values = new IRubyObject[objects.length];
		for (int i = 0;i < values.length;i++) {
			values[i] = deriveCrimsonFrom(objects[i]);
		}

		return values;
	}
	public IRubyObject deriveCrimsonFrom(final Object object) {//TODO
		if (!(object instanceof Scriptable)) {
			return JavaEmbedUtils.javaToRuby(m_crimson.getNativeRuntime(),object);
		}

		return convert((Scriptable)object,(Scriptable)object);
	}
	private IRubyObject convert(final Scriptable object,final Scriptable self) {
		final IRubyObject crimson = fromAzureToCrimson(object,self);

		m_repository.put(crimson,object);

		return crimson;
	}
	private IRubyObject fromAzureToCrimson(final Scriptable object,final Scriptable self) {
		if (object instanceof NativeArray) {//TODO Test!!!!!!!!!!!!!
			final NativeArray array = (NativeArray)object;
			final IRubyObject[] children = new IRubyObject[(int)array.getLength()];
			for (int i = 0;i < array.getLength();i++) {
				children[i] = deriveCrimsonFrom(array.get(i,array));
			}
			return RubyArray.newArray(m_crimson.getNativeRuntime(),children);
		}

		final RubyClass clazz = RubyClass.newClass(m_crimson.getNativeRuntime(),m_crimson.getNativeRuntime().getObject());
		final IRubyObject container = clazz.newInstance(m_crimson.getNativeRuntime().getCurrentContext(),Block.NULL_BLOCK);
//		final IRubyObject container = m_crimson.evaluate("Object.new");

		if (object instanceof Function) {
			container.getMetaClass().defineMethod("[]",new MethodCallback("[]",object,self,this));
//			container.getMetaClass().defineMethod("_",new MethodCallback("_",object,self,this));
		}
		else {
			//TODO Test!!!!!!!!!!!!!!!!!!!!!!
			container.getMetaClass().defineMethod("[]",new Callback() {
				@Override
				public Arity getArity() {
					return Arity.ONE_REQUIRED;
				}

				@Override
				public IRubyObject execute(final IRubyObject receiver,final IRubyObject[] arguments,final Block block) {
					return container.callMethod(m_crimson.getNativeRuntime().getCurrentContext(),arguments[0].toString());
				}
			});
			container.getMetaClass().defineMethod("to_hash",new Callback() {
				@Override
				public Arity getArity() {
					return Arity.NO_ARGUMENTS;
				}

				@Override
				public IRubyObject execute(final IRubyObject receiver,final IRubyObject[] arguments,final Block block) {
					final Map<RubyString,Object> map = new HashMap<RubyString,Object>();

					final Set<String> inclusion = new HashSet<String>();
					final Scriptable scriptable = m_repository.get(container);
					for (final Object id:scriptable.getIds()) {
						inclusion.add(id.toString());
					}

					for (final Entry<String,DynamicMethod> entry:container.getMetaClass().getMethods().entrySet()) {
						if (!inclusion.contains(entry.getKey())) {
							continue;
						}

						map.put(RubyString.newString(m_crimson.getNativeRuntime(),entry.getKey()),entry.getValue().call(m_crimson.getNativeRuntime().getCurrentContext(),container,container.getMetaClass(),"hash!!!",Block.NULL_BLOCK));
					}

					return new RubyHash(m_crimson.getNativeRuntime(),map,m_crimson.getNativeRuntime().getNil());
				}
			});
		}

		if (object.getPrototype() != null) {
			structure(object.getPrototype(),self,container);
		}
		structure(object,self,container);

		return container;
	}

	private void structure(final Scriptable object,final Scriptable self,final IRubyObject container) {
		final Object[] ids;
		if (object instanceof ScriptableObject) {
			ids = ((ScriptableObject)object).getAllIds();
		}
		else {
			ids = object.getIds();
		}
		for (final Object id:ids) {
			if ("prototype".equals(id)) {
				continue;
			}

			final Object child = m_azure.inspect(object,id);
			final String name = id.toString();
//			if (methods.contains(name)) {//TODO
////				System.err.println(name);
//				continue;
//			}

			if (child instanceof Scriptable) {
				if (child instanceof Function) {//TODO Test!!!!!!!!!!!!!!!!!
					container.getMetaClass().defineMethod(name,new MethodCallback(name,(Function)child,self,this));
				}
				else {
					final IRubyObject value = convert((Scriptable)child,self);
					container.getMetaClass().defineMethod(name,new SimpleCallback(name,value));
				}

			}
			else {
				final IRubyObject value = JavaEmbedUtils.javaToRuby(m_crimson.getNativeRuntime(),child);
				container.getMetaClass().defineMethod(name,new SimpleCallback(name,value));
			}
		}
	}

	private static final class MethodCallback implements Callback {
		private final String		m_name;
		private final Scriptable	m_object;
		private final Scriptable	m_self;
		private final Violet		m_violet;

		private MethodCallback(String name, Scriptable object, Scriptable self,Violet violet) {
			m_name	= name;
			m_object = object;
			m_self = self;
			m_violet = violet;
		}

		@Override
		public Arity getArity() {
			return Arity.optional();
		}

		@Override
		public IRubyObject execute(final IRubyObject receiver,final IRubyObject[] arguments,final Block block) {
			final List<Object> javascripts = new ArrayList(Arrays.asList(m_violet.deriveAzureFromAll(arguments)));
			if (block != null&&block.getBody() != BlockBody.NULL_BODY) {
				javascripts.add(new BlockAdapter(m_violet.getAzure().getNativeScope(),"[]",block,m_violet));
			}

			try {
				//TODO Test self!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				final Object result = ((Function)m_object).call(m_violet.m_azure.getNativeContext(),m_violet.m_azure.getNativeScope(),m_self,javascripts.toArray());
				return m_violet.deriveCrimsonFrom(result);
			}
			catch (final RuntimeException ex) {
				ex.printStackTrace();
				System.err.println(m_name + ":" + ex.getMessage());
				throw new SilenceException(ex);
			}
		}
	}

	private static final class SimpleCallback implements Callback {
		private final String		m_name;
		private final IRubyObject	m_value;

		private SimpleCallback(final String name,final IRubyObject value) {
			m_name	= name;
			m_value = value;
		}
		@Override
		public Arity getArity() {
			return Arity.noArguments();
		}
		@Override
		public IRubyObject execute(final IRubyObject receiver,final IRubyObject[] arguments,final Block block) {
			return m_value;
		}
	}
}
