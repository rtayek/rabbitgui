package com.tayek.tablet;
import static com.tayek.tablet.io.IO.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import com.tayek.tablet.io.*;
import com.tayek.tablet.io.swing.Swing;
public class Controller {
    Controller(Tablet tablet) {
        this(tablet,System.in,System.out);
    }
    Controller(Tablet tablet,InputStream in,PrintStream out) {
        this.tablet=tablet;
        this.in=in;
        this.out=out;
    }
    private void usage() {
        p(out,"usage:");
        p(out,"a add/remove audio observer");
        p(out,"b <buttonId> <boolean> - set button state");
        p(out,"c - add/remove a command line view");
        p(out,"g - add/remove a gui");
        p(out,"p - print view");
        p(out,"q - quit");
        p(out,"r - reset");
        p(out,"s - start client");
        p(out,"t - stop client");
    }
    private String[] splitNext(String command,int i) {
        while(command.charAt(i)==' ')
            i++;
        String[] tokens=command.substring(i).split(" ");
        return tokens;
    }
    boolean process(String command) {
        if(command.length()==0) return true;
        String[] tokens=null;
        switch(command.charAt(0)) {
            case 'h':
                usage();
                break;
            case 'a':
                if(audioObserver==null) {
                    audioObserver=new AudioObserver(tablet.model);
                    tablet.model.addObserver(audioObserver);
                } else {
                    tablet.model.deleteObserver(audioObserver);
                    audioObserver=null;
                }
                break;
            case 'b':
                if(command.charAt(1)==' ') {
                    tokens=splitNext(command,2);
                    if(tokens.length==2) try {
                        int buttonId=Integer.valueOf(tokens[0]);
                        boolean state=Boolean.valueOf(tokens[1]);
                        tablet.model.setState(buttonId,state);
                    } catch(Exception e) {
                        p(out,"controller split caught: "+e);
                        p(out,"syntax error: "+command);
                    }
                    else p(out,"too many tokens!");
                } else p(out,"syntax error: "+command);
                break;
            case 'o': // send start form foreign group
                // tablet.send(Message.dummy,0);
                break;
            case 'c':
                if(commandLineView==null) {
                    commandLineView=new View.CommandLine(tablet.model);
                    tablet.model.addObserver(commandLineView);
                    p(out,"added command line view: "+commandLineView);
                } else {
                    tablet.model.deleteObserver(commandLineView);
                    p(out,"removed command line view: "+commandLineView);
                    commandLineView=null;
                }
                break;
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
            case 'p':
                p(out,tablet.model.toString());
                break;
            case 'r':
                tablet.model.reset();
                break;
            case 's':
                boolean ok=tablet.group.io.startListening(tablet);
                if(!ok) p(out,"badness");
                break;
            case 't':
                tablet.group.io.stopListening(tablet);
                break;
            case 'q':
                return false;
            default:
                p(out,"unimplemented: "+command.charAt(0));
                usage();
                break;
        }
        return true;
    }
    void run() {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
        String string=null;
        System.out.println(out+" "+System.out);
        usage();
        prompt();
        try {
            while((string=bufferedReader.readLine())!=null) {
                if(!process(string)) {
                    p(out,"quitting.");
                    return;
                }
                prompt();
            }
        } catch(IOException e) {
            p(out,"controller readln caught: "+e);
            p(out,"quitting.");
            return;
        }
        p(out,"end of file.");
    }
    void prompt() {
        out.print(lineSeparator+">");
        out.flush();
    }
    public static void main(String[] arguments) throws UnknownHostException, InterruptedException, ExecutionException {
        LoggingHandler.init();
        String host=InetAddress.getLocalHost().getHostName();
        p("host: "+host);
        InetAddress inetAddress=IO.runAndWait(new GetNetworkInterfacesCallable(host));
        Group group=new Group(inetAddress,1,Group.groups.get("g2"));
        Tablet tablet=group.getTablet(InetAddress.getByName(host));
        new Controller(tablet).run();
    }
    private final Tablet tablet;
    private final InputStream in;
    private final PrintStream out;
    private View.CommandLine commandLineView;
    private Observer audioObserver;
    private Swing newGui;
    public static final String lineSeparator=System.getProperty("line.separator");
}
