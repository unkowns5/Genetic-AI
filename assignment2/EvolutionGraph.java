import javax.swing.*;
import java.awt.*;
import java.util.*;

public class EvolutionGraph extends JFrame {
    public ArrayList<int[]> points;
    public EvolutionGraph(){
      super("EvolutionGraph");
      setContentPane(new DrawPane());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(800, 800);
      setVisible(true);
      points = new ArrayList<int[]>();
    }
    
    public void addPoint(int x, int y){
        points.add(new int[]{x, y});
        repaint();
    }
  
    class DrawPane extends JPanel{
        public Graphics graphics;
        public void paintComponent(Graphics g){
            graphics = g;
            drawGraph();
        }

        public void drawGraph(){
            int width = getContentPane().getSize().width;
            int height = getContentPane().getSize().height;
            int heightMult = 20;
            int breakpoint = points.size()/width+1;
            int counter = 0;
            int max = 0;
            int y = 0;
            int x = 0;
            int avg = 0;
            int lastAvg = 0;
            int bestAvg = 0;
            for(int i = 0; i < points.size(); i++){
                int survivors = points.get(i)[1];
                avg += survivors;
                y+=survivors;
                counter++;
                if(counter >= breakpoint){
                    graphics.fillOval(x+15, height-40-(y/counter), 2, 2);
                    counter = 0;
                    y = 0;
                    x++;
                }
                if(survivors > max){
                    max = survivors;
                }
                if(points.get(i)[0]%100 == 0){
                    lastAvg = avg/100;
                    if(bestAvg < lastAvg){
                        bestAvg = lastAvg;
                    }
                    avg = 0;
                }
            }
            graphics.drawString("Max Fitness: " + max, 10, height-5);
            graphics.drawString("Last Avg: " + lastAvg + "   Best Avg: " + bestAvg, 10, height-20);
            for(int i = 0; i < 80; i+=5){
                graphics.drawString(""+i, 7, height - 35 - i);
            }
            if(points.size() > 0){
                graphics.drawString("Generation " + points.get(points.size()-1)[0], width/3, 20);
            }
        }
    }
}