package com.henryshaw;

import org.junit.Test;

public class ARC4Test {
    
    @Test
    public void DoubleTest(){
        
        ARC4 arc4 = new ARC4();
        arc4.init("test.\0".getBytes());
        System.out.println(arc4.nextDouble());
        System.out.println(arc4.nextDouble());
        System.out.println(arc4.nextDouble());
        System.out.println(arc4.nextDouble());
    }

    @Test
    public void IntTest(){
        ARC4 arc4 = new ARC4("test.\0");
        System.out.println(arc4.nextInt());
        System.out.println(arc4.nextInt());
        System.out.println(arc4.nextInt());
        System.out.println(arc4.nextInt());

        System.out.println(arc4.nextInt(3));
        System.out.println(arc4.nextInt(3));
    }
}
