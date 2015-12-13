package com.tayek.io.swing;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import com.tayek.io.swing.*;
import static com.tayek.tablet.io.IO.*;
import static java.lang.Math.*;
// shapes, colors, styles, arrangement
public class Arranger extends MainGui {
    Arranger(MyJApplet applet) {
        super(applet);
    }
    @Override public void initialize() {
        // TODO Auto-generated method stub
    }
    @Override public String title() {
        return "Arrangements";
    }
    @Override public void addContent() {
        JPanel grid=new JPanel();
        int n=(int)ceil(sqrt(pow(2,Where.values().length)));
        p("n="+n);
        GridLayout experimentLayout=new GridLayout(n,n);
        grid.setLayout(experimentLayout);
        for(int i=0;i<(int)pow(2,Where.values().length);i++) {
            JPanel panel=makeArrangement(Where.set(Where.from(i)));
            panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            grid.add(panel,i);
            // panel.setPreferredSize(new Dimension(800,600));
        }
        add(grid);
    }
    JPanel makeArrangement(Set<Where> set) {
        JPanel panel=new JPanel();
        panel.setLayout(new BorderLayout());
        for(Where where:set) {
            JButton button=new JButton();
            button.setBackground(where.color);
            panel.add(button,where.k);
        }
        return panel;
    }
    void run_() {
        p(((Double)pow(2,Where.values().length)).toString());
        for(int i=0;i<(int)pow(2,Where.values().length);i++) {
            EnumSet<Where> set=Where.set(Where.from(i));
            p(set.toString());
            makeArrangement(set);
        }
    }
    public void run() {
        setLayout(new BorderLayout());
        initialize();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(isApplet()) addContent();
                else {
                    frame.setTitle(title());
                    frame.getContentPane().add(Arranger.this,BorderLayout.CENTER);
                    addContent();
                    frame.pack();
                    frame.setVisible(true);
                }
            }
        });
    }
    public static void main(String[] args) {
        Where.init();
        Arranger arranger=new Arranger(null);
        arranger.run_();
        arranger.run();
    }
    private static final long serialVersionUID=1L;
}
