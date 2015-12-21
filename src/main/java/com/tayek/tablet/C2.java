package com.tayek.tablet;
import static com.tayek.tablet.io.IO.p;
import java.io.*;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import com.tayek.tablet.io.swing.Swing;
public class C2 extends Controller {
    C2(Tablet tablet) {
        super(tablet);
    }
    C2(Tablet tablet,InputStream in,PrintStream out) {
        super(tablet,in,out);
    }

    @Override protected void help() {
        p(out,"g - add/remove a gui");
        super.help();
    }
    @Override protected boolean process(String command) {
        if(command.length()==0) return true;
        String[] tokens=null;
        switch(command.charAt(0)) {
            case 'g':
                if(newGui==null) {
                    newGui=Swing.create(tablet);
                    tablet.model.addObserver(newGui);
                } else {
                    tablet.model.deleteObserver(newGui);
                    newGui.frame.dispose();
                    newGui=null;
                }
                break;
            default:
                return super.process(command);
        }
        return true;
    }
    public static void main(String[] arguments) throws UnknownHostException,InterruptedException,ExecutionException {
        Tablet tablet=initialize(arguments);
        new C2(tablet).run();
    }
    private Swing newGui;
}
