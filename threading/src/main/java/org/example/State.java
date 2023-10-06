package org.example;

public class State {
    private Type nextType;

    public State(Type nextType) {
        this.nextType = nextType;
    }

    public Type getNextType() {
        return nextType;
    }

    public void setNextType(Type nextType) {
        this.nextType = nextType;
    }
}
