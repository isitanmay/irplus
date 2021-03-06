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

package edu.ur.dspace.export;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Simple class that can export files for collections.
 * 
 * @author Nathan Sarr
 *
 */
public class DSpaceCollectionExporter {
	
	public static void main(String[] args) throws IOException
	{
		// zip file name to create
		String zipFileName = args[0];
		
		// path to place the created xml file
		String filePath = args[1];
		
		/** get the application context */
		ApplicationContext ctx  = new ClassPathXmlApplicationContext(
		"applicationContext.xml");

		
		CollectionExporter collectionExporter = (CollectionExporter)ctx.getBean("collectionExporter");
		collectionExporter.exportCollections(filePath + zipFileName, filePath);
	}

}
