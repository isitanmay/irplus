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

package edu.ur.hibernate.ir.statistics.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.statistics.FileDownloadInfo;
import edu.ur.ir.statistics.FileDownloadInfoDAO;
import edu.ur.ir.statistics.IgnoreIpAddress;
import edu.ur.ir.statistics.IgnoreIpAddressDAO;
import edu.ur.ir.statistics.IpIgnoreFileDownloadInfoDAO;


/**
 * Test file download info persistence
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class FileDownloadInfoDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	FileDownloadInfoDAO fileDownloadInfoDAO = (FileDownloadInfoDAO) ctx
	.getBean("fileDownloadInfoDAO");
	
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
	/** Data access for ignore ip address class */
	IgnoreIpAddressDAO ignoreIpAddressDAO = (IgnoreIpAddressDAO) ctx
	.getBean("ignoreIpAddressDAO");
	
	/** deals with ingore ip file downloads */
	IpIgnoreFileDownloadInfoDAO ipIgnorefileDownloadInfoDAO = (IpIgnoreFileDownloadInfoDAO) ctx
	.getBean("ipIgnoreFileDownloadInfoDAO");
	

	/**
	 * Test download info persistance
	 */
	@Test
	public void baseDownloadInfoDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
	    Date d = simpleDateFormat.parse("1/1/2008");
        FileDownloadInfo downloadInfo1 = new FileDownloadInfo("123.0.0.1", 1l,d);
        downloadInfo1.setDownloadCount(1);
        
        fileDownloadInfoDAO.makePersistent(downloadInfo1);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		FileDownloadInfo other = fileDownloadInfoDAO.getById(downloadInfo1.getId(), false);
        assert other.equals(downloadInfo1) : "File download info should be equal other = \n" + other + "\n downloadInfo1 = " + downloadInfo1;
         
        fileDownloadInfoDAO.makeTransient(other);
        assert  fileDownloadInfoDAO.getById(other.getId(), false) == null : "Should no longer be able to find file download info";
	    tm.commit(ts);
	}
	
	/**
	 * Test the ignore code.
	 * 
	 * @throws Exception
	 */
	@Test
	public void getDownloadInfoIgnoredDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
	    Date d = simpleDateFormat.parse("1/1/2008");
	    
        FileDownloadInfo downloadInfo1 = new FileDownloadInfo("123.0.0.1", 1l,d);
        downloadInfo1.setDownloadCount(1);
        
        FileDownloadInfo downloadInfo2 = new FileDownloadInfo("123.0.0.5", 1l,d);
        downloadInfo2.setDownloadCount(2);
        
        
        fileDownloadInfoDAO.makePersistent(downloadInfo1);
        fileDownloadInfoDAO.makePersistent(downloadInfo2);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 	    
 	    assert fileDownloadInfoDAO.getDownloadInfoIgnoredCount() == 0l : "Should be zero but is " + fileDownloadInfoDAO.getDownloadInfoIgnoredCount();
	    IgnoreIpAddress ip2 = new IgnoreIpAddress(123,0,0,5, 5);
	    ignoreIpAddressDAO.makePersistent(ip2);
 	    
 	    tm.commit(ts);
 	    
 	    
 	    ts = tm.getTransaction(td);
 	    assert fileDownloadInfoDAO.getDownloadInfoIgnoredCount() == 1l : "Should be one but is " + fileDownloadInfoDAO.getDownloadInfoIgnoredCount();
 	    List<FileDownloadInfo> downloadInfo = fileDownloadInfoDAO.getDownloadInfoIgnored(0, 10);
 	    
 	    assert !downloadInfo.contains(downloadInfo1) : "Should not contain " + downloadInfo1;
 	    assert downloadInfo.contains(downloadInfo2) : "Should contain " + downloadInfo2; 
 	    
        fileDownloadInfoDAO.makeTransient(fileDownloadInfoDAO.getById(downloadInfo1.getId(), false));
        fileDownloadInfoDAO.makeTransient(fileDownloadInfoDAO.getById(downloadInfo2.getId(), false));
        ignoreIpAddressDAO.makeTransient(ignoreIpAddressDAO.getById(ip2.getId(), false));
        tm.commit(ts);

	}
	
	/**
	 * Test deleting ignored downloads from the file download info table.
	 * 
	 * @throws ParseException 
	 */
	@Test
	public void deleteIngoredDownloadsTest() throws ParseException
	{
		 TransactionStatus ts = tm.getTransaction(td);

		 
		 IgnoreIpAddress ip1 = new IgnoreIpAddress(123,0,0,9, 10);
		 ip1.setStoreCounts(false);
	     ignoreIpAddressDAO.makePersistent(ip1);

	     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
		 Date d = simpleDateFormat.parse("1/1/2008");
		    
	     FileDownloadInfo downloadInfo1 = new FileDownloadInfo("123.0.0.1", 1l,d);
	     downloadInfo1.setDownloadCount(1);
	        
	     FileDownloadInfo downloadInfo2 = new FileDownloadInfo("123.0.0.10", 1l,d);
	     downloadInfo2.setDownloadCount(2);
	        
	        
	     fileDownloadInfoDAO.makePersistent(downloadInfo1);
	     fileDownloadInfoDAO.makePersistent(downloadInfo2);
	     tm.commit(ts);

	 	 // check to make sure the ip address counts are correct
	 	 ts = tm.getTransaction(td);
	 	 
	 	 Long numDeleted = fileDownloadInfoDAO.deleteIgnoreCounts();
	 	 assert numDeleted == 1l : "Should have deleted one record but deleted " + numDeleted;
	 	 assert fileDownloadInfoDAO.getById(downloadInfo2.getId(), false) == null : "should not be able to find " + downloadInfo2;
	 	 assert fileDownloadInfoDAO.getById(downloadInfo1.getId(), false) != null : "Should be able to find " + downloadInfo2;
	 	 
	 	 tm.commit(ts);
	     
	 	 
	 	 ts = tm.getTransaction(td);
	     ignoreIpAddressDAO.makeTransient(ignoreIpAddressDAO.getById(ip1.getId(), false));
	     fileDownloadInfoDAO.makeTransient(fileDownloadInfoDAO.getById(downloadInfo1.getId(), false));
		 tm.commit(ts);
	}
	
}
