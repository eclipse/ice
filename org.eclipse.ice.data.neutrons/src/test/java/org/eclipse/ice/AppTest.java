package org.eclipse.ice;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class AppTest {
   
    @Test
    public void shouldAnswerWithTrue() {
    	ReductionConfiguration ex = new ReductionConfigurationImplementation();
    	assertTrue(ex.getNumQBins() == 100);
    	
    }
}
