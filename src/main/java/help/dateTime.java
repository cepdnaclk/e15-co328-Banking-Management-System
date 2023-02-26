
package help;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author RISITH-PC
 */
public class dateTime {
    private final String nowDate;
    private final String nowTime;
    private final Date currentDate;
    
    public dateTime() {
        currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        nowDate = dateFormat.format(currentDate);
        nowTime = timeFormat.format(currentDate);
    }

    public String getNowDate() {
        return nowDate;
    }

    public String getNowTime() {
        return nowTime;
    }
    
}
