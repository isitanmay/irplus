/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.ir.user;

import java.io.File;
import java.io.Serializable;

/**
 * @author Nathan Sarr
 *
 */
public interface ReIndexUserGroupsService extends Serializable{
	
	/**
	 * Re-Index the user groups in the institutional repository
	 * 
	 * @param batchSize - number of users to index at a time
	 * @param userGroupIndexFolder - location of the user group index
	 * 
	 * @return total number of user groups processed.
	 */
	public int reIndexUserGroups(int batchSize, File userGroupIndexFolder);


}
