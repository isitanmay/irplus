package edu.ur.ir.handle;

import edu.ur.persistent.BasePersistent;

/**
 * This class represents the naming authority including the local name
 * 
 * @author Nathan Sarr
 *
 */
public class NameAuthority extends BasePersistent{

	/** eclipse generated id  */
	private static final long serialVersionUID = 5099575658284711364L;
	
	/** Name representing the naming authority */
	private String namingAuthority;
	
	/** local handle name */
	private String localName;
	
	/** url for the authority  */
	private String authorityBaseUrl;


	/**
	 * Package protected constructor
	 */
	NameAuthority(){}
	
	/**
	 * Create a handle name authority
	 * 
	 * @param handleNamingAuthority
	 * @param handleLocalName
	 */
	public NameAuthority(String namingAuthority, String localName)
	{
		this.setLocalName(localName);
		this.setNamingAuthority(namingAuthority);
	}

	public String getNamingAuthority() {
		return namingAuthority;
	}

	public void setNamingAuthority(String handleNamingAuthority) {
		this.namingAuthority = handleNamingAuthority;
	}
	
	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String handleLocalName) {
		this.localName = handleLocalName;
	}
	
	public String getAuthorityBaseUrl() {
		return authorityBaseUrl;
	}

	public void setAuthorityBaseUrl(String authorityBaseUrl) {
		this.authorityBaseUrl = authorityBaseUrl;
	}
	
	/**
	 * Returns the full name authority
	 *  e.g. 0.NA/12345 where
	 *  0.NA is the naming authority and 12345 is the local name
	 * @return
	 */
	public String getFullNameAuthority()
	{
		return namingAuthority + "/" + localName;
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof NameAuthority)) return false;

		final NameAuthority other = (NameAuthority) o;
		if( (namingAuthority != null && !namingAuthority.equals(other.getNamingAuthority()) ) ||
			(namingAuthority == null && other.getNamingAuthority() != null ) ) return false;
		
		if( (localName != null && !localName.equals(other.getLocalName()) ) ||
			(localName == null && other.getLocalName() != null ) ) return false;
		
		return true;
	}
	
	public int hashCode()
	{
		int value = 0;
		value += namingAuthority == null ? 0 : namingAuthority.hashCode();
		value += localName == null ? 0 : localName.hashCode();
		return value;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" namingAuthority = ");
		sb.append( namingAuthority);
		sb.append(" localName = ");
		sb.append(localName);
		sb.append( " base url = ");
		sb.append(authorityBaseUrl);
		sb.append("]");
		return sb.toString();
	}
	
	

}
