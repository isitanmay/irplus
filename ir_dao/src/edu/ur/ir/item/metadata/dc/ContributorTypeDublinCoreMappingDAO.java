/**  
   Copyright 2008-2010 University of Rochester

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
package edu.ur.ir.item.metadata.dc;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.ListAllDAO;

/**
 * Interface for creating a mapping between a contributor type and a dublin core mapping.
 * 
 * @author Nathan Sarr
 *
 */
public interface ContributorTypeDublinCoreMappingDAO extends CountableDAO, CrudDAO<ContributorTypeDublinCoreMapping>, ListAllDAO{
	
	/**
	 * Get the mapping by contributor type id
	 * 
	 * @param contributorTypeId
	 * 
	 * @return the mapping if found otherwise null
	 */
	public ContributorTypeDublinCoreMapping get(Long contributorTypeId);

	
	/**
	 * Get the mapping by contributor type id and dublin core element id.
	 * 
	 * @param contributorTypeId
	 * @param dublinCoreElementId
	 * 
	 * @return the mapping if found otherwise null
	 */
	public ContributorTypeDublinCoreMapping get(Long contributorTypeId, Long dublinCoreElementId);

}
