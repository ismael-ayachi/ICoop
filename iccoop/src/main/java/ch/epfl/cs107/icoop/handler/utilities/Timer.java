package ch.epfl.cs107.icoop.handler.utilities;

/**
 * Timer handling countdown-based timing logic.
 * Provides methods to start, reset, and check the state of the timer.
 */
public class Timer {
    private int time;

    /**
     * Default constructor initializes the timer to zero.
     */
    public Timer() {
        time = 0;
    }

    /**
     * Constructor to initialize the timer with a specific starting value.
     * @param time (int): Initial countdown time
     */
    public Timer(int time) {
        this.time = time;
    }

    /**
     * Decreases the timer value by one if it is greater than zero.
     * Should be called periodically to update the timer state.
     */
    public void tick(){
        if (time>0) {
            time--;
        }
    }

    /**
     * Checks if the current time is a multiple of the specified interval.
     * Useful for triggering periodic events.
     *
     * @param i (int): Interval to check against
     * @return (boolean): True if the time is a multiple of the interval, false otherwise
     */
    public boolean interval(int i){
        return time%i==0;
    }

    /**
     * Starts the timer with a specified countdown value.
     * @param time (int): Countdown time to set
     */
    public void start(int time) {
        this.time = time;
    }

    /**
     * Resets the timer to zero, effectively stopping it.
     */
    public void reset(){
        time = 0;
    }

    /**
     * Checks if the timer is currently active (i.e., time > 0).
     *
     * @return (boolean): True if the timer is active, false otherwise
     */
    public boolean isGoing() {
        return time>0;
    }

    /**
     * Checks if the timer is inactive (i.e., time == 0).
     * @return (boolean): True if the timer is inactive, false otherwise
     */
    public boolean isOff() {
        return time == 0;
    }
}
