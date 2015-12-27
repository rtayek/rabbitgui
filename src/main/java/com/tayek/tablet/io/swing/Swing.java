package com.tayek.tablet.io.swing;
import static com.tayek.tablet.io.IO.*;
import static java.lang.Math.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.tayek.io.*;
import com.tayek.io.swing.*;
import com.tayek.tablet.*;
import com.tayek.tablet.Tablet.MenuItem;
import com.tayek.tablet.io.*;
import com.tayek.tablet.io.IO.GetNetworkInterfacesCallable;
public class Swing extends MainGui implements View,ActionListener {
    // http://www.javaknowledge.info/android-like-toast-using-java-swing/
    private Swing(Tablet tablet) {
        super();
        this.tablet=tablet;
        buttons=new AbstractButton[colors.n];
    }
    @Override public JFrame frame() {
        @SuppressWarnings("serial") JFrame frame=new JFrame() {
            @Override public void dispose() { // dispose of associated text view
                if(textView!=null) textView.frame.dispose();
                super.dispose();
            }
        };
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }
    @Override public void initialize() { // as much as we can/need off the awt/edt thread
        textView=TextView.addTextView("Tablet "+tablet.tabletId());
        if(textView!=null) textView.frame.setVisible(false);
    }
    @Override public String title() {
        return "Buttons";
    }
    @Override public void addContent() {
        JMenuBar jMenuBar=createMenuBar();
        frame.setJMenuBar(jMenuBar);
        JPanel top=new JPanel();
        JLabel topLabel=new JLabel(tablet.name());
        Font current=topLabel.getFont();
        // p(topLabel.getFont().toString());
        add(top,BorderLayout.PAGE_START);
        buildCenter(buttonSize);
        Font small=new Font(current.getName(),current.getStyle(),2*current.getSize()/3);
        topLabel.setFont(small);
        top.add(topLabel);
        JPanel bottom=new JPanel();
        JLabel bottomLabel=new JLabel("bottom");
        bottomLabel.setFont(small);
        bottom.add(bottomLabel);
        add(bottom,BorderLayout.PAGE_END);
        add(new JLabel("left"),BorderLayout.LINE_START);
        add(new JLabel("right"),BorderLayout.LINE_END);
        frame.getContentPane().addHierarchyBoundsListener(hierarchyBoundsListener);
    }
    void buildCenter(int size) {
        JPanel center=new JPanel();
        center.setBackground(new Color(colors.background));
        EmptyBorder e=new EmptyBorder(10,10,10,10);
        JPanel box=new JPanel();
        box.setBackground(new Color(colors.background));
        box.setLayout(new BoxLayout(box,BoxLayout.LINE_AXIS));
        EmptyBorder e2=new EmptyBorder(size*12/10,size,size*12/10,size);
        box.setBorder(e2);
        Insets x=box.getInsets();
        // p(x.toString());
        JPanel left=new JPanel();
        GridLayout grid=new GridLayout(colors.rows,colors.columns,size*2/10,size*2/10);
        // left.setBorder(BorderFactory.createLineBorder(Color.red));
        left.setLayout(grid);
        left.setBackground(new Color(colors.background));
        // Font sans=new Font(Font.SANS_SERIF,Font.PLAIN,size*6/10);
        // Font serif=new Font(Font.SERIF,Font.PLAIN,size*4/10);
        // p("serif size: "+serif.getSize());
        actionListener=new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof AbstractButton) {
                    AbstractButton button=(AbstractButton)e.getSource();
                    String name=button.getName(); // name starts at zero
                    Integer index=Integer.valueOf(name);
                    guiAdapter.processClick(index);
                }
            }
        };
        Font font=null;
        for(int i=0;i<colors.rows*colors.columns;i++) {
            JButton button=new JButton();
            button.setText(tablet.getButtonText(i+1));
            button.setName(""+i); // name starts at zero, text starts at one!
            left.add(button,i);
            button.addActionListener(actionListener);
            button.setBackground(new Color(colors.color(i,false)));
            button.setPreferredSize(new Dimension(size,size));
            if(i==0) {
                font=button.getFont();
                int fontsize=font.getSize();
                font=new Font(font.getFontName(),font.getStyle(),3*fontsize);
            }
            button.setFont(font);
            buttons[i]=button;
            buttons[i].setFont(font);
        }
        box.add(left);
        JPanel middle=new JPanel();
        middle.setLayout(new BoxLayout(middle,BoxLayout.PAGE_AXIS));
        // middle.setBorder(BorderFactory.createLineBorder(Color.blue));
        middle.setPreferredSize(new Dimension(size*8/10,2*size));
        JButton small=new JButton();
        small.setPreferredSize(new Dimension(size/2,size/2));
        // middle.add(small);
        box.add(middle);
        JPanel right=new JPanel();
        right.setBackground(new Color(colors.background));
        // right.setLayout(null);
        // right.setLayout(new BoxLayout(right,BoxLayout.PAGE_AXIS));
        right.setLayout(new GridLayout(colors.rows,1,10,10));
        // right.setBorder(BorderFactory.createLineBorder(Color.green));
        JButton button=new JButton();
        button.setText(tablet.getButtonText(tablet.model.resetButtonId));
        button.setBackground(new Color(colors.color(colors.rows*colors.columns,false)));
        buttons[colors.rows*colors.columns]=button;
        button.setText("R");
        button.setFont(font);
        button.setName(""+(colors.rows*colors.columns));
        button.addActionListener(actionListener);
        button.setMinimumSize(new Dimension(size,size));
        button.setPreferredSize(new Dimension(size,size));
        button.setMaximumSize(new Dimension(size,size));
        right.add(button,0);
        right.setPreferredSize(new Dimension(size,2*size));
        // button.setFont(sans);
        box.add(right);
        center.add(box);
        add(center,Where.center.k);
    }
    ActionListener actionListener;
    @Override public void update(Observable o,Object hint) {
        if(o instanceof Model&&o.equals(tablet.model)) guiAdapter.update(o,hint);
        else l.warning("not a model or not our model!");
    }
    @Override public void actionPerformed(ActionEvent e) {
        l.info("action performed: "+e);
        MenuItem x=MenuItem.valueOf(e.getActionCommand());
        if(x!=null) {
            if(x.equals(MenuItem.Log)) { // no text view on android
                if(textView!=null) textView.frame.setVisible(!textView.frame.isVisible());
                else l.info("no log window to toggle!");
            } else x.doItem(tablet);
        } else l.info("action not handled: "+e.getActionCommand());
    }
    public JMenuBar createMenuBar() {
        JMenuBar menuBar=new JMenuBar();
        JMenu menu=new JMenu("Options");
        menu.setMnemonic(KeyEvent.VK_O);
        menu.getAccessibleContext().setAccessibleDescription("Options menu");
        // Reset,Ping,Disconnect,Connect,Log;
        JMenuItem menuItem=null;
        for(MenuItem x:MenuItem.values()) {
            menuItem=new JMenuItem(x.name());
            int vk=(KeyEvent.VK_A-1)+(x.name().toUpperCase().charAt(0)-'A');
            menuItem.setAccelerator(KeyStroke.getKeyStroke(vk,ActionEvent.ALT_MASK));
            menuItem.getAccessibleContext().setAccessibleDescription(x.name());
            menuItem.addActionListener(this);
            menu.add(menuItem);
        }
        if(false) {
            menuItem=new JMenuItem("Log",KeyEvent.VK_C);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,ActionEvent.ALT_MASK));
            menuItem.getAccessibleContext().setAccessibleDescription("Log");
            menuItem.addActionListener(this);
            menu.add(menuItem);
        }
        menuBar.add(menu);
        return menuBar;
    }
    static GuiAdapterABC create(final Tablet tablet,final Swing gui) {
        GuiAdapterABC adapter=new GuiAdapterABC(tablet) {
            @Override public void setButtonText(final int id,final String string) {
                final Integer index=id-1; // model uses 1-n
                if(SwingUtilities.isEventDispatchThread()) {
                    gui.buttons[index].setText(string);
                } else SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        gui.buttons[index].setText(string);
                    }
                });
            }
            @Override public void setButtonState(final int id,final boolean state) {
                final Integer index=id-1; // model uses 1-n
                if(SwingUtilities.isEventDispatchThread()) {
                    gui.buttons[index].setSelected(state);
                    gui.buttons[index].setBackground(new Color(gui.colors.color(index,state)));
                } else SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        gui.buttons[index].setSelected(state);
                        gui.buttons[index].setBackground(new Color(gui.colors.color(index,state)));
                    }
                });
            }
        };
        return adapter;
    }
    public static Swing create(final Tablet tablet) { // subclass instead
        final Swing gui=new Swing(tablet);
        gui.guiAdapter=create(tablet,gui);
        gui.run();
        return gui;
    }
    public static void run2() {}
    public static void main(String[] argumentss) throws Exception {
        // System.gc();
        LoggingHandler.loggers.add(Swing.class);
        LoggingHandler.setLevel(Level.OFF);
        // looks like we can't run with the real tablets anymore :(
        // this is a routing problem mostly
        // we should be able to run on the laptop just fine.
        if(true) {
            InetAddress inetAddress=null;
            try {
                inetAddress=IO.runAndWait(new GetNetworkInterfacesCallable(IO.defaultNetworkPrefix));
                // could check for more subnets provided group info stored the entire ip address.
                // what about the log server host address?
            } catch(Exception e) {}
            if(inetAddress==null) {
                p("quiting, can not find inet address!");
                return;
            }
            p("found inetAddress: "+inetAddress);
            Group group=new Group(1,Group.groups.get("g0"));
            Tablet tablet=group.getTablet(inetAddress,null);
            p("tablet: "+tablet);
            tablet.model.addObserver(create(tablet));
            tablet.model.addObserver(new AudioObserver(tablet.model));
            tablet.group.io.startListening(tablet);
        } else {
            Group group=new Group(1,Group.groups.get("g2"));
            for(Iterator<Integer> i=group.tablets().iterator();i.hasNext();) {
                int tabletId=i.next();
                group=new Group(1,Group.groups.get("g2"));
                Tablet tablet=new Tablet(group,tabletId);
                tablet.model.addObserver(create(tablet));
                tablet.model.addObserver(new AudioObserver(tablet.model));
                tablet.group.io.startListening(tablet);
            }
        }
    }
    final Colors colors=new Colors();
    final Tablet tablet;
    final AbstractButton[] buttons;
    int buttonSize=100;
    public /* final */ GuiAdapterABC guiAdapter;
    TextView textView;
    HierarchyBoundsListener hierarchyBoundsListener=new HierarchyBoundsListener() {
        @Override public void ancestorMoved(HierarchyEvent e) {
            // p(e.toString());
        }
        @Override public void ancestorResized(HierarchyEvent e) {
            if(e.getID()==HierarchyEvent.ANCESTOR_RESIZED) {
                Dimension d=frame.getContentPane().getSize();
                buttonSize=d.width/10;
            }
        }
    };
    public final Logger l=Logger.getLogger(getClass().getName());
    private static final long serialVersionUID=1L;
}
