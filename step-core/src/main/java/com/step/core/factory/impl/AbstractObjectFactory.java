package com.step.core.factory.impl;

import com.step.core.annotations.Initialize;
import com.step.core.factory.ObjectFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: amishra
 * Date: 5/21/13
 * Time: 10:44 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractObjectFactory implements ObjectFactory {
    private Map<Class<?>, Object> objectByClassMap = new HashMap<Class<?>, Object>();
    private Map<String, Object> objectByNameMap = new HashMap<String, Object>();
    private Map<Class<?>, PrototypeClassInfo> prototypeMap = new HashMap<Class<?>, PrototypeClassInfo>();
    private Map<String, PrototypeClassInfo> prototypeMapByName = new HashMap<String, PrototypeClassInfo>();

    public <T> T fetch(Class<T> objClass){
        if(prototypeMap.get(objClass) != null){
            return (T)initializeClass(objClass);
        }
        return (T)objectByClassMap.get(objClass);
    }

    public Object fetch(String objName){
        if(prototypeMapByName.get(objName) != null){
            return initializeClass(prototypeMapByName.get(objName).getProtoTypeClass());
        }
        return objectByNameMap.get(objName);
    }

    protected void add(Object obj){
        Class cls = obj.getClass();
        Class[] interfaces = cls.getInterfaces();

        if(interfaces.length == 0){
            objectByClassMap.put(cls, obj);
            return;
        }

        for(Class interfaceCls : interfaces){
            objectByClassMap.put(interfaceCls, obj);
        }

        objectByClassMap.put(cls, obj);
        addByName(obj);
    }

    protected void addByName(Object obj){
        String objName = fetchNameOfClass(obj.getClass());
        objectByNameMap.put(objName, obj);
    }

    protected Object initializeClass(Class c){
        return initializeClass(c, null);
    }

    protected void prototypeClass(Class c){
        prototypeClass(c, null);
    }

    protected void prototypeClass(Class c, ConstructorArgs args){
        Class[] interfaces = c.getInterfaces();
        String objName = fetchNameOfClass(c);

        for(Class inter : interfaces){
            prototypeMap.put(inter, new PrototypeClassInfo(c, args));
        }

        prototypeMap.put(c, new PrototypeClassInfo(c, args));
        prototypeMapByName.put(objName, new PrototypeClassInfo(c, args));
    }

    protected Object initializeClass(Class c, ConstructorArgs args) {
        Object obj = objectByClassMap.get(c);
        Object createdObject = null;

        try{
			/*
			 * For prototype class, every time create new instance of class
			 */
            PrototypeClassInfo protoInfo = prototypeMap.get(c);
            if(protoInfo != null){
                c = protoInfo.getProtoTypeClass();
                args = protoInfo.getArgs();
            }
            //If interface then should be present in repository
            if(c.isInterface()){
                if(obj == null){
                    throw new IllegalStateException(c+" should be initialized first.");
                }
                return obj;
            }else if(obj != null){
                //If already present in repository then return
                return obj;
            }

			/*
			 * If ConstructorArgs is provided then initialising that particular Constructor
			 */
            if(args != null){
                Constructor ctr = c.getConstructor(args.getArgs());

                if(ctr == null){
                    throw new IllegalStateException(c+" could not be initialized, no constructor found with args "+args.getArgs());
                }

                createdObject = initConstructor(ctr);
                initFields(createdObject, c.getDeclaredFields());
                initSuperFields(createdObject);

                return createdObject;
            }

			/*
			 * ConstructorArgs is not provided then this means that the class contains only default Constructor
			 * and if so the initialize using default Constructor. For multiple Constructors try to find
			 * default Constructor and initialize otherwise throw Exception if no default Constructor found.
			 */
            Constructor[] construtors = c.getConstructors();

            if(construtors.length == 1){
                Class[] params = construtors[0].getParameterTypes();
                if(params.length == 0){
                    createdObject = construtors[0].newInstance();
                    initFields(createdObject, c.getDeclaredFields());
                    initSuperFields(createdObject);
                    return createdObject;
                }else{
                    createdObject = initConstructor(construtors[0]);
                    initFields(createdObject, c.getDeclaredFields());
                    initSuperFields(createdObject);
                    return createdObject;
                }
            }

            Constructor defaultConstructor = getDefaultConstructor(construtors);

            if(defaultConstructor == null){
                throw new IllegalStateException(c+"could not be initialized, no default constructor found.");
            }

            createdObject = initConstructor(defaultConstructor);
            initFields(createdObject, c.getDeclaredFields());
            initSuperFields(createdObject);
        }catch(Exception e){
            throw new IllegalStateException("Problem with "+c+", nested exception: "+e.getMessage());
        }

        return createdObject;
    }

    private Constructor getDefaultConstructor(Constructor[] construtors){
        for(Constructor constructor : construtors){
            if(constructor.getParameterTypes().length == 0){
                return constructor;
            }
        }

        return null;
    }

    private Object initConstructor(Constructor constructor) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object[] typeInstances = new Object[constructor.getParameterTypes().length];

        for(int j=0 ; j<typeInstances.length ; j++){
            Class pc = constructor.getParameterTypes()[j];
            typeInstances[j] = initializeClass(pc);
        }

        return constructor.newInstance(typeInstances);
    }

    private void initFields(Object object, Field... fields) throws IllegalArgumentException, IllegalAccessException {
        for(Field f : fields){
            Initialize annotation = f.getAnnotation(Initialize.class);
            if(annotation != null){
                Class cls = f.getType();
                f.setAccessible(true);
                f.set(object, initializeClass(cls));
            }
        }
    }

    private void initSuperFields(Object object) throws IllegalArgumentException, SecurityException, IllegalAccessException {
        Class superClass = object.getClass().getSuperclass();
        if(superClass != null){
            initFields(object, superClass.getDeclaredFields());
        }
    }

    private static class PrototypeClassInfo{
        private Class protoTypeClass;
        private ConstructorArgs args;

        public PrototypeClassInfo(Class protoTypeClass, ConstructorArgs args){
            this.protoTypeClass = protoTypeClass;
            this.args = args;
        }

        public Class getProtoTypeClass() {
            return protoTypeClass;
        }

        public ConstructorArgs getArgs() {
            return args;
        }
    }

    private String fetchNameOfClass(Class cls){
        String objName = cls.getName();
        objName = objName.substring(objName.lastIndexOf(".")+1, objName.length());
        return objName;
    }
}
