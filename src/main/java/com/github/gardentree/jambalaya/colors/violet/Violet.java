package com.github.gardentree.jambalaya.colors.violet;

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
import org.jruby.runtime.builtin.Variable;
import org.jruby.runtime.callback.Callback;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaPackage;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.github.gardentree.colors.azure.AzureRuntime;
import com.github.gardentree.colors.azure.NativeString;
import com.github.gardentree.colors.crimson.CrimsonRuntime;
import com.github.gardentree.jambalaya.SilenceException;

/**
 * @author garden_tree
 * @since 2011/05/03
 */
public class Violet {
	private static final Set<Class<? extends RubyObject>> BASIC_RUBY_CLASS;
	private final CrimsonRuntime	m_crimson;
	private final AzureRuntime		m_azure;

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
			if (object instanceof RubyString) {
				return new NativeString(object.asJavaString(),m_azure.getNativeScope());
			}
			return Context.javaToJS(JavaEmbedUtils.rubyToJava(object),m_azure.getNativeScope());
		}
		else if (object instanceof RubyObject) {
//			if ("Escher".equals(object.getMetaClass().toString())) {
//				final RubyObject real = (RubyObject)object;
//				System.out.println(real.getMetaClass().getVariableNameList());
//			}
			{
				final ScriptableObject value = (ScriptableObject)m_azure.newObject();
				final RubyObject real = (RubyObject)object;
				for (final Variable<IRubyObject> variable:real.getInstanceVariableList()) {
					value.defineProperty(variable.getName().substring(1),deriveAzureFrom(variable.getValue()),ScriptableObject.PERMANENT);
				}

				for (final Entry<String,DynamicMethod> entry:real.getMetaClass().getMethods().entrySet()) {
//					if (exclusion.contains(entry)) {
//						continue;
//					}
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

//	private final Map<IRubyObject,Scriptable> m_repository = new HashMap<IRubyObject,Scriptable>();//TODO
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
		if (object instanceof NativeString) {
			return RubyString.newString(m_crimson.getNativeRuntime(),object.toString());
		}

		return convert((Scriptable)object,(Scriptable)object);
	}
	private IRubyObject convert(final Scriptable object,final Scriptable self) {
		final IRubyObject crimson = fromAzureToCrimson(object,self);
//		m_repository.put(crimson,object);

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

		final Escher escher = Escher.newInstance(m_crimson,object);
		///////////////////////////////
		{
			escher.defineSpecialMethod("[]=",new Callback() {
				@Override
				public Arity getArity() {
					return Arity.TWO_REQUIRED;
				}
				@Override
				public IRubyObject execute(final IRubyObject receiver,final IRubyObject[] arguments,final Block block) {
					final String name = arguments[0].asJavaString();
					final Object value = deriveAzureFrom(arguments[1]);

					ScriptableObject.putProperty(object,name,value);
					define(escher,name,value,self);

					return null;
				}
			});
		}
		///////////////////////////////

		if (object instanceof Function) {
			escher.defineSpecialMethod("[]",new MethodCallback("[]",object,self,this));
//			container.getMetaClass().defineMethod("_",new MethodCallback("_",object,self,this));
		}
		else {
//			//TODO Test!!!!!!!!!!!!!!!!!!!!!!
//			container.getMetaClass().defineMethod("[]",new Callback() {
//				@Override
//				public Arity getArity() {
//					return Arity.ONE_REQUIRED;
//				}
//
//				@Override
//				public IRubyObject execute(final IRubyObject receiver,final IRubyObject[] arguments,final Block block) {
//					return container.callMethod(arguments[0].toString()).getNativeObject();
//				}
//			});
			escher.defineSpecialMethod("to_hash",new Callback() {
				@Override
				public Arity getArity() {
					return Arity.NO_ARGUMENTS;
				}

				@Override
				public IRubyObject execute(final IRubyObject receiver,final IRubyObject[] arguments,final Block block) {
					final Map<RubyString,IRubyObject> map = new HashMap<RubyString,IRubyObject>();

					final Set<String> inclusion = new HashSet<String>();
//					final Scriptable scriptable = m_repository.get(escher.getNativeObject());
					for (final Object id:escher.getSource().getIds()) {
						inclusion.add(id.toString());
					}

//					for (final Entry<String,DynamicMethod> entry:escher.getKeys()) {
//					if (!inclusion.contains(entry.getKey())) {
//						continue;
//					}
//
//					map.put(RubyString.newString(m_crimson.getNativeRuntime(),entry.getKey()),entry.getValue().call(m_crimson.getNativeRuntime().getCurrentContext(),container.getNativeObject(),container.getMetaClass().getNativeObject(),"hash!!!",Block.NULL_BLOCK));
//				}
					for (final Entry<String,IRubyObject> entry:escher.getFields().entrySet()) {
						map.put(RubyString.newString(m_crimson.getNativeRuntime(),entry.getKey()),entry.getValue());
					}

					return new RubyHash(m_crimson.getNativeRuntime(),map,m_crimson.getNativeRuntime().getNil());
				}
			});
		}

		if (object.getPrototype() != null) {
			structure(object.getPrototype(),self,escher);
		}
		structure(object,self,escher);

		return escher.getNativeObject();
	}

	private void structure(final Scriptable object,final Scriptable self,final Escher container) {
//		if (object instanceof NativeString) {
//			System.out.println("NativeString:"+ object);
//		}

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
			if (child instanceof NativeJavaPackage) {
				continue;
			}

			final String name = id.toString();
//			if (methods.contains(name)) {//TODO
////				System.err.println(name);
//				continue;
//			}

			define(container,name,child,self);
		}
	}

	private void define(final Escher escher,final String name,final Object value,final Scriptable self) {
		if (value instanceof Scriptable) {
			if (value instanceof Function) {//TODO Test!!!!!!!!!!!!!!!!!
				escher.defineMethod(name,new MethodCallback(name,(Function)value,self,this));
			}
//			else if (value instanceof NativeString) {
//				escher.defineField(name,JavaEmbedUtils.javaToRuby(m_crimson.getNativeRuntime(),value.toString()));
//			}
			else {
				escher.defineField(name,convert((Scriptable)value,self));
			}
		}
		else {
			escher.defineField(name,JavaEmbedUtils.javaToRuby(m_crimson.getNativeRuntime(),value));
		}
	}

	///////////////////////////////////////////////////////////////////////////
	static {
		final Set<Class<? extends RubyObject>> work = new HashSet<Class<? extends RubyObject>>();
		work.add(RubyString.class);
		work.add(RubyNumeric.class);
		work.add(RubyBoolean.class);

		BASIC_RUBY_CLASS = Collections.unmodifiableSet(work);
	}

	///////////////////////////////////////////////////////////////////////////
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
