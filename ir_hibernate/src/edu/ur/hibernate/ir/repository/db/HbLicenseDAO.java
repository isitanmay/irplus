package edu.ur.hibernate.ir.repository.db;


import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.repository.License;
import edu.ur.ir.repository.LicenseDAO;

/**
 * Implementation of license DAO class for hibernate
 * 
 * @author Nathan Sarr
 *
 */
public class HbLicenseDAO implements LicenseDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = 5716858535609250492L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<License> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbLicenseDAO() {
		hbCrudDAO = new HbCrudDAO<License>(License.class);
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
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public License getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(License entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	/**
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(License entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("licenseCount");
		return (Long)q.uniqueResult();
	}

}
