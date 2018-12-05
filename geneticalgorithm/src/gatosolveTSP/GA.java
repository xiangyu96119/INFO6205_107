package gatosolveTSP;

import java.io.BufferedReader;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.util.Random;  

public class GA {

	private int scale;// Population size  
    private int cityNum; // Number of cities, chromosome length  
    private int MAX_GEN; // max generation  
    private int[][] distance; // Distance matrix 
    private int bestT;// Best Number  
    private int bestLength; // Best Length  
    private int[] bestTour; // Best Tour
  
//    The initial population, the number of rows represents the size of the population, 
//    one row represents a chromosome, and the column represents a chromosomal gene fragment. 
    private int[][] oldPopulation;  
    private int[][] newPopulation;// New population, progeny population  
    private int[] fitness;// Population fitness, indicating the fitness of individual individuals in the population  
  
    private float[] Pi;// Cumulative probability of each individual in the population  
    private float Pc;// Cross probability 
    private float Pm;// Mutation probability  
    private int t;// Current algebra  
  
    private Random random;  
  
    public GA() {  
  
    }  
  
    /** 
     * constructor of GA 
     *  
     * @param s 
     *            Population size 
     * @param n 
     *            Number of cities 
     * @param g 
     *            max generation 
     * @param c 
     *            Cross rate 
     * @param m 
     *            Mutation rate 
     *  
     **/  
    public GA(int s, int n, int g, float c, float m) {  
        scale = s;  
        cityNum = n;  
        MAX_GEN = g;  
        Pc = c;  
        Pm = m;  
    }  
  
