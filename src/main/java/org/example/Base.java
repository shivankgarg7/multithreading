package org.example;

public class Base implements Runnable{
    private final int step;
    private final int maxN;
    private int currentValue;
    private final Type currentType;
    private final Type nextType;

    private final State state;




    public Base(int step, int maxN, int currentValue, Type currentType, Type nextType, State state){
        this.step = step;
        this.maxN = maxN;
        this.currentValue = currentValue;
        this.currentType = currentType;
        this.nextType = nextType;
        this.state = state;
    }


    @Override
    public void run() {
        while(currentValue < maxN){
            synchronized (state){
                while(currentType != state.getNextType()){
                    try {
                        state.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(currentValue + " " + currentType + " " + Thread.currentThread().getName());
                currentValue += step;
                state.setNextType(this.nextType);
                state.notifyAll();
            }

        }

    }
}
