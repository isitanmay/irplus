/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  


package edu.ur.cgLib;

/**
 * Class to help with the cg lib classes
 * 
 * @author Nathan Sarr
 *
 */
public class CgLibHelper 
{
	
	/**
	 * Cleans up the class name removing the CGLIB or javassist and
	 * everything that follows.
	 * 
	 * @param name - class name
	 * @return the class name or null if the object is null.
	 */
	public static String cleanClassName(String name)
	{
		if (name == null )
		{
			return null;
		}
		
		if( name.indexOf("CGLIB") != -1 )
		{
		    String realClassname = name.substring(0,
		    name.indexOf("$$"));
		    return realClassname;
		}
	
		if( name.indexOf("javassist") != -1)
		{
			String realClassname = name.substring(0,
		    name.indexOf("_$$_javassist"));
			return realClassname;
		}
		else
		{
			return name;
		}
	}

}
