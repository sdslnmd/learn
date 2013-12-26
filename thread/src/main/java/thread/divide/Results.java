package thread.divide;

/**
 * User: sunluning
 * Date: 13-10-23 下午8:10
 */
public class Results {

    private int[] data;

    public Results(int size) {
        this.data = new int[size];
    }

    public void setData(int position, int value) {
        this.data[position] = value;
    }

    public int[] getData() {
        return data;
    }


}
