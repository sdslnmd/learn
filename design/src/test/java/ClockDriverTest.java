import junit.framework.TestCase;

/**
 * User: sunluning
 * Date: 12-6-29 上午8:01
 */
public class ClockDriverTest extends TestCase {
    public ClockDriverTest(String name) {
        super(name);
    }

    public void testTimeChange() {
        MockTimeSource source=new MockTimeSource();
        MockTimeSink sink=new MockTimeSink();
        ClockDriver driver=new ClockDriver(source,sink);
        source.setTime(3,4,5);
        assertEquals(3,sink.getItsHours());
        assertEquals(4,sink.getItsMinutes());
        assertEquals(5,sink.getItsMinutes());
    }


}
