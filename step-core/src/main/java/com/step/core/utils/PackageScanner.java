package com.step.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("rawtypes")
public abstract class PackageScanner {
	/**
	 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
	 *
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws java.io.IOException
	 */
	public static Class[] getClassesInPackage(String packageName)
	throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		Set<String> className = new HashSet<String>();
		List<Class> classes = new ArrayList<Class>();

		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			if(resource.getProtocol().equals("jar")){
				String jarFileName;
				JarFile jf ;
				Enumeration<JarEntry> jarEntries;
				String entryName;
				
				jarFileName = URLDecoder.decode(resource.getFile(), "UTF-8");
				jarFileName = jarFileName.substring(5,jarFileName.indexOf("!"));
				jf = new JarFile(jarFileName);
				jarEntries = jf.entries();
				while(jarEntries.hasMoreElements()){
					entryName = jarEntries.nextElement().getName();

					if(entryName.contains(".class")){
						entryName = entryName.replace("/", ".");
						entryName = entryName.substring(0,entryName.lastIndexOf('.'));
						className.add(entryName);
					}
				}
			}
			else{
				dirs.add(new File(resource.getFile()));
			}
		}

		if(dirs.isEmpty()){
			for(String clazz : className){
				try{
					Class cls = Class.forName(clazz);
					classes.add(cls);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			return classes.toArray(new Class[classes.size()]);
		}

		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
}
