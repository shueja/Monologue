package monologue;

import edu.wpi.first.wpilibj.TimedRobot;

public class MonoRobot extends TimedRobot implements Logged {
    
    @Override
    protected void loopFunc() {
        Tracer.traceFunc("Robot Loop", super::loopFunc);
    }

    public Boolean isDebug() {
        return true;
    }

    public MonoRobot() {
        super();
        Monologue.setupMonologue(this, "/Robot", this.isDebug());
    }
}
