package dungeonmania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TimeController implements Serializable {
    private int currentTick;
    private int currentTime;
    private List<TimePeriod> periods = new ArrayList<>();

    public TimeController() {
        this.currentTick = 0;
        this.currentTime = 0;
        periods.add(new TimePeriod(new ArrayList<String>()));
    }

    public void addPeriod(TimePeriod t) {
        periods.add(t);
    }

    public void goForward() {
        if (currentTick == currentTime) {
            currentTime++;
        }
        currentTick++;
    }

    public void reverseTime(int tick) {
        currentTime -= tick;
    }

    public int getTick() {
        return currentTick;
    }
    public void setTick(int tick) {
        this.currentTick = tick;
    }
    public List<TimePeriod> getPeriods() {
        return periods;
    }

    public TimePeriod getCurrentTimePeriod() {
        return periods.get(currentTime);
    }

    public void setPeriods(List<TimePeriod> periods) {
        this.periods = periods;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public boolean inPast() {
        if (currentTick == currentTime && currentTime < periods.size()) {
            return true;
        } else if (currentTick == currentTime && currentTime == periods.size()) {
            return false;
        }
        return false;
    }

    public void logRewind(boolean expression) {
        if (expression) {
            if (getCurrentTick() == getCurrentTime() && getCurrentTime() == getPeriods().size() - 1) {
                if (getPeriods().size() == 1) {
                    List<String> s = new ArrayList<>();
                    addPeriod(new TimePeriod(s));
                }
                TimePeriod current = getCurrentTimePeriod();
                current.addAction("rewind");
            } 
        }
    }

    public void shiftTime(String operation, String command) {
        if (operation.equals("build") || operation.equals("interact")) {
            if (getCurrentTick() == getCurrentTime() && getCurrentTime() == getPeriods().size() - 1) {
                if (periods.size() == 1) {
                    List<String> s = new ArrayList<>();
                    addPeriod(new TimePeriod(s));
                }
                TimePeriod current = getCurrentTimePeriod();
                if (operation.equals("build")) {
                    current.addAction("build_" + command);
                } else if (operation.equals("interact")) {
                    current.addAction("interact_" + command);
                }
            } 
        } else if (operation.equals("use") || operation.equals("move")) {
            goForward();
            if (!inPast()) {
                List<String> s = new ArrayList<>();
                if (operation.equals("use")) {
                    s.add("use_" + command);
                } else if (operation.equals("move")) {
                    s.add("move_" + command);
                }
                addPeriod(new TimePeriod(s));
            } 
        }    
    }


}

