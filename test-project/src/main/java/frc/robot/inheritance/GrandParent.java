package frc.robot.inheritance;

import monologue.Annotations.*;

public abstract class GrandParent {
    @Log.NT
    public Boolean grandparentPublicTrue = true;
    @Log.NT
    private Boolean grandparentPrivateTrue = true;
    @Log.NT
    protected Boolean grandparentProtectedTrue = true;

    @Log.NT
    public Double grandparentPublicRand() {
        return Math.random();
    }

    @Log.NT
    private Double grandparentPrivateRand() {
        return Math.random();
    }

    @Log.NT
    protected Double grandparentProtectedRand() {
        return Math.random();
    }
}
