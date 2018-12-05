package gatosolveTSP;

import javax.swing.*;

import java.awt.*;
import java.io.*;

@SuppressWarnings("serial")
public class GeneticAlgorithm extends JPanel {

	int cityNum = 48;

	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		
		try {
			GA ga = new GA(50, 48, 3000, 0.8f, 0.9f);
			ga.init("./data/data.txt");
			ga.solve();
			int[] bestTour = ga.getBestTour();
			int bestLength=ga.getBestLength();
			g.drawString("The Best Length:"+bestLength, 30, 540);

			StringBuilder sb=new StringBuilder();
			for (int i = 0; i < cityNum; i++) {
				sb.append(bestTour[i]+"-");
	        }
			sb.delete(sb.length()-1, sb.length());
			g.drawString("The Best Tour: "+sb.toString(), 30, 570);
			
			g.setColor(Color.RED); 
			int[] x;
			int[] y;

			String strbuff;
			BufferedReader data = new BufferedReader(new InputStreamReader(
					new FileInputStream("./data/data.txt")));

			x = new int[cityNum];
			y = new int[cityNum];
			for (int i = 0; i < cityNum; i++) {
				strbuff = data.readLine();
				String[] strcol = strbuff.split(" ");
				x[i] = Integer.valueOf(strcol[1]);
				y[i] = Integer.valueOf(strcol[2]);
				g.fillOval(x[i] / 10, y[i] / 10, 5, 5);
				g.drawString(String.valueOf(i), x[i] / 10, y[i] / 10);
			}
			data.close();
			g.setColor(Color.BLUE); 
			for(int j=0;j<cityNum-1;j++)
			{
				g.drawLine(x[bestTour[j]]/ 10, y[bestTour[j]]/ 10, x[bestTour[j+1]]/ 10, y[bestTour[j+1]]/ 10);
			}
			
			g.setColor(Color.GREEN); 
			g.fillOval(x[bestTour[0]]/ 10, y[bestTour[0]]/ 10, 6, 6);
			g.fillOval(x[bestTour[cityNum-1]]/ 10, y[bestTour[cityNum-1]]/ 10, 6, 6);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		JFrame f = new JFrame();
		f.setTitle("Genetic Algorithm to Solve Travel Salesperson Problem");
		f.getContentPane().add(new GeneticAlgorithm());
		f.setSize(1000, 640);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}