    @SuppressWarnings("resource")  
    /** 
     * @param filename 
     * @throws IOException 
     */  
    public void init(String filename) throws IOException {  
        int[] x;  
        int[] y;  
        String strbuff;  
        BufferedReader data = new BufferedReader(new InputStreamReader(  
                new FileInputStream(filename)));  
        distance = new int[cityNum][cityNum];  
        x = new int[cityNum];  
        y = new int[cityNum];  
        for (int i = 0; i < cityNum; i++) {  
            strbuff = data.readLine();  
            String[] strcol = strbuff.split(" ");  
            x[i] = Integer.valueOf(strcol[1]);
            y[i] = Integer.valueOf(strcol[2]); 
        }  
        for (int i = 0; i < cityNum - 1; i++) {  
            distance[i][i] = 0;  
            for (int j = i + 1; j < cityNum; j++) {  
                double rij = Math  
                        .sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j])  
                                * (y[i] - y[j])) / 10.0);  
                int tij = (int) Math.round(rij);  
                if (tij < rij) {  
                    distance[i][j] = tij + 1;  
                    distance[j][i] = distance[i][j];  
                } else {  
                    distance[i][j] = tij;  
                    distance[j][i] = distance[i][j];  
                }  
            }  
        }  
        distance[cityNum - 1][cityNum - 1] = 0;  
  
        bestLength = Integer.MAX_VALUE;  
        bestTour = new int[cityNum + 1];  
        bestT = 0;  
        t = 0;  
  
        newPopulation = new int[scale][cityNum];  
        oldPopulation = new int[scale][cityNum];  
        fitness = new int[scale];  
        Pi = new float[scale];  
  
        random = new Random(System.currentTimeMillis());  
        }  
  
    void initGroup() {  
        int i, j, k;  
        for (k = 0; k < scale; k++) 
        {  
            oldPopulation[k][0] = random.nextInt(65535) % cityNum;  
            for (i = 1; i < cityNum;)  
            {  
                oldPopulation[k][i] = random.nextInt(65535) % cityNum;  
                for (j = 0; j < i; j++) {  
                    if (oldPopulation[k][i] == oldPopulation[k][j]) {  
                        break;  
                    }  
                }  
                if (j == i) {  
                    i++;  
                }  
            }  
        }  
  }  
  
    public int evaluate(int[] chromosome) {  
        int len = 0;  
        for (int i = 1; i < cityNum; i++) {  
            len += distance[chromosome[i - 1]][chromosome[i]];  
        }  
        len += distance[chromosome[cityNum - 1]][chromosome[0]];  
        return len;  
    }  
  void countRate() {  
        int k;  
        double sumFitness = 0; 
        double[] tempf = new double[scale];  
        for (k = 0; k < scale; k++) {  
            tempf[k] = 10.0 / fitness[k];  
            sumFitness += tempf[k];  
        }  
  
        Pi[0] = (float) (tempf[0] / sumFitness);  
        for (k = 1; k < scale; k++) {  
            Pi[k] = (float) (tempf[k] / sumFitness + Pi[k - 1]);  
        }  
  
        /* 
        * for(k=0;k<scale;k++) { System.out.println(fitness[k]+" "+Pi[k]); } 
         */  
    }  
  
    public void selectBestGh() {  
        int k, i, maxid;  
        int maxevaluation;  
  
        maxid = 0;  
        maxevaluation = fitness[0];  
        for (k = 1; k < scale; k++) {  
            if (maxevaluation > fitness[k]) {  
                maxevaluation = fitness[k];  
                maxid = k;  
            }  
        }  
  
        if (bestLength > maxevaluation) {  
            bestLength = maxevaluation;  
            bestT = t;// Best chromosome  
            for (i = 0; i < cityNum; i++) {  
                bestTour[i] = oldPopulation[maxid][i];  
            }  
        }  
  
        // System.out.println("Value" + t + " " + maxevaluation);  
        copyGh(0, maxid);
    }  
      public void copyGh(int k, int kk) {  
        int i;  
        for (i = 0; i < cityNum; i++) {  
            newPopulation[k][i] = oldPopulation[kk][i];  
        }  
    }  
  
    public void select() {  
        int k, i, selectId;  
        float ran1;  
        // Random random = new Random(System.currentTimeMillis());  
        for (k = 1; k < scale; k++) {  
            ran1 = (float) (random.nextInt(65535) % 1000 / 1000.0);  
            // System.out.println("Possibility"+ran1);  
            for (i = 0; i < scale; i++) {  
                if (ran1 <= Pi[i]) {  
                    break;  
                }  
            }  
            selectId = i;  
            // System.out.println("SelectedId" + selectId);  
            copyGh(k, selectId);  
        }  
    }  
    public void evolution() {  
        int k;  
        selectBestGh();  
        select();  
  
        // Random random = new Random(System.currentTimeMillis());  
        float r;  
  
        for (k = 0; k < scale; k = k + 2) {  
            r = random.nextFloat();  
            // System.out.println("Cross rate..." + r);  
            if (r < Pc) {  
                // System.out.println(k + "And" + k + 1 + "Crossing...");  
                OXCross1(k, k + 1); //Cross 
            } else {  
                r = random.nextFloat();// /Probability of occurrence  
                // System.out.println("Mutation rate1..." + r);  
                // Mutation  
                if (r < Pm) {  
                    // System.out.println(k + "Mutation...");  
                    OnCVariation(k);  
                }  
                r = random.nextFloat();// Probability of occurrence  
                // System.out.println("Mutation rate2..." + r);  
                // Mutation  
                if (r < Pm) {  
                    // System.out.println(k + 1 + "Mutation...");  
                    OnCVariation(k + 1);  
                }  
            }  
  
        }  
    }  
  
    public void evolution1() {  
        int k;  
        selectBestGh();  
  
        select();  
  
        // Random random = new Random(System.currentTimeMillis());  
        float r;  
  
        for (k = 1; k + 1 < scale / 2; k = k + 2) {  
            r = random.nextFloat(); 
            if (r < Pc) {  
                OXCross1(k, k + 1);  
                //OXCross(k,k+1);  
            } else {  
                r = random.nextFloat(); 
                if (r < Pm) {  
                    OnCVariation(k);  
                }  
                r = random.nextFloat(); 
                if (r < Pm) {  
                    OnCVariation(k + 1);  
                }  
            }  
        }  
        if (k == scale / 2 - 1)  
        {  
            r = random.nextFloat();  
            if (r < Pm) {  
                OnCVariation(k);  
            }  
        }  
  
    }  
  
    void OXCross(int k1, int k2) {  
        int i, j, k, flag;  
        int ran1, ran2, temp;  
        int[] Gh1 = new int[cityNum];  
        int[] Gh2 = new int[cityNum];  
        // Random random = new Random(System.currentTimeMillis());  
  
        ran1 = random.nextInt(65535) % cityNum;  
        ran2 = random.nextInt(65535) % cityNum;  
        // System.out.println();  
        // System.out.println("-----------------------");  
        // System.out.println("----"+ran1+"----"+ran2);  
  
        while (ran1 == ran2) {  
            ran2 = random.nextInt(65535) % cityNum;  
        }  
  
        if (ran1 > ran2)  
        {  
            temp = ran1;  
            ran1 = ran2;  
            ran2 = temp;  
        }  
        // System.out.println();  
        // System.out.println("-----------------------");  
        // System.out.println("----"+ran1+"----"+ran2);  
        // System.out.println("-----------------------");  
        // System.out.println();  
        flag = ran2 - ran1 + 1; 
        for (i = 0, j = ran1; i < flag; i++, j++) {  
            Gh1[i] = newPopulation[k2][j];  
            Gh2[i] = newPopulation[k1][j];  
        }  
        
        for (k = 0, j = flag; j < cityNum;) 
        {  
            Gh1[j] = newPopulation[k1][k++];  
            for (i = 0; i < flag; i++) {  
                if (Gh1[i] == Gh1[j]) {  
                    break;  
                }  
            }  
            if (i == flag) {  
                j++;  
            }  
        }  
  
        for (k = 0, j = flag; j < cityNum;)  
        {  
            Gh2[j] = newPopulation[k2][k++];  
            for (i = 0; i < flag; i++) {  
                if (Gh2[i] == Gh2[j]) {  
                    break;  
                }  
            }  
            if (i == flag) {  
                j++;  
            }  
        }  
  
        for (i = 0; i < cityNum; i++) {  
            newPopulation[k1][i] = Gh1[i];  
            newPopulation[k2][i] = Gh2[i];  
        }  
  
        // System.out.println("Cross--------------------------");  
        // System.out.println(k1+"After Crossing...");  
        // for (i = 0; i < cityNum; i++) {  
        // System.out.print(newPopulation[k1][i] + "-");  
        // }  
        // System.out.println();  
        // System.out.println(k2+"After Crossing...");  
        // for (i = 0; i < cityNum; i++) {  
        // System.out.print(newPopulation[k2][i] + "-");  
        // }  
        // System.out.println();  
        // System.out.println("Crossed--------------------------");  
    }  
  
    public void OXCross1(int k1, int k2) {  
        int i, j, k, flag;  
        int ran1, ran2, temp;  
        int[] Gh1 = new int[cityNum];  
        int[] Gh2 = new int[cityNum];  
        // Random random = new Random(System.currentTimeMillis());  
  
        ran1 = random.nextInt(65535) % cityNum;  
        ran2 = random.nextInt(65535) % cityNum;  
        while (ran1 == ran2) {  
            ran2 = random.nextInt(65535) % cityNum;  
        }  
  
        if (ran1 > ran2)
        {  
            temp = ran1;  
            ran1 = ran2;  
            ran2 = temp;  
        }  
        for (i = 0, j = ran2; j < cityNum; i++, j++) {  
            Gh2[i] = newPopulation[k1][j];  
        }  
  
        flag = i;  
  
        for (k = 0, j = flag; j < cityNum;) 
        {  
            Gh2[j] = newPopulation[k2][k++];  
            for (i = 0; i < flag; i++) {  
                if (Gh2[i] == Gh2[j]) {  
                    break;  
                }  
            }  
            if (i == flag) {  
                j++;  
            }  
        }  
  
        flag = ran1;  
        for (k = 0, j = 0; k < cityNum;)  
        {  
            Gh1[j] = newPopulation[k1][k++];  
            for (i = 0; i < flag; i++) {  
                if (newPopulation[k2][i] == Gh1[j]) {  
                    break;  
                }  
            }  
            if (i == flag) {  
                j++;  
            }  
        }  
  
        flag = cityNum - ran1;  
  
        for (i = 0, j = flag; j < cityNum; j++, i++) {  
            Gh1[j] = newPopulation[k2][i];  
        }  
  
        for (i = 0; i < cityNum; i++) {  
            newPopulation[k1][i] = Gh1[i];  
            newPopulation[k2][i] = Gh2[i]; 
        }  
    }  
  
    public void OnCVariation(int k) {  
        int ran1, ran2, temp;  
        int count;  
  
        // Random random = new Random(System.currentTimeMillis());  
        count = random.nextInt(65535) % cityNum;  
  
        for (int i = 0; i < count; i++) {  
  
            ran1 = random.nextInt(65535) % cityNum;  
            ran2 = random.nextInt(65535) % cityNum;  
            while (ran1 == ran2) {  
                ran2 = random.nextInt(65535) % cityNum;  
            }  
            temp = newPopulation[k][ran1];  
            newPopulation[k][ran1] = newPopulation[k][ran2];  
            newPopulation[k][ran2] = temp;  
        }  
  
        /* 
         * for(i=0;i<L;i++) { printf("%d ",newGroup[k][i]); } printf("\n"); 
         */  
    }  
  
    public void solve() {  
        int i;  
        int k;  
  
        initGroup();  
        for (k = 0; k < scale; k++) {  
            fitness[k] = evaluate(oldPopulation[k]);  
            // System.out.println(fitness[k]);  
        }  
        countRate();  
        System.out.println("Initial population...");  
        for (k = 0; k < scale; k++) {  
            for (i = 0; i < cityNum; i++) {  
                System.out.print(oldPopulation[k][i] + ",");  
            }  
            System.out.println();  
            System.out.println("----" + fitness[k] + " " + Pi[k]);  
        }  
          
        for (t = 0; t < MAX_GEN; t++) {  
            evolution1();  
            for (k = 0; k < scale; k++) {  
                for (i = 0; i < cityNum; i++) {  
                    oldPopulation[k][i] = newPopulation[k][i];  
                }  
            }  
            for (k = 0; k < scale; k++) {  
                fitness[k] = evaluate(oldPopulation[k]);  
            }  
            countRate();  
        }  
  
        System.out.println("Final population...");  
        for (k = 0; k < scale; k++) {  
            for (i = 0; i < cityNum; i++) {  
                System.out.print(oldPopulation[k][i] + ",");  
            }  
            System.out.println();  
            System.out.println("---" + fitness[k] + " " + Pi[k]);  
        }  
  
        System.out.println("Generation when the optimal length occurs：");  
        System.out.println(bestT);  
        System.out.println("Best Length");  
        System.out.println(bestLength);
        System.out.println("Best Tour：");  
        for (i = 0; i < cityNum; i++) {  
            System.out.print(bestTour[i] + ",");  
        }
  
    }  
  
   public int [] getFitness() {
	   return fitness;
   }
   public int getBestLength() {
		return bestLength;
	}
    public int getBestT() {
    	return bestT;
    }

	public int[] getBestTour() {
		return bestTour;
	}
	public float getCrossrate() {
		return Pc;
	}
	public float getMutationrate() {
		return Pm;
	}
	public int getPopulation() {
		return scale;
	}
	public int getCitynum() {
		return cityNum;
	}

    public int[][] getOldPopulation() {
        return oldPopulation;
    }

    public void setOldPopulation(int[][] oldPopulation) {
        this.oldPopulation = oldPopulation;
    }

    public int[][] getNewPopulation() {
        return newPopulation;
    }

    public void setNewPopulation(int[][] newPopulation) {
        this.newPopulation = newPopulation;
    }
	


	/** 
     * @param args 
     * @throws IOException 
     */  
    public static void main(String[] args) throws IOException {  
        System.out.println("Start....");  
        GA ga = new GA(50, 48, 3000, 0.8f, 0.9f); 
       ga.init("./data/data.txt");  
        ga.solve();  
    }  
}
  


