package gameoflife;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;


public class GameOfLife {
    
    boolean [][] aktSejt, kovSejt;
    boolean [] sor;
    boolean pause = true, hasSetRowArray=false;
    int hasTicked;
    final int sejtMeret = 5, c = 500, vonalMeret = 1;
    Dimension frameSize;
    DrawPanel panel;
    
    public static void main(String[] args) {
        new GameOfLife();
    }
    
    
    GameOfLife(){
        setUpGui();
        
        while(true){
            kor(c);
            hasTicked++;
            
            if(hasTicked >= 1000){
                hasSetRowArray = false;
                hasTicked = 0;
            }
        }
    }
    
        

    private void kor(int sleepTime) {
        kovSejt = new boolean [aktSejt.length][aktSejt[0].length];
    
        try{
            if(!pause){
                for(int i = 0, sorMeret = aktSejt.length; i < sorMeret; i++){
                    try{
                        if(sor[i] || sor[i-1] || sor[i+1] || !hasSetRowArray){
                            for(int j=0, columSize = aktSejt[i].length; j < columSize; j++){
                                boolean sejtAllapot = aktSejt[i][j];
                                int szomszed = szomszed(i, j, 3, aktSejt);
                                if(aktSejt[i][j]){
                                    sor[i] = true;
                                }
                                if(szomszed == 3){
                                    kovSejt[i][j]=true;
                                    sor[i]=true;
                                }else if(szomszed == 2){
                                    kovSejt[i][j] = sejtAllapot;
                                }else {
                                    kovSejt[i][j] = false;
                                }
                            }
                        }
                    }catch(ArrayIndexOutOfBoundsException aie){}
                }
                hasSetRowArray = true;
                aktSejt = kovSejt;
                panel.repaint();
            }
            Thread.sleep(sleepTime);
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }
    }
  
        int szomszed(int sor, int oszlop, int hely, boolean[][] sejt){
       int szomszed = 0;
       int felsoSor = sor - hely + 2, balOszlop = oszlop - hely + 2;
       
       for(int i = felsoSor; i < felsoSor + hely; i++){
           for(int j = balOszlop; j < balOszlop + hely; j++){
               if(!(i == sor && j ==  oszlop)){
                   try{
                       if(sejt[i][j])
                           szomszed++;
                   }catch (ArrayIndexOutOfBoundsException aie){}
                }
            }
        }
       return szomszed;
    }

        
void setUpGui() {
        JFrame frame = new JFrame("Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        panel = new DrawPanel();
        panel.setBackground(Color.white);
        panel.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent me) {
                int x = me.getX(), y = me.getY();
                boolean sejtAllapot = aktSejt[x / sejtMeret][y / sejtMeret];
                
                if(sejtAllapot){
                    sejtAllapot = false;
                } else {
                    sejtAllapot = true;
                }
                aktSejt[x / sejtMeret][y / sejtMeret] = sejtAllapot;
                frame.repaint();
            }
        });
        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "pause");
        panel.getActionMap().put("pause", new AbstractAction(){
            private static final long serialVersionUID = 1L;
            
            @Override
            public void actionPerformed(ActionEvent e){
                if(pause){
                    pause = false;
                } else {
                    hasSetRowArray = false;
                    pause = true;
                }
            }
    });
        
    frame.getContentPane().add(panel);
    frame.setVisible(true);
    frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    
    frameSize = new Dimension(frame.getWidth(), frame.getHeight());
    
    aktSejt = new boolean [(int) frameSize.getWidth()/ sejtMeret][(int) frameSize.getHeight() / sejtMeret];
    sor = new boolean[aktSejt.length];
    
    }



    
   
    class DrawPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.setColor(Color.gray);
            
            for(int i = 0; i< frameSize.getWidth(); i += sejtMeret){
                g2d.fillRect(i, 0, vonalMeret, (int) frameSize.getHeight());
                g2d.fillRect(0, i, (int) frameSize.getWidth(), vonalMeret);
            }
            g2d.setColor(Color.black);
            
            for(int i = 0, sorMeret = aktSejt.length; i < sorMeret; i++){
                for(int j = 0, oszlopMeret = aktSejt[i].length; j < oszlopMeret; j++){
                    g2d.setColor(Color.blue);
                    if(aktSejt[i][j])
                        g2d.fillRect(i * sejtMeret + vonalMeret, j * sejtMeret + vonalMeret, sejtMeret - vonalMeret, sejtMeret - vonalMeret); 
                }
            }
        }
    }
}
