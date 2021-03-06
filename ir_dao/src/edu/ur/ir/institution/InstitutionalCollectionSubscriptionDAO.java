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

import java.util.List;

import edu.ur.dao.CrudDAO;
import edu.ur.ir.user.IrUser;

/**
 * Institutional collection subscrition data access object interface.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalCollectionSubscriptionDAO extends
CrudDAO<InstitutionalCollectionSubscription>{
	
	/**
	 * Get all subscriptions for a given user.
	 * 
	 * @param user - user to get all subscriptions for
	 * @return - set of subscriptions for a given user.
	 */
	public List<InstitutionalCollectionSubscription> getAllSubscriptionsForUser(IrUser user);
	
	/**
	 * Get the count for the number of subscribers for a given collection.  This does
	 * not count subscribers to sub collections.
	 * 
	 * @param institutionalCollection
	 * @return - count of subscribers to a given collection
	 */
	public Long getSubscriberCount(InstitutionalCollection institutionalCollection);
	
	/**
	 * Returns true if the user is subscribed to the given collection.
	 * 
	 * @param institutionalCollection - collection to check and see if the user is subscribed.
	 * 
	 * @param user - user to check for
	 * @return count - number of times user is found in the specified collection 1 = subscribed 
	 * 0 = not subscribed
	 */
	public Long isSubscriberCount(InstitutionalCollection institutionalCollection, IrUser user);
	
	/**
	 * Get a list of all unique subscriber user id's.  This allows
	 * one single list of all users who have at least one subscription
	 * in the system, the user id is not duplicated in this list even
	 * if they have more than one subscription
	 * 
	 * @return list of unique user id's
	 */
	public List<Long> getUniqueSubsciberUserIds();
	
	/**
	 * Get the subscription for the specified user and collection.  Will return null if the user
	 * does not have a subscription for the collection.
	 * 
	 * @param institutionalCollection - instituitonal collection the user should be subscribed to
	 * @param user - the user 
	 * @return the subscription if the user is subscribed to the specified collection
	 */
	public InstitutionalCollectionSubscription getSubscriptionForUser(InstitutionalCollection institutionalCollection, IrUser user);

}
