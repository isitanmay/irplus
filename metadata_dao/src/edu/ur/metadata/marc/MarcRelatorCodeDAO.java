/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.metadata.marc;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.ListAllDAO;
import edu.ur.dao.UniqueNameDAO;

/**
 * Deals with marc relator code data access.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcRelatorCodeDAO extends CountableDAO, 
CrudDAO<MarcRelatorCode>, UniqueNameDAO<MarcRelatorCode>, ListAllDAO {

	/**
	 * Get the marc relator code by relator code value.
	 * 
	 * @param relatorCode - value of the relator code
	 * @return - the the marc relator code if found otherwise null
	 */
	public MarcRelatorCode getByRelatorCode(String relatorCode);
}
