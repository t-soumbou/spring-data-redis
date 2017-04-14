package org.demo.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.demo.data.record.CarRecord ;
import org.demo.persistence.CarPersistence;

/**
 * Generic test class for a persistence service
 */
public class CarPersistenceGenericTest {

	/**
	 * Persistence service generic test for a record with an auto-incremented key
	 * @param persistenceService
	 * @throws SQLException
	 */
	public void testPersistenceServiceWithAutoincrementedKey(CarPersistence persistenceService) {
    	System.out.println("--- test CarPersistence ");

    	CarRecord record = new CarRecord();
		// Auto-incremented key : nothing to set in the Primary Key
		//--- Other values
		record.setLastname("A"); // "lastname" : java.lang.String
		record.setFirstname("A"); // "firstname" : java.lang.String

		long initialCount = persistenceService.countAll() ;
    	System.out.println("Initial count = " + initialCount );

    	//--- CREATE #1
    	System.out.println("Create : " + record);
    	persistenceService.create(record);
		// Retrieve the generated id 
		int generatedId = record.getId() ;
    	System.out.println("Generated id #1 = " + generatedId ); 
    	assertTrue( persistenceService.exists(generatedId) );
		assertTrue( persistenceService.exists(record) );
		long count = persistenceService.countAll() ;
    	System.out.println("Count = " + count );
		assertEquals(initialCount+1, count );

    	//--- CREATE #2
		record = new CarRecord();  // Mandatory for JPA 
		persistenceService.create(record);
		int generatedId2 = record.getId() ;
    	System.out.println("Generated id #2 = " + generatedId2 ); 
		assertEquals(generatedId+1, generatedId2 );

    	//--- CREATE #3
		record = new CarRecord();  // Mandatory for JPA 
		persistenceService.create(record);
		int generatedId3 = record.getId() ;
    	System.out.println("Generated id #3 = " + generatedId3 ); 
		assertEquals(generatedId2+1, generatedId3 );

    	//--- FIND #1
    	System.out.println("Find by id #1 ..." );
    	CarRecord record1 = persistenceService.findById(generatedId);
    	System.out.println("Found : " + record1 );
    	assertNotNull(record1);
    	assertTrue( persistenceService.exists(record1) ) ;

    	//--- FIND #2
    	System.out.println("Find by id #2 ..." );
    	CarRecord record2 = persistenceService.findById(generatedId2);
    	System.out.println("Found : " + record2 );
    	assertNotNull(record2);
    	assertTrue( persistenceService.exists(record2) ) ;

    	//--- UPDATE
		//--- Change values
		record2.setLastname("B"); // "lastname" : java.lang.String
		record2.setFirstname("B"); // "firstname" : java.lang.String
    	System.out.println("Update : " + record2 );
    	assertTrue( persistenceService.update(record2) );

    	//--- RELOAD AFTER UPDATE
    	System.out.println("Find by id..." );
    	CarRecord record2reloaded = persistenceService.findById(generatedId2);
    	assertNotNull(record2reloaded);
    	System.out.println("Found : " + record2reloaded );
		assertEquals(record2.getLastname(), record2reloaded.getLastname() ); 
		assertEquals(record2.getFirstname(), record2reloaded.getFirstname() ); 

    	//--- DELETE
    	System.out.println("Delete : " + record2 );
    	assertTrue(  persistenceService.delete(record2) ); // First delete : OK
    	assertFalse( persistenceService.delete(record2) ); // Nothing (already deleted)
    	assertFalse( persistenceService.deleteById(generatedId2) ); // Nothing (already deleted)

	}
}
