import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GenGraph extends JFrame {
    public ArrayList<int[]> points;
    public GenGraph(){
      super("GenGraph");
      setLayout(new BorderLayout());
      DrawPane DPane = new DrawPane();
      //DPane.setPreferredSize(new Dimension(600, 400));
      JScrollPane pane=new JScrollPane(DPane,
              ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
      pane.setPreferredSize(new Dimension(400, 600));
      JScrollBar horizontal = pane.getHorizontalScrollBar();
      horizontal.setValue( horizontal.getMaximum() );
      pane.revalidate();
      add(pane, BorderLayout.CENTER);
      pack();
      //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
      //this.setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(600, 420);
      revalidate();
      repaint();
      setVisible(true);
      //this.repaint();
      points = new ArrayList<int[]>();
      
    }
    
    public void addPoint(int x, int y){
        points.add(new int[]{x, y});
        repaint();
    }
  
    class DrawPane extends JPanel{
        public DrawPane(){
            
        }
        public Graphics graphics;
        /**
        @Override
        public void setPreferredSize(Dimension preferredSize){
            this.setPreferredSize(preferredSize);
        }
        **/
        @Override
        protected void paintComponent(Graphics g){
            graphics = g;
            super.paintComponent(g);
            drawGraph();
        }
        
        public void drawGraph(){
            int width = 500;//getContentPane().getSize().width;
            int height = 350;//getContentPane().getSize().height;
            int heightMult = 3;
            //int breakpoint = points.size()/width+1;
            int counter = 0;
            int max = 0;
            int y = 0;
            int x = 20;
            int avg = 0;
            int lastAvg = 0;
            int bestAvg = 0;
            for(int i = 0; i < points.size(); i++){
                this.setPreferredSize(new Dimension(300+i, 500));
                int nSurvivors = points.get(i)[1];
                avg += nSurvivors;
                y+=nSurvivors;
                counter++;
                //if(counter >= breakpoint){
                graphics.fillOval(x+2, height-50-(y/counter)*heightMult, 3, 3);
                counter = 0;
                y = 0;
                x++;
                //}
                if(nSurvivors > max){
                    max = nSurvivors;
                }
                if(points.get(i)[0]%100 == 0){
                    lastAvg = avg/100;
                    if(bestAvg < lastAvg){
                        bestAvg = lastAvg;
                    }
                    avg = 0;
                }
            }
            graphics.drawString("Max Survivors: " + max, 10, height-5);
            graphics.drawString("Last Avg: " + lastAvg + "   Best Avg: " + bestAvg, 10, height-20);
            for(int i = 0; i < 100; i+=10){
                graphics.drawString(""+i, 7, height - 50 - i*heightMult);
            }
            if(points.size() > 0){
                graphics.drawString("Generation " + points.get(points.size()-1)[0], width/3, 20);
            }
        }
    }
}