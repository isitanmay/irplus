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

package edu.ur.ir.institution;

import java.io.Serializable;
import java.util.List;

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.repository.RepositoryLicenseNotAcceptedException;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.user.IrUser;

/**
 * Service methods for reviewable item
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ReviewableItemService extends Serializable{

	/**
	 * Get all items pending for review for specified user
	 * 
	 * @param user User having review permission
	 * @return List of items to review
	 */
	public List<ReviewableItem> getPendingReviewableItems(IrUser user) ;
	
	/**
	 * Get the reviewable item.
	 * 
	 * @param id Id of the reviewable item
	 * @param lock 
	 * 
	 * @return Reviewable item
	 */
	public ReviewableItem getReviewableItem(Long id, boolean lock) ;

	/**
	 * Save reviewable Item
	 * 
	 * @param reviewableItem
	 */
	public void saveReviewableItem(ReviewableItem reviewableItem);
	
	/**
	 * Send publication acceptance email
	 * 
	 * @param item Item reviewed
	 */
	public void sendItemAcceptanceEmail(ReviewableItem item) ;
	
	/**
	 * Send publication rejection email
	 * 
	 * @param item Item reviewed
	 */
	public void sendItemRejectionEmail(ReviewableItem item);

	/**
	 * Accept item and publish to collection
	 * 
	 * @param reviewableItem
	 * @param reviewer
	 * @throws CollectionDoesNotAcceptItemsException 
	 */
	public InstitutionalItem acceptItem(ReviewableItem reviewableItem, IrUser reviewer) throws CollectionDoesNotAcceptItemsException;

	/**
	 * Get review history for item
	 * 
	 * @param item  Item to get review history 
	 * @return list of history for item
	 */
	public List<ReviewableItem> getReviewHistoryForItem(GenericItem item);
	
	/**
	 * Delete review history for item
	 * 
	 * @param item Item to get review history 
	 * 
	 */
	public void deleteReviewHistoryForItem(GenericItem item);
	
	/**
	 * Add a reviewable item to the collection.
	 * 
	 * @param user - user who is adding the item
	 * @param item - generic item to add
	 * @param institutionalCollection - institutional collection to add the item to
	 * 
	 * @return created reviewable item
	 * 
	 * @throws PermissionNotGrantedException - the user does not have permission to add the item to the collection
	 * @throws RepositoryLicenseNotAcceptedException - the user has not accepted the repostiory license
	 * @throws CollectionDoesNotAcceptItemsException 
	 */
	public ReviewableItem addReviewableItemToCollection(IrUser user, GenericItem item, InstitutionalCollection institutionalCollection)  throws PermissionNotGrantedException, 
	RepositoryLicenseNotAcceptedException, 
	CollectionDoesNotAcceptItemsException;
}
