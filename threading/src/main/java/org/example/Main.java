package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        State state = new State(Type.ZERO);
        Base zero = new Base(2,50,0,Type.ZERO,Type.ODD,state);
        Base odd = new Base(2,50,1,Type.ODD,Type.EVEN,state);
        Base even = new Base(2,50,2,Type.EVEN,Type.ODD,state);

        Thread tZ = new Thread(zero,"zero");
        Thread tO = new Thread(odd,"odd");
        Thread tE = new Thread(even,"even");
        tZ.start();
        tO.start();
        tE.start();

        ExecutorService service = Executors.newFixedThreadPool(100);
//        for(int i ;i<100;i++){
//            service.execute(New Task());
//        }

    }
}