package com.tayek.tablet;
import static org.junit.Assert.*;
import java.io.*;
import java.net.InetAddress;
import java.util.Map;
import java.util.logging.Level;
import org.junit.*;
import com.tayek.tablet.Group.Info;
import com.tayek.tablet.io.*;
import com.tayek.tablet.io.IO.GetNetworkInterfacesCallable;
import static com.tayek.tablet.io.IO.*;
public class ControllerTestCase {
    @BeforeClass public static void setUpBeforeClass() throws Exception {
        LoggingHandler.init();
        group=new Group(1,Group.groups.get("g2"));
    }
    @AfterClass public static void tearDownAfterClass() throws Exception {}
    @Before public void setUp() throws Exception {
        tablet=new Tablet(group,group.tablets().iterator().next());
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
        controller=new C2(tablet,bais,ps);
        controller.run();
        Thread.sleep(10);
        ps.flush();
        ps.close();
        assertTrue(baos.toString().contains("{TFFFFFFFFFF}"));
    }
    Tablet tablet;
    C2 controller;
    static Group group;
    static Map<Integer,Info> info;
}
