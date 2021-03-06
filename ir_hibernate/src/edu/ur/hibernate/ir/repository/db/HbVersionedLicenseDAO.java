package edu.ur.hibernate.ir.repository.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.repository.VersionedLicense;
import edu.ur.ir.repository.VersionedLicenseDAO;

/**
 * Hibernate Implementation of the versioned license.
 * 
 * @author Nathan Sarr
 *
 */
public class HbVersionedLicenseDAO implements VersionedLicenseDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -6647976311732031528L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<VersionedLicense> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbVersionedLicenseDAO() {
		hbCrudDAO = new HbCrudDAO<VersionedLicense>(VersionedLicense.class);
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
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public VersionedLicense getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(VersionedLicense entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(VersionedLicense entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("versionedLicenseCount");
		return (Long)q.uniqueResult();
	}

	
	/**
	 * Returns a versioned license with the given unique name
	 * @see edu.ur.dao.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public VersionedLicense findByUniqueName(String name) {
		return (VersionedLicense) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getVersionedLicenseByName", name));
	}

}
