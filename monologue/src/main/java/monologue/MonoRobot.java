package monologue;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.MatchType;

/**
 * A Robot subclass that automatically does monologue setup
 *  - Sets up logging
 *  - Sets up robot cycle tracing
 *  - Automatically disables debug mode when in a match
 */
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

    @Override
    public void driverStationConnected() {
        super.driverStationConnected();
        //if we are in a match disable debug
        Monologue.setDebug(
            DriverStation.getMatchType() == MatchType.None
        );
    }
}
