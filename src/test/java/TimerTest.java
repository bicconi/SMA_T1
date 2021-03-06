import org.junit.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Calendar;

import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void requestTimerTime() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Timer timer = new Timer();
        timer.requestTimerTime(); // [status] 0: Stopped -> 2: Setting
        assertEquals(2, timer.getStatus());
    }

    @Test
    public void requestNextTimerTimeSection() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        Timer timer = new Timer();
        timer.setCurrSection(2); // [currSection] 0: Second -> 2: Hour / Last Section
        timer.requestNextTimerTimeSection(); // [currSection] 2: Hour -> 0: Second
        timer.requestNextTimerTimeSection(); // [currSection] 0: Second -> 1: Minute
        assertEquals(1, timer.getCurrSection()); // [currSection] 1: Minute
    }

    @Test
    public void requestIncreaseTimerTimeSection() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Timer timer = new Timer();

        // 1: Max Second Increase Test
        timer.getRsvTime().set(Calendar.SECOND, 59);

        timer.requestIncreaseTimerTimeSection(); // 59 -> 0
        timer.requestIncreaseTimerTimeSection(); // 0 -> 1

        assertEquals(0, timer.getRsvTime().get(Calendar.MILLISECOND));
        assertEquals(1, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(0, timer.getRsvTime().get(Calendar.MINUTE));

        // 2: Max Minute Increase Test
        timer.setCurrSection(1);
        timer.getRsvTime().set(Calendar.MINUTE, 59);

        timer.requestIncreaseTimerTimeSection(); // 59 -> 0
        timer.requestIncreaseTimerTimeSection(); // 0 -> 1

        assertEquals(1, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(1, timer.getRsvTime().get(Calendar.MINUTE));
        assertEquals(0, timer.getRsvTime().get(Calendar.HOUR_OF_DAY));

        // 3: Max HOUR Increase Test
        timer.setCurrSection(2);
        timer.getRsvTime().set(Calendar.HOUR_OF_DAY, 23);

        timer.requestIncreaseTimerTimeSection(); // 23 -> 24(0)
        timer.requestIncreaseTimerTimeSection(); // 0 -> 1

        assertEquals(1, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(1, timer.getRsvTime().get(Calendar.MINUTE));
        assertEquals(1, timer.getRsvTime().get(Calendar.HOUR_OF_DAY));
        assertEquals(1, timer.getRsvTime().get(Calendar.DATE));
    }

    @Test
    public void requestDecreaseTimerTimeSection() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Timer timer = new Timer();
        Calendar testTime = Calendar.getInstance();
        testTime.set(1970, Calendar.JANUARY, 1, 22, 37, 0);
        testTime.set(Calendar.MILLISECOND, 0);
        timer.setRsvTime((Calendar)testTime.clone()); // Avoid Call-by-Reference

        // 1: Min Second Decrease Test
        timer.requestDecreaseTimerTimeSection(); // 0 -> 59
        timer.requestDecreaseTimerTimeSection(); // 59 -> 58

        assertEquals(0, timer.getRsvTime().get(Calendar.MILLISECOND));
        assertEquals(58, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(37, timer.getRsvTime().get(Calendar.MINUTE));
        assertEquals(22, timer.getRsvTime().get(Calendar.HOUR_OF_DAY));

        // 2: Min Minute Decrease Test
        timer.setCurrSection(1);
        timer.getRsvTime().set(Calendar.MINUTE, 0);
        timer.requestDecreaseTimerTimeSection(); // 0 -> 59
        timer.requestDecreaseTimerTimeSection(); // 59 -> 58

        assertEquals(0, timer.getRsvTime().get(Calendar.MILLISECOND));
        assertEquals(58, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(58, timer.getRsvTime().get(Calendar.MINUTE));
        assertEquals(22, timer.getRsvTime().get(Calendar.HOUR_OF_DAY));

        // 3: Max Minute Increase Test
        timer.setCurrSection(2);
        timer.getRsvTime().set(Calendar.HOUR_OF_DAY, 0);
        timer.requestDecreaseTimerTimeSection(); // 0 -> 23
        timer.requestDecreaseTimerTimeSection(); // 23 -> 22

        assertEquals(0, timer.getRsvTime().get(Calendar.MILLISECOND));
        assertEquals(58, timer.getRsvTime().get(Calendar.SECOND));
        assertEquals(58, timer.getRsvTime().get(Calendar.MINUTE));
        assertEquals(22, timer.getRsvTime().get(Calendar.HOUR_OF_DAY));
        assertEquals(1, timer.getRsvTime().get(Calendar.DATE));
    }

    @Test
    public void changeStatus() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Timer timer = new Timer();
        timer.changeStatus(1); // [status] 0: Stopped -> 1: Continued
        assertEquals(1, timer.getStatus()); // [status] 1: Continued

        timer.changeStatus(-1); // [status] 1: Continued -> -1: Not valid
        assertEquals(1, timer.getStatus()); // [status] 1: Continued
    }

    @Test
    public void requestResetTimer() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Timer timer = new Timer();
        Calendar testTime = Calendar.getInstance();
        testTime.set(1970, Calendar.JANUARY, 1, 22, 34, 30);
        testTime.set(Calendar.MILLISECOND, 0);

        // [rstTime] 1970. Calendar.JANUARY. 1. 00. 00. 000 -> 1970. Calendar.JANUARY. 1. 22. 34. 30. 000
        timer.setRsvTime((Calendar)testTime.clone()); // Avoid Call-By-Reference

        // [timerTime] 1970. Calendar.JANUARY. 1. 00. 00. 000 -> 1970. Calendar.JANUARY. 1. 22. 34. 30. 000
        timer.requestResetTimer();

        assertEquals(timer.getRsvTime(), timer.getTimerTime()); // [rsvTime] == [timerTime]
    }

    @Test
    public void ringOff() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Timer timer = new Timer();
        timer.setStatus(3); // [status] 0: Stopped -> 3: Ringing
        timer.ringOff(); // [status] 3: Ringing -> 0: Stopped
        assertEquals(0, timer.getStatus()); // [status] 0: Stopped
    }

    @Test
    public void realTimeTimerTask() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Timer timer = new Timer();
        timer.getRsvTime().set(1970, 1-1, 1, 10, 30, 12);
        timer.getRsvTime().set(Calendar.MILLISECOND, 0);

        timer.requestResetTimer();
        assertEquals(timer.getRsvTime().getTimeInMillis(), timer.getTimerTime().getTimeInMillis());

        timer.changeStatus(1); // [Status] 0: Stopped -> 1: Continued
        timer.realTimeTimerTask();
        assertEquals(timer.getRsvTime().getTimeInMillis() - 10, timer.getTimerTime().getTimeInMillis());

        // Decrease timerTime to 00:00:00.000
        while(timer.getTimerTime().getTimeInMillis() > -32400000)
            timer.realTimeTimerTask();

        // [status] 1: Continued -> 3: Ringing
        assertEquals(3, timer.getStatus()); // [status] 3: Ringing
    }

    /*
    @Test
    public void showTimer() {}
    */

    @Test
    public void requestExitSetTimerTime() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Timer timer = new Timer();

        timer.changeStatus(2); // [status] 0: Stopped -> 2: Setting
        timer.requestNextTimerTimeSection(); // [currSection] 0: Second -> 1: Minute

        // [status] 2: Setting -> 0: Stopped / [currSection] 1: Minute -> 0: Second
        timer.requestExitSetTimerTime();

        assertEquals(0, timer.getStatus()); // [status] 0: Stopped
        assertEquals(0, timer.getCurrSection()); // [currSection] 0: Second
    }
}