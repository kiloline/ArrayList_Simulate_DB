package Data.classes;
import java.time.*;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author rkppo
 */
public class Sysdate 
{
    public String getNowTime()
    {
        return LocalDateTime.now().toString();
    }
    private String getNowTime(String format)
    {
        LocalDateTime.of(0, Month.MARCH, 0, 0, 0, 0, 0);
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
    }
}
