package com.tayek.tablet;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import com.tayek.tablet.Main2;
import com.tayek.tablet.io.*;
import com.tayek.tablet.io.swing.Swing;
import static com.tayek.tablet.io.IO.*;
import com.tayek.utilities.*;
// http://cs.nyu.edu/~yap/prog/cygwin/FAQs.html
// http://poppopret.org/2013/01/07/suterusu-rootkit-inline-kernel-function-hooking-on-x86-and-arm/
// http://angrytools.com/android/
public class Main2 { // http://steveliles.github.io/invoking_processes_from_java.html
    public static class Run2 {
        public static void main(String[] arguments)  {
            Swing.run2();
        }
        
    }
    public static void main(String[] arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
        new Dispatcher(arguments) {
            {
                while(entryPoints.size()>0)
                    remove(1);
                add(Tablet.class);
                add(Swing.class);
                add(LogServer.class);
            }
        }.run();
    }
}
