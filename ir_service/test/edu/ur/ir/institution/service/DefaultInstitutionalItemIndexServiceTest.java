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


package edu.ur.ir.institution.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.DeletedInstitutionalItemService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.institution.InstitutionalItemIndexService;
import edu.ur.ir.institution.InstitutionalItemSearchService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeService;
import edu.ur.ir.item.DuplicateContributorException;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeService;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherService;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.SeriesService;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;


/**
 * Test the service methods for the institutional item index services
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultInstitutionalItemIndexServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");
	
	/** Repository data access */
	RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
	
	/** User File System data access */
	UserFileSystemService userFileSystemService = (UserFileSystemService) ctx.getBean("userFileSystemService");
	
	/** Service for dealing with contributors */
	ContributorTypeService contributorTypeService = (ContributorTypeService) ctx.getBean("contributorTypeService");
	
	/** Item services */
	ItemService itemService = (ItemService) ctx.getBean("itemService");

	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** Collection service  */
    InstitutionalCollectionService institutionalCollectionService = 
    	(InstitutionalCollectionService) ctx.getBean("institutionalCollectionService");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** User data access */
	UserPublishingFileSystemService userPublishingFileSystemService = 
		(UserPublishingFileSystemService) ctx.getBean("userPublishingFileSystemService");
	
	/** person data access */
	PersonService personService = (PersonService) ctx
	.getBean("personService");
	
	/** Service for creating identifier types  */
	IdentifierTypeService identifierTypeService= (IdentifierTypeService)ctx
	.getBean("identifierTypeService");
	
	/** Series service for dealing with series */
	SeriesService seriesService = (SeriesService) ctx.getBean("seriesService");
	
	/** Service for dealing with content types */
	ContentTypeService contentTypeService = (ContentTypeService) ctx.getBean("contentTypeService");
	
	/** Service for dealing with publishers */
	PublisherService publisherService = (PublisherService) ctx.getBean("publisherService");
	
	/** Service for dealing with language types */
	LanguageTypeService languageTypeService = (LanguageTypeService) ctx.getBean("languageTypeService");
	
	/** Service for indexing institutional items */
	InstitutionalItemIndexService institutionalItemIndexService = 
		(InstitutionalItemIndexService) ctx.getBean("institutionalItemIndexService");
	
	/** service for dealing with institutional items */
	InstitutionalItemService institutionalItemService = (InstitutionalItemService)ctx.getBean("institutionalItemService");
	
	/** Service for searching institutional items */
	InstitutionalItemSearchService institutionalItemSearchService = 
		(InstitutionalItemSearchService) ctx.getBean("institutionalItemSearchService");
	
	/** service for marking items that need to be indexed */
	private InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService = 
		(InstitutionalItemIndexProcessingRecordService) ctx.getBean("institutionalItemIndexProcessingRecordService");
	
	/** index processing type service */
	private IndexProcessingTypeService indexProcessingTypeService = 
		(IndexProcessingTypeService) ctx.getBean("indexProcessingTypeService");

    /** Deleted Institutional Item service  */
    private DeletedInstitutionalItemService deletedInstitutionalItemService = 
    	(DeletedInstitutionalItemService) ctx.getBean("deletedInstitutionalItemService");
	/**
	 * Executes the query returning the number of hits.
	 * 
	 * @param field - field to search on
	 * @param queryString - query string
	 * @param dir - lucene index to search
	 * 
	 * @return - number of hits
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	private int executeQuery(String field, String queryString, Directory dir)
			throws CorruptIndexException, IOException, ParseException {
		
		IndexReader reader = IndexReader.open(dir, true);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser(Version.LUCENE_35, field, new StandardAnalyzer(Version.LUCENE_35));
		Query q1 = parser.parse(queryString);
		TopDocs hits = searcher.search(q1, 1000);
		int hitCount = hits.totalHits;

		searcher.close();
		reader.close();

		return hitCount;
	}
	
	/**
	 * Test indexing an institutional item
	 * 
	 * @throws NoIndexFoundException 
	 * @throws IllegalFileSystemNameException 
	 * @throws DuplicateNameException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws DuplicateContributorException 
	 * @throws LocationAlreadyExistsException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void testIndexInstitutionalItem() throws NoIndexFoundException, 
	IllegalFileSystemNameException, 
	DuplicateNameException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException, 
	DuplicateContributorException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException, IOException, ParseException
	{
		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		institutionalCollectionService.saveCollection(collection);
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - Institutional Item Index Service test");

		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		
		// create a personal file to add to the item
		PersonalFile pf = userFileSystemService.addFileToUser(repo, f, user, 
		    		"test_file.txt", "description");
		
		// create a person name to add to the item as a contributor
		PersonName personName = new PersonName();
		personName.setFamilyName("familyName");
		personName.setForename("forename");
		personName.setInitials("n.d.s.");
		personName.setMiddleName("MiddleName");
		personName.setNumeration("III");
		personName.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(personName);
		personService.save(p);
		
        // create a contributor type
		ContributorType contributorType1 = new ContributorType("contributorType1");
		contributorTypeService.save(contributorType1);
		
		// create the contributor
		Contributor c = new Contributor();
		c.setPersonName(personName);
		c.setContributorType(contributorType1);
		
		// create an identifier
	    IdentifierType identType = new IdentifierType("identTypeName", "identTypeDescription");
 		
 		identifierTypeService.save(identType);
 		
		// create a series
 		Series series = new Series("my series", "12345");
 		seriesService.saveSeries(series);
 		
 		// create a content type
 		ContentType contentType = new ContentType("contentType");
 		contentTypeService.saveContentType(contentType);
 		
 		// create a publisher
 		Publisher publisher = new Publisher("publisher");
 		publisherService.savePublisher(publisher);
 		
 		// create a language
 		LanguageType languageType = new LanguageType();
 		languageType.setName("language");
 		languageTypeService.save(languageType);
 		
		// create a personal item to publish into the repository
		PersonalItem item = userPublishingFileSystemService.createRootPersonalItem(user, "articles ",  "personalItem");
		GenericItem genericItem = item.getVersionedItem().getCurrentVersion().getItem();
		genericItem.addFile(pf.getVersionedFile().getCurrentVersion().getIrFile());
		genericItem.addContributor(c);
		genericItem.updateFirstAvailableDate(9, 8, 1977);
        genericItem.addItemIdentifier("identifier value", identType);
        genericItem.createLink("msnbc", "http://www.msnbc.com");
        genericItem.updateOriginalItemCreationDate(9, 8, 2001);
        genericItem.addReport(series, "report 3456");
        genericItem.addSubTitle("the sub title", "The articles");
        genericItem.setItemAbstract("abstract");
        genericItem.setItemKeywords("biology, keyword");
        genericItem.setDescription("description");
        
        genericItem.setPrimaryContentType(contentType);
        genericItem.setLanguageType(languageType);
        genericItem.setName("name");
        ExternalPublishedItem externalPublishedItem = genericItem.createExternalPublishedItem();
        
        externalPublishedItem.setCitation("citation");
        externalPublishedItem.setPublisher(publisher);
        externalPublishedItem.updatePublishedDate(10, 1, 2005);
        
        
        userPublishingFileSystemService.makePersonalItemPersistent(item);
        
        // add the item to the collection
        InstitutionalItem institutionalItem = collection.createInstitutionalItem(genericItem);
        institutionalItemService.saveInstitutionalItem(institutionalItem);
        
        // add the item to the index
		tm.commit(ts);
		
		// test searching for the data
		ts = tm.getTransaction(td);
        institutionalItemIndexService.addItem(institutionalItem, new File(repo.getInstitutionalItemIndexFolder()), false);

		Directory  lucenDirectory = FSDirectory.open(new File(repo.getInstitutionalItemIndexFolder()));
		// search the document and make sure we can find the stored data
	
			int hits = executeQuery(DefaultInstitutionalItemIndexService.ABSTRACT, 
					"abstract", 
					lucenDirectory);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.ABSTRACT + " " 
			+ "abstract";
			

			hits = executeQuery(DefaultInstitutionalItemIndexService.CITATION,
					"citation", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.CITATION + " " 
			+ "abstract";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.CONTRIBUTOR_NAMES_ANALYZED,
					"familyName", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.CONTRIBUTOR_NAMES_ANALYZED + " " 
			+ "familyName";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.FILE_TEXT,
					"Institutional Item Index", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.FILE_TEXT + " " 
			+ "Institutional Item Index";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.IDENTIFIERS,
					"identifier", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.IDENTIFIERS + " " 
			+ "identifier";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.KEY_WORDS_ANALYZED,
					"biology", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.KEY_WORDS_ANALYZED + " " 
			+ "biology";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.LANGUAGE_ANALYZED,
					"language", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.LANGUAGE_ANALYZED + " " 
			+ "language";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.LINK_NAMES,
					"msnbc", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.LINK_NAMES + " " 
			+ "msnbc";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.PUBLISHER_ANALYZED,
					"publisher", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.PUBLISHER_ANALYZED + " " 
			+ "publisher";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.SUB_TITLES,
					"sub title", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.SUB_TITLES + " " 
			+ "sub title";
			
		
		
		institutionalItemIndexService.deleteItem(institutionalItem.getId(), new File(repo.getInstitutionalItemIndexFolder()));
		
		lucenDirectory = FSDirectory.open(new File(repo.getInstitutionalItemIndexFolder()));
		// search the document and make sure we can NOT find the stored data

		hits = executeQuery(DefaultInstitutionalItemIndexService.ABSTRACT, 
					"abstract", 
					lucenDirectory);

			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.ABSTRACT + " " 
			+ "abstract";
			
		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
		institutionalItemService.deleteInstitutionalItem(institutionalItemService.getInstitutionalItem(institutionalItem.getId(), false), user);
		deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
		IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
		helper.cleanUpRepository();
		personService.delete(personService.getAuthority(p.getId(), false));
		identifierTypeService.delete(identifierTypeService.get(identType.getId(), false));
		seriesService.deleteSeries(series.getId());
		contentTypeService.deleteContentType(contentType.getId());
		publisherService.deletePublisher(publisher.getId());
		languageTypeService.delete(languageTypeService.get(languageType.getId(), false));
		contributorTypeService.delete(contributorTypeService.get(contributorType1.getId(), false));
		tm.commit(ts);	
	}
	
	/**
	 * Test deleting a collection - make sure the item is removed from the index
	 * 
	 * @throws NoIndexFoundException 
	 * @throws IllegalFileSystemNameException 
	 * @throws DuplicateNameException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws DuplicateContributorException 
	 * @throws LocationAlreadyExistsException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void testDeleteSearchCollectionItemIndex() throws NoIndexFoundException, 
	IllegalFileSystemNameException, 
	DuplicateNameException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException, 
	DuplicateContributorException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException, IOException, ParseException
	{
		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		
		IndexProcessingType deleteProcessingType = new IndexProcessingType(IndexProcessingTypeService.DELETE);
		indexProcessingTypeService.save(deleteProcessingType);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		institutionalCollectionService.saveCollection(collection);
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - Institutional Item Index Service test");

		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		
		// create a personal file to add to the item
		PersonalFile pf = userFileSystemService.addFileToUser(repo, f, user, 
		    		"test_file.txt", "description");
		
		// create a person name to add to the item as a contributor
		PersonName personName = new PersonName();
		personName.setFamilyName("familyName");
		personName.setForename("forename");
		personName.setInitials("n.d.s.");
		personName.setMiddleName("MiddleName");
		personName.setNumeration("III");
		personName.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(personName);
		personService.save(p);
		
        // create a contributor type
		ContributorType contributorType1 = new ContributorType("contributorType1");
		contributorTypeService.save(contributorType1);

		
		// create the contributor
		Contributor c = new Contributor();
		c.setPersonName(personName);
		c.setContributorType(contributorType1);
		
		// create an identifier
	    IdentifierType identType = new IdentifierType("identTypeName", "identTypeDescription");
		
 		identifierTypeService.save(identType);
 		
		// create a series
 		Series series = new Series("my series", "12345");
 		seriesService.saveSeries(series);
 		
 		// create a content type
 		ContentType contentType = new ContentType("contentType");
 		contentTypeService.saveContentType(contentType);
 		
 		// create a publisher
 		Publisher publisher = new Publisher("publisher");
 		publisherService.savePublisher(publisher);
 		
 		// create a language
 		LanguageType languageType = new LanguageType();
 		languageType.setName("language");
 		languageTypeService.save(languageType);
 		
		// create a personal item to publish into the repository
		PersonalItem item = userPublishingFileSystemService.createRootPersonalItem(user, "articles", "personalItem");
		GenericItem genericItem = item.getVersionedItem().getCurrentVersion().getItem();
		genericItem.addFile(pf.getVersionedFile().getCurrentVersion().getIrFile());
		genericItem.addContributor(c);
		genericItem.updateFirstAvailableDate(9, 8, 1977);
        genericItem.addItemIdentifier("identifier value", identType);	
        genericItem.createLink("msnbc", "http://www.msnbc.com");
        genericItem.updateOriginalItemCreationDate(9, 8, 2001);
        genericItem.addReport(series, "report 3456");
        genericItem.addSubTitle("the sub title", "The Articles");
        genericItem.setItemAbstract("abstract");
        genericItem.setItemKeywords("biology, keyword");
        genericItem.setDescription("description");
        genericItem.setPrimaryContentType(contentType);
        genericItem.setLanguageType(languageType);
        genericItem.setName("name");
        ExternalPublishedItem externalPublishedItem = genericItem.createExternalPublishedItem();
        
        externalPublishedItem.setCitation("citation");
        externalPublishedItem.setPublisher(publisher);
        externalPublishedItem.updatePublishedDate(10, 1, 2005);
        
        userPublishingFileSystemService.makePersonalItemPersistent(item);
        
        // add the item to the collection
        InstitutionalItem institutionalItem = collection.createInstitutionalItem(genericItem);
        institutionalItemService.saveInstitutionalItem(institutionalItem);
        
        // add the item to the index
		tm.commit(ts);
		
		// test searching for the data
		ts = tm.getTransaction(td);
        institutionalItemIndexService.addItem(institutionalItem, new File(repo.getInstitutionalItemIndexFolder()), false);

		Directory lucenDirectory = FSDirectory.open(new File(repo.getInstitutionalItemIndexFolder()));

			int hits = executeQuery(DefaultInstitutionalItemIndexService.ABSTRACT, 
					"abstract", 
					lucenDirectory);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.ABSTRACT + " " 
			+ "abstract";
			

			hits = executeQuery(DefaultInstitutionalItemIndexService.CITATION,
					"citation", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.CITATION + " " 
			+ "abstract";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.CONTRIBUTOR_NAMES_ANALYZED,
					"familyName", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.CONTRIBUTOR_NAMES_ANALYZED + " " 
			+ "familyName";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.FILE_TEXT,
					"Institutional Item Index", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.FILE_TEXT + " " 
			+ "Institutional Item Index";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.IDENTIFIERS,
					"identifier", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.IDENTIFIERS + " " 
			+ "identifier";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.KEY_WORDS_ANALYZED,
					"biology", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.KEY_WORDS_ANALYZED + " " 
			+ "biology";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.LANGUAGE_ANALYZED,
					"language", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.LANGUAGE_ANALYZED + ": " 
			+ "language";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.LINK_NAMES,
					"msnbc", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.LINK_NAMES + " " 
			+ "msnbc";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.PUBLISHER_ANALYZED,
					"publisher", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.PUBLISHER_ANALYZED + " " 
			+ "publisher";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.SUB_TITLES,
					"sub title", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.SUB_TITLES + " " 
			+ "sub title";
			
		
		institutionalItemIndexProcessingRecordService.processItemsInCollection(collection, deleteProcessingType);
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		List <InstitutionalItemIndexProcessingRecord> records  = institutionalItemIndexProcessingRecordService.getAllOrderByItemIdUpdatedDate(0, 1000);		
		for( InstitutionalItemIndexProcessingRecord record : records )
		{
		    InstitutionalItem i = institutionalItemService.getInstitutionalItem(record.getInstitutionalItemId(), false);
		    if( record.getIndexProcessingType().getName().equals(IndexProcessingTypeService.DELETE))
		    {
		        institutionalItemIndexService.deleteItem(i.getId(), new File(repo.getInstitutionalItemIndexFolder()));
		        institutionalItemIndexProcessingRecordService.delete(record);
		    }
		}
		
			repo = helper.getRepository();
			lucenDirectory = FSDirectory.open(new File(repo.getInstitutionalItemIndexFolder()));
		// search the document and make sure we can NOT find the stored data
		hits = executeQuery(DefaultInstitutionalItemIndexService.ABSTRACT, 
					"abstract", 
					lucenDirectory);

			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.ABSTRACT + " " 
			+ "abstract";
			
	
		
	    // Start new transaction - clean up the data
		institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(), false), user);
		deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
		IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
		helper.cleanUpRepository();
		personService.delete(personService.getAuthority(p.getId(), false));
		identifierTypeService.delete(identifierTypeService.get(identType.getId(), false));
		seriesService.deleteSeries(series.getId());
		contentTypeService.deleteContentType(contentType.getId());
		publisherService.deletePublisher(publisher.getId());
		languageTypeService.delete(languageTypeService.get(languageType.getId(), false));
		contributorTypeService.delete(contributorTypeService.get(contributorType1.getId(), false));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
		tm.commit(ts);	
	}
	

	

	

}
