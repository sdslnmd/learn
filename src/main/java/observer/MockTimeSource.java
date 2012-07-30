package observer;

/**
 * User: sunluning
 * Date: 12-6-28
 * Time: 下午11:25
 */
public class MockTimeSource implements TimeSource{
    private ClockDriver itsDriver;

    public void setTime(int hours, int minutes, int seconds) {
        itsDriver.update(hours,minutes,seconds);
    }

    public void setDriver(ClockDriver driver) {
        itsDriver=driver;
    }
}
