package ch.epfl.cs107.icoop.handler;

public class Timer {
    private int time;

    public Timer() {
        time = 0;
    }

    public Timer(int time) {
        this.time = time;
    }

    public void tick(){
        if (time>0) {
            time--;
        }
    }

    public boolean interval(int i){
        return time%i==0;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void reset(){
        time = 0;
    }

    public boolean isGoing() {
        return time>0;
    }

    public boolean isOff() {
        return time == 0;
    }
}
