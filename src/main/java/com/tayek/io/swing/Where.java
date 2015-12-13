package com.tayek.io.swing;
import static com.tayek.tablet.io.IO.p;
import static java.lang.Math.*;
import java.awt.*;
import java.util.*;
public enum Where {
    top(BorderLayout.PAGE_START),bottom(BorderLayout.PAGE_END),right(BorderLayout.LINE_END),left(BorderLayout.LINE_START),center(BorderLayout.CENTER);
    Where(String k) {
        this.k=k;
    }
    public final String k;
    public Color color;
    public static void init() {
        for(Where where:values())
            where.color=Color.getHSBColor((float)(where.ordinal()*1./values().length),.9f,.9f);
    }
    public Character toCharacter() {
        return name().charAt(0);
    }
    public static Boolean[] from(Integer x) {
        x%=(int)round(pow(2,values().length));
        String string=Integer.toBinaryString(x);
        while(string.length()<values().length)
            string='0'+string;
        Boolean[] b=new Boolean[values().length];
        for(int i=0;i<values().length;i++)
            b[i]=string.charAt(i)=='1';
        p(Arrays.asList(b).toString());
        return b;
    }
    public static EnumSet<Where> set(Boolean[] bits) {
        EnumSet<Where> set=EnumSet.noneOf(Where.class);
        for(int i=0;i<values().length;i++)
            if(bits[i]) set.add(values()[i]);
        return set;
    }
}
