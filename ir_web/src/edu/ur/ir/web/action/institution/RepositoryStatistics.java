package edu.ur.ir.web.action.institution;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeCount;
import edu.ur.ir.item.ContentTypeService;
import edu.ur.ir.item.SponsorService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.user.UserService;

/**
 * Allows the viewing of repository information.
 * 
 * @author Nathan Sarr
 *
 */
public class RepositoryStatistics extends ActionSupport{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -7952557504407745226L;

	/** Number of collections in the repository */
	private Long numberOfCollections;

	/** Number of publications in the repository */
	private Long numberOfPublications;
	
	/** Number of file downloads in the repository */
	private Long numberOfFileDownloads;
	
	/** Number of users in the system */
	private Long numberOfUsers;
	
	/** number of researchers */
	private Long numberOfResearchers;
	
	/** Institutional Item data access */
	private InstitutionalItemService institutionalItemService;
	
	/** File Download Statistics data access */
	private DownloadStatisticsService downloadStatisticsService;
	
	/** Institutional Collection data access */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Service to get user information */
	private UserService userService;
	
	/** Service for dealing with researcher information*/
	private ResearcherService researcherService;
	
	/** get a count of content types */
	private Set<ContentTypeCount> contentTypeCounts = new HashSet<ContentTypeCount>();
	
	/** service to get content type information */
	private ContentTypeService contentTypeService;
	
	/** Service for sponsor information */
	private SponsorService sponsorService;

	/** count for the name of the sponsors */
	private Long sponsorCount;
	
	/**
     * Get the statistics information
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception 
    {
	
	    // get the statistics
	    numberOfCollections = institutionalCollectionService.getCount();
	    numberOfPublications = institutionalItemService.getDistinctInstitutionalItemCount();
	    numberOfFileDownloads = downloadStatisticsService.getNumberOfDownloadsForAllCollections();
	    numberOfUsers = userService.getUserCount();
	    numberOfResearchers = researcherService.getPublicResearcherCount();
	    
	    List<ContentType> contentTypes = contentTypeService.getAllContentTypeByNameOrder();
	    
	    for(ContentType c : contentTypes)
	    {
	    	Long count = institutionalItemService.getCount(Repository.DEFAULT_REPOSITORY_ID, c.getId());
	    	contentTypeCounts.add(new ContentTypeCount(c, count));
	    }
	    
	    sponsorCount = sponsorService.getCount();
	    return SUCCESS;
    }
    
	public Long getNumberOfCollections() {
		return numberOfCollections;
	}


	public Long getNumberOfPublications() {
		return numberOfPublications;
	}


	public Long getNumberOfFileDownloads() {
		return numberOfFileDownloads;
	}


	public Long getNumberOfUsers() {
		return numberOfUsers;
	}


	public Long getNumberOfResearchers() {
		return numberOfResearchers;
	}


	public Set<ContentTypeCount> getContentTypeCounts() {
		return contentTypeCounts;
	}


	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}


	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}


	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}


	public void setContentTypeService(ContentTypeService contentTypeService) {
		this.contentTypeService = contentTypeService;
	}
	

	public Long getSponsorCount() {
		return sponsorCount;
	}

	public void setSponsorService(SponsorService sponsorService) {
		this.sponsorService = sponsorService;
	}


}
