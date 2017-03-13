package com.tayek.tablet;
import static com.tayek.io.IO.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutionException;
import com.tayek.*;
import com.tayek.io.LoggingHandler;
import com.tayek.tablet.Group.Groups;
import com.tayek.tablet.MessageReceiver.Model;
import com.tayek.tablet.io.swing.Swing;
public class C2 extends Controller {
    C2(Group group) {
        super(group,false);
    }
    C2(Group group,InputStream in,PrintStream out) {
        super(group,false,in,out);
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
                    newGui=Swing.createGui(tablet);
                    tablet.model().addObserver(newGui);
                } else {
                    tablet.model().deleteObserver(newGui);
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
        LoggingHandler.init();
        //LoggingHandler.setLevel(Level.OFF);
        Group group=new Group("1",new Groups().groups.get("g0"),Model.mark1);
        if(true) { // hack to get my ip address in group
            p("id to required: "+group.idToRequired);
            group.idToRequired.remove("192.168.0.100:33000");
            Required required=new Required("192.168.0.107",33000);
            group.idToRequired.put(required.id,required);
            p("id to required: "+group.idToRequired);
        } 
        new C2(group).run();
    }
    private Swing newGui;
}
