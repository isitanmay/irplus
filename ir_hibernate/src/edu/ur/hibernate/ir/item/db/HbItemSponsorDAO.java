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

package edu.ur.hibernate.ir.item.db;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.item.ItemSponsor;
import edu.ur.ir.item.ItemSponsorDAO;


/**
 * Data access for a item sponsor.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbItemSponsorDAO implements ItemSponsorDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 8406000779078048375L;

	/** hibernate helper  */
	private final HbCrudDAO<ItemSponsor> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbItemSponsorDAO() {
		hbCrudDAO = new HbCrudDAO<ItemSponsor>(ItemSponsor.class);
	}
	
	/**
	 * Set the session factory.  
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
    {
        hbCrudDAO.setSessionFactory(sessionFactory);
    }

	/**
	 * Get a count of the extent types in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("itemSponsorCount");
		return (Long)q.uniqueResult();
	}

	public ItemSponsor getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(ItemSponsor entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(ItemSponsor entity) {
		hbCrudDAO.makeTransient(entity);
	}
}
