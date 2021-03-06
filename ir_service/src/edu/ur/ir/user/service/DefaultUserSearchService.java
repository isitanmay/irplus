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


package edu.ur.ir.user.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;

import edu.ur.ir.SearchHelper;
import edu.ur.ir.SearchResults;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserSearchService;

/**
 * Default implementation of user search service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserSearchService implements UserSearchService{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -4995845331563910038L;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserSearchService.class);
	
	/** File system service for loading file system objects */
	private UserService userService;
	
	/** Fields to be searched   */
	private String[] fields = {DefaultUserIndexService.USER_NAME,
			DefaultUserIndexService.USER_FIRST_NAME,
			DefaultUserIndexService.USER_LAST_NAME,
			DefaultUserIndexService.USER_EMAILS,
			DefaultUserIndexService.USER_DEPARTMENTS,
			DefaultUserIndexService.USER_NAMES};
	
	/** Analyzer for dealing with analyzing the search */
	private transient Analyzer analyzer;


	public SearchResults<IrUser> search(File userIndexFolder, String query,
			int offset, int numResults) {
		SearchResults<IrUser> searchResults = new SearchResults<IrUser>();
		searchResults.setOriginalQuery(query);
		query = SearchHelper.prepareMainSearchString(query, true);
		ArrayList<IrUser> users = new ArrayList<IrUser>();
		if( log.isDebugEnabled())
		{
			log.debug("User search results executing query " + query 
					+ " on index " + userIndexFolder.getAbsolutePath());
		}
		
		String indexFolder = userIndexFolder.getAbsolutePath();
		IndexSearcher searcher = null;
		IndexReader reader = null;
		try {
			FSDirectory directory = FSDirectory.open(new File(indexFolder));
			reader = IndexReader.open(directory, true);
			searcher = new IndexSearcher(reader);
			QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35, fields, analyzer);
			parser.setDefaultOperator(QueryParser.AND_OPERATOR);
			
			Query luceneQuery = parser.parse(query);
			TopDocs hits = searcher.search(luceneQuery, 1000);
			searchResults.setTotalHits(hits.totalHits);
			
			int position = offset;
			int addedResults = 0;
			while( hits.totalHits > position  && (addedResults < numResults))
			{
				if( log.isDebugEnabled())
				{
					log.debug( " adding document at position " + position);
					
				}
				
				Document d = searcher.doc(hits.scoreDocs[position].doc);
				Long userId = NumericUtils.prefixCodedToLong(d.get(DefaultUserIndexService.USER_ID));
				log.debug( "user id = " + userId);
				IrUser user = userService.getUser(userId, false);
				users.add(user);
				addedResults += 1;
				position += 1;
			}
		} catch (Exception e) {
			log.error(e);
		}
		finally
		{
			if( searcher != null )
			{
				try {
					searcher.close();
				} catch (IOException e) {
					log.error("the searcher could not be closed", e);
				}
			}
			if( reader != null )
			{
				try {
					reader.close();
				} catch (IOException e) {
					log.error("the reader could not be closed", e);
				}
			}
		}
		searchResults.setObjects(users);
		return searchResults;
	}

	/**
	 * Set the user service to access user information.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}



	/**
	 * Set the analyzer for searching the index.
	 * 
	 * @param analyzer
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
}
