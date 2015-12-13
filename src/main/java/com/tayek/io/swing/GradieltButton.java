package com.tayek.io.swing;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
public class GradieltButton {
    public static void main(String[] args) {
        Object grad=UIManager.get("Button.gradient");
        List gradient;
        if(grad instanceof List) {
            gradient=(List)grad;
            System.out.println(gradient.get(0));
            System.out.println(gradient.get(1));
            System.out.println(gradient.get(2));
            System.out.println(gradient.get(3));
            System.out.println(gradient.get(4));
            //gradient.set(2, new ColorUIResource(Color.blue));
            //gradient.set(3, new ColorUIResource(Color.YELLOW));
            //gradient.set(4, new ColorUIResource(Color.GREEN));
            //gradient.set(2, new ColorUIResource(221, 232, 243));//origal Color
            //gradient.set(2, new ColorUIResource(255, 255, 255));//origal Color
            //gradient.set(2, new ColorUIResource(184, 207, 229));//origal Color
            gradient.set(2,new ColorUIResource(190,230,240));
            gradient.set(3,new ColorUIResource(240,240,240));
            gradient.set(4,new ColorUIResource(180,200,220));
            //UIManager.put("Button.background", Color.pink);
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new GradieltButton().makeUI();
            }
        });
    }
    public void makeUI() {
        JButton button=new JButton("Click");
        JFrame frame=new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(button);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
