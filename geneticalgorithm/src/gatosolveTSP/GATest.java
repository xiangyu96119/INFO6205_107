package gatosolveTSP;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;


public class GATest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBestlength1() throws Exception {
        GA tspProblem = new GA(50, 48, 8000, 0.8f, 0.9f);
        tspProblem.init("./data/data.txt");
        
        tspProblem.solve();
        
        int s1 = tspProblem.getBestLength();
        int s0 = 10700;//optimal value is 10700
        double result = 10700d/s1;
        assertEquals(1, result,0.5);
    }

    @Test
    public void testBestlength2() throws Exception {
        GA tspProblem = new GA(50, 48, 5000, 0.5f, 0.5f);
        
        tspProblem.init("./data/data2.txt");
        
        tspProblem.solve();
        
        int s1 = tspProblem.getBestLength();
        int s0 = 5170;//optimal value is 5170
        double result = 5170d/s1;
        
        assertEquals(1, result, 0.5);
    }
    @Test
	public void testGetCrossover()throws Exception {
    	    GA tspProblem = new GA(50, 48, 3000, 0.8f, 0.9f);
    	    tspProblem.init("./data/data.txt");  
    		tspProblem.solve();
		assertEquals(80, (int) (tspProblem.getCrossrate() * 100));

		tspProblem = new GA(50, 48, 5000, 1.0f, 0.9f);  
		tspProblem.init("./data/data.txt");  
		tspProblem.solve();
		assertEquals(100, (int) (tspProblem.getCrossrate() * 100));

		tspProblem = new GA(50, 48, 10000, 0.5f, 0.5f);
		tspProblem.init("./data/data.txt");  
		tspProblem.solve();
		assertEquals(50, (int) (tspProblem.getCrossrate() * 100));
	}
    @Test
	public void testGetMutation() throws IOException {
    	    GA tspProblem = new GA(50, 48, 3000, 0.8f, 0.5f);
	    tspProblem.init("./data/data.txt");  
		tspProblem.solve();
		assertEquals(50, (int) (tspProblem.getMutationrate() * 100));

		tspProblem = new GA(50, 48, 5000, 0.8f, 0.1f);
	    tspProblem.init("./data/data.txt");  
		tspProblem.solve();
		assertEquals(10, (int) (tspProblem.getMutationrate() * 100));

		tspProblem = new GA(50, 48, 8000, 0.8f, 0.99f);
	    tspProblem.init("./data/data.txt");  
		tspProblem.solve();
		assertEquals(99, (int) (tspProblem.getMutationrate() * 100));
	}
    @Test
    public void testEvolve() throws IOException{
      	GA tspProblem = new GA(50, 48, 5000, 0.8f, 0.5f);
	    tspProblem.init("./data/data.txt");  
		 int [][] oldArr = tspProblem.getOldPopulation();
	    tspProblem.solve();
		int [][] newArr = tspProblem.getNewPopulation();

		assertEquals(80, (int) (tspProblem.getCrossrate() * 100));
		assertEquals(50, (int) (tspProblem.getMutationrate() * 100));
        
		int counter = newArr.length;
		assertTrue(counter >= 50);
		
		int elitism  = tspProblem.getBestT();
		assertTrue(elitism < 5000);
	}
    
    @Test
	public void testGetFitness() throws IOException {
    	   GA tspProblem = new GA(50, 48, 8000, 0.8f, 0.5f);
	    tspProblem.init("./data/data.txt");  
		tspProblem.solve();
		int bestlength = tspProblem.getBestLength();
		int [] fitness = tspProblem.getFitness();
		
		for(int i =0; i<50; i++){
		assertTrue(bestlength <= fitness[i]);
		}
		
		
	}
	

}
