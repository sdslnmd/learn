/**
 * 
 */
package com.taobao.top.xbox.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * 用于处理一些在class级别的问题
 * @author fangweng
 *
 */
public class ClassUtil {

	private static final Log logger = LogFactory.getLog(ClassUtil.class);
	
	/**
	 * 获取指定包名下的所有的class的名称
	 * @param packageName
	 * @return
	 */
	public static List<String> getClassListFromPackage(String packageName)
	{
		List<String> classes = new ArrayList<String>();
		
		if (packageName == null || packageName.equals(""))
			return classes;
		
		String packagePath = packageName.replace('.',File.separatorChar);
		String jarPackagePath = packageName.replace(".", "/") + "/";
		
		String[] classPaths = System.getProperty("java.class.path")
								.split(System.getProperty ("path.separator"));
			
		for(String classpath : classPaths)
		{

			if (classpath.endsWith(".jar"))
			{
				try
				{
					JarFile jarFile = new JarFile(classpath);
					
					JarEntry entry = jarFile.getJarEntry(jarPackagePath);
					
					if (entry != null)
					{
						Enumeration<JarEntry> entrys = jarFile.entries();

						while(entrys.hasMoreElements())
						{
							entry = entrys.nextElement();
							
							if (entry.getName().indexOf("$") < 0 && 
									entry.getName().endsWith(".class") &&
									entry.getName().indexOf(jarPackagePath) >= 0 
									&& !entry.getName().equals(jarPackagePath))
							{
								classes.add(packageName + "." + 
										entry.getName().substring(jarPackagePath.length(), entry.getName().length() - 6));
							}
						}

					}
				}
				catch(Exception ex)
				{
					logger.error(ex,ex);
				}
			}
			else
			{
				if (classpath.indexOf("test-classes") >= 0)
					continue;
					
				File dir = new File(classpath + File.separatorChar + packagePath);
				
				if (dir.exists() && dir.isDirectory())
				{
					File[] files = dir.listFiles();
					
					for(File f : files)
					{
						if (f.getName().indexOf("$") < 0 && f.getName().indexOf(".class") > 0)
							classes.add(packageName + "." 
									+ f.getName().substring(0, f.getName().length() - 6));
					}
					
					//认为不可能有同样包名的情况
					break;
				}
			}
		}
		
		return classes;
	}
	
	/**
	 * 根据传入的Annotation来获取当前classpath下所有的实现类
	 * @param <A>
	 * @param annotationType
	 * @return
	 */
	public static <A extends Annotation> List<Class<?>> 
		getClassListFromPackageByAnnotation(Class<A> annotationType,String packageName)
	{
		
		List<Class<?>> result = new ArrayList<Class<?>>();
		
		List<String> classes = getClassListFromPackage(packageName);
		
		for(String c : classes)
		{
			try
			{
				A as = Class.forName(c).getAnnotation(annotationType);
				
				if (as != null)
				{
					result.add(Class.forName(c));
				}
			}
			catch(Exception ex)
			{
				logger.error(ex,ex);
			}
			
		}
		
		return result;
	}
	
}
