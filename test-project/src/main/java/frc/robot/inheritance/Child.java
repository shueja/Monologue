package frc.robot.inheritance;

import monologue.Annotations.*;

public class Child extends Parent {
    @Log.NT
    public Boolean childPublicTrue = true;
    @Log.NT
    private Boolean childPrivateTrue = true;
    @Log.NT
    protected Boolean childProtectedTrue = true;

    @Log.NT
    public Double childPublicRand() {
        return Math.random();
    }

    @Log.NT
    private Double childPrivateRand() {
        return Math.random();
    }

    @Log.NT
    protected Double childProtectedRand() {
        return Math.random();
    }
}
