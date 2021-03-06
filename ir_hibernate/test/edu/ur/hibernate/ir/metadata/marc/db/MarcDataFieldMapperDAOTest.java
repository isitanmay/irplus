/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.hibernate.ir.metadata.marc.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.metadata.marc.MarcDataFieldMapper;
import edu.ur.ir.item.metadata.marc.MarcDataFieldMapperDAO;
import edu.ur.metadata.marc.MarcDataField;
import edu.ur.metadata.marc.MarcDataFieldService;

/**
 * Test data access for the marc data field mapper.
 * 
 * @author Nathan Sarr
 *
 */
public class MarcDataFieldMapperDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** dublin core mapping data access object */
	MarcDataFieldMapperDAO marcDataFieldMapperDAO = (MarcDataFieldMapperDAO) ctx
	.getBean("marcDataFieldMapperDAO");

  
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
    MarcDataFieldService marcDataFieldService = (MarcDataFieldService) ctx
	.getBean("marcDataFieldService");
    
	/**
	 * Test mapping persistence
	 */
	@Test
	public void baseMarcDataFieldMapperDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);
 		
	    MarcDataField element = new MarcDataField("field", true, "100");
	    marcDataFieldService.save(element);
	    
 		MarcDataFieldMapper mapper = new MarcDataFieldMapper(element);
 		mapper.setIndicator1("1");
 		mapper.setIndicator1("2");
 		marcDataFieldMapperDAO.makePersistent(mapper);	    
 	    tm.commit(ts);
 	   
 	    ts = tm.getTransaction(td);
 	    
 	    MarcDataFieldMapper other = marcDataFieldMapperDAO.getById(mapper.getId(), false);
 	    assert other.equals(mapper) : " Other " + other + "\n should be equal to " + mapper;
 	    
 	    other =  marcDataFieldMapperDAO.getByMarcDataFieldIndicatorsId(element.getId(), mapper.getIndicator1(), mapper.getIndicator2());
 	    assert other.equals(mapper) : " Other " + other + "\n should be equal to " + mapper;
 	    tm.commit(ts);
 	    
 	    
	    ts = tm.getTransaction(td);
        // delete data
	    marcDataFieldMapperDAO.makeTransient(marcDataFieldMapperDAO.getById(mapper.getId(), false));
	    marcDataFieldService.delete(marcDataFieldService.getById(element.getId(), false));
	    tm.commit(ts);
	}
}
