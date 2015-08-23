
import java.net.InetAddress;
import java.util.Date;
import java.util.concurrent.Callable;

public class pingCal implements Callable<Integer> {

    private String _ip;
    private int _wait;

    public pingCal(String ip, int wait) {
        _ip = ip;
        _wait = wait;
    }

    @Override
    public Integer call() throws Exception {
        try {
            Date start = new Date();
            InetAddress inet = InetAddress.getByName(_ip);
            Boolean res = inet.isReachable(_wait);
            Date stop = new Date();
            if (res) {
                return (int)(stop.getTime() - start.getTime());
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
