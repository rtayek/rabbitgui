package com.tayek.tablet;
import static org.junit.Assert.*;
import java.io.*;
import java.net.InetAddress;
import java.util.Map;
import java.util.logging.Level;
import org.junit.*;
import com.tayek.*;
import com.tayek.Tablet.Type;
import com.tayek.io.LoggingHandler;
import com.tayek.tablet.Group.*;
import com.tayek.tablet.MessageReceiver.Model;
import com.tayek.tablet.io.*;
import static com.tayek.io.IO.*;
public class ControllerTestCase {
    @BeforeClass public static void setUpBeforeClass() throws Exception {
        LoggingHandler.init();
        group=new Group("1",new Groups().groups.get("g0"),Model.mark1);
        if(true) { // hack to get my ip address in group
            p("id to required: "+group.idToRequired);
            group.idToRequired.remove("192.168.0.100:33000");
            Required required=new Required("192.168.0.107",33000);
            group.idToRequired.put(required.id,required);
            p("id to required: "+group.idToRequired);
        } 
    }
    @AfterClass public static void tearDownAfterClass() throws Exception {}
    @Before public void setUp() throws Exception {
        
        tablet=Tablet.factory.create(Type.normal,group,group.keys().iterator().next(),group.getModelClone());
    }
    @After public void tearDown() throws Exception {}
    @Test public void test(){
        
    }
    
    @Test public void testController() throws InterruptedException, IOException {
        String input="s\nb 1\np\nh\na\na\nc\nc\ng\ng\nr\nt\nq\n";
        InputStream bais=new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        //System.out.println("ps="+ps);
        controller=new C2(group,bais,ps);
        controller.run();
        Thread.sleep(10);
        ps.flush();
        ps.close();
        assertTrue(baos.toString().contains("{TFFFFFFFFFF}"));
    }
    Tablet tablet;
    C2 controller;
    static Group group;
}
