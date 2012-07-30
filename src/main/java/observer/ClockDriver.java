package observer;

/**
 * Created by IntelliJ IDEA.
 * User: sunluning
 * Date: 12-6-28
 * Time: 上午7:59
 * To change this template use File | Settings | File Templates.
 */
public class ClockDriver {
    private TimeSink itsSink;

    public ClockDriver(TimeSource source,TimeSink sink) {
        source.setDriver(this);
        itsSink=sink;
    }

    public void update(int hours,int minutes,int seconds) {
        itsSink.setTime(hours,minutes,seconds);
    }


}
