/* WatchSystem Class */

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WatchSystem {


    private ArrayList menu;
    private int currMode;
    private int maxCnt;
    private WatchGUI watchGUI;

    public WatchSystem(){
        this.menu = new ArrayList(){};

        menu.add(new ModeSetting(this));
        menu.add(new RealTime());
        menu.add(new SettingTime((RealTime)menu.get(1)));
        menu.add(new Stopwatch());

        /* [sonarqube][Bug #9] */
        try{

            menu.add(new Timer());
            menu.add(new Alarm((RealTime)menu.get(1)));
        }
        catch(Exception e) {
            /* [sonarqube][Use a logger to log this exception] */
            Logger logger = Logger.getLogger(WatchSystem.class.getName());
            logger.log(Level.ALL, "context", e);
            return;
        }

        this.currMode = 1; // [currMode] 1: Always RealTime
        this.maxCnt = 4;

        watchGUI = new WatchGUI(this);
        watchGUI.setMode(menu.get(1));
        watchGUI.designMode(true);
    }

    // [WatchSystem] System Method
    // Worked by thread
    public void realTimeTask() {
        if(this.currMode == 0)
            watchGUI.realtimeGUI(((ModeSetting) menu.get(0)).showModeSetting());

        for (int i = 1; i <= this.maxCnt + 1; i++) {
            Object menu;
            if((menu = this.menu.get(i)) == null) continue;
            switch (menu.getClass().getTypeName()) {
                case "RealTime":
                    ((RealTime) this.menu.get(1)).calculateTime();
                    if (this.currMode == i) watchGUI.realtimeGUI(((RealTime) menu).showRealTime());
                    break;

                case "SettingTime":
                    // ((SettingTime) menu).realTimeTaskSettingTime();
                    if (this.currMode == i) watchGUI.realtimeGUI(((SettingTime) menu).showSettingTime());
                    break;

                case "Stopwatch":
                    ((Stopwatch) menu).realTimeTaskStopwatch();
                    if (this.currMode == i) watchGUI.realtimeGUI(((Stopwatch) menu).showStopwatch());
                    break;

                case "Timer":
                    ((Timer) menu).realTimeTimerTask();
                    if (this.currMode == i) watchGUI.realtimeGUI(((Timer) menu).showTimer());
                    break;

                case "Alarm":
                    ((Alarm) menu).realTimeTaskAlarm();
                    if (this.currMode == i) watchGUI.realtimeGUI(((Alarm) menu).showAlarm());
                    break;

                case "Worldtime":
                    ((Worldtime) menu).realTimeTaskWorldtime();
                    if (this.currMode == i) watchGUI.realtimeGUI(((Worldtime) menu).showWorldTime());
                    break;

                case "Sun":
                    ((Sun) menu).realTimeTaskSun();
                    if (this.currMode == i) watchGUI.realtimeGUI(((Sun) menu).showSun());
                    break;

                default:
                    break;
            }
        }
    }

    public void pressChangeMode() {
        watchGUI.designMode(false);
        if(++this.currMode == this.maxCnt + 2)
            this.currMode = 1; // 1: RealTime
        watchGUI.setMode(menu.get(this.currMode));
        watchGUI.designMode(true);
    }

    // RealTime
    public void pressShowType() { ((RealTime)this.menu.get(1)).requestChangeType();}

    // Mode Setting
    public void enterModeSetting(){
        watchGUI.designMode(false);
        this.currMode = 0;
        ((ModeSetting)this.menu.get(0)).requestModeSetting();
        watchGUI.setMode(menu.get(this.currMode));
        watchGUI.designMode(true);
    }

    public void pressNextMode()  { ((ModeSetting)this.menu.get(0)).requestNextMode(); }
    public void pressSelectMode(){ ((ModeSetting)this.menu.get(0)).requestSelectMode(); }
    public void pressConfirmSelectMode() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        ArrayList newList = ((ModeSetting)this.menu.get(0)).confirmSelectMode();
        this.maxCnt = 0;
        for(int i = 0; i < 4; i++) {
            try{
                this.maxCnt++;
                this.menu.set(i+2, newList.get(i));
            }
            /* [sonarqube][Catching 'Exception' is not allowed.] */
            catch(IndexOutOfBoundsException e){
                this.maxCnt--;
                this.menu.set(i+2, null);
            }
        }
        this.pressChangeMode();
    }

    // Setting Time
    public void nextTimeSection()    { ((SettingTime)this.menu.get(this.currMode)).requestPointNextTimeSection(); }
    public void increaseTimeSection(){ ((SettingTime)this.menu.get(this.currMode)).requestIncreaseTimeSection(); }
    public void decreaseTimeSection(){ ((SettingTime)this.menu.get(this.currMode)).requestDecreaseTimeSection(); }
    public void pressResetSecond()   { ((SettingTime)this.menu.get(this.currMode)).requestResetSecond(); }
    public void exitSettingTime()    { ((SettingTime)this.menu.get(this.currMode)).requestExitSettingTime(); }
    public int  getSettingTimeFlag() { return ((SettingTime)this.menu.get(this.currMode)).getCurrSection(); }

    // Stopwatch
    public void pressStartStopwatch(){ ((Stopwatch)this.menu.get(this.currMode)).requestStartStopwatch(); }
    public void pressStopStopwatch() { ((Stopwatch)this.menu.get(this.currMode)).requestStopStopwatch(); }
    public void pressSplitStopwatch(){ ((Stopwatch)this.menu.get(this.currMode)).requestSplitStopwatch(); }
    public void pressResetStopwatch(){ ((Stopwatch)this.menu.get(this.currMode)).requestResetStopwatch(); }
    public int  getStopwatchFlag()   { return ((Stopwatch)this.menu.get(this.currMode)).requestStopwatchFlag(); }

    // Timer
    public void enterSetTimerTime()       { ((Timer)this.menu.get(this.currMode)).requestTimerTime(); }
    public void nextTimerTimeSection()    { ((Timer)this.menu.get(this.currMode)).requestNextTimerTimeSection(); }
    public void increaseTimerTimeSection(){ ((Timer)this.menu.get(this.currMode)).requestIncreaseTimerTimeSection(); }
    public void decreaseTimerTimeSection(){ ((Timer)this.menu.get(this.currMode)).requestDecreaseTimerTimeSection(); }
    public void exitSetTimerTime()        { ((Timer)this.menu.get(this.currMode)).requestExitSetTimerTime(); }
    public void pressStartTimer()         { ((Timer)this.menu.get(this.currMode)).changeStatus(1); }
    public void pressStopTimer()          { ((Timer)this.menu.get(this.currMode)).changeStatus(0); }
    public void pressResetTimer()         { ((Timer)this.menu.get(this.currMode)).requestResetTimer(); }
    public void pressStopRingingTimer()   { ((Timer)this.menu.get(this.currMode)).ringOff(); }
    public int  getTimerFlag()            { return ((Timer)this.menu.get(this.currMode)).requestTimerFlag(); }

    // Alarm
    public void enterSetAlarmTime()    { ((Alarm)this.menu.get(this.currMode)).requestSettingAlarm(); }
    public void nextAlarmTimeSection() { ((Alarm)this.menu.get(this.currMode)).requestAlarmNextSection();}
    public void increaseAlarmTime()    { ((Alarm)this.menu.get(this.currMode)).increaseSection(); }
    public void decreaseAlarmTime()    { ((Alarm)this.menu.get(this.currMode)).decreaseSection(); }
    public void pressNextAlarm()       { ((Alarm)this.menu.get(this.currMode)).requestNextAlarm(); }
    public void pressStopRingingAlarm(){ ((Alarm)this.menu.get(this.currMode)).requestStopRinging(); }
    public void pressAlarmOnOff()      { ((Alarm)this.menu.get(this.currMode)).requestAlarmOnOff(); }
    public void exitSetAlarmSetting()  { ((Alarm)this.menu.get(this.currMode)).requestExitAlarmSetting(); }
    public int  getAlarmFlag()         { return ((Alarm)this.menu.get(this.currMode)).requestAlarmFlag(); }

    // Worldtime
    public void nextWorldtimeNation(){ ((Worldtime)this.menu.get(this.currMode)).nextNation(); }
    public void prevWorldtimeNation(){ ((Worldtime)this.menu.get(this.currMode)).prevNation(); }
    /* [Remove] public void pressSummerTime(){} */

    // Sun
    public void pressSetRise() { ((Sun)this.menu.get(this.currMode)).requestSetRise(); }
    public void nextSunNation(){ ((Sun)this.menu.get(this.currMode)).requestNextNation(); }
    public void prevSunNation(){ ((Sun)this.menu.get(this.currMode)).requestPrevNation(); }

    // Getters and Setters

    /* [sonarqube][Return a copy of "menu"] */
    public ArrayList getMenu(){ return (ArrayList)this.menu.clone(); }
    public Object getMenu(int i){ return this.menu.get(i); }
    public void setMenu(int i, Object o){ this.menu.set(i, o); }
    public int getMaxCnt(){ return this.maxCnt; }
}
