package frc.robot.inheritance;

import monologue.Annotations.*;

public abstract class GrandParent {
    @Log
    public Boolean grandparentPublicTrue = true;
    @Log
    private Boolean grandparentPrivateTrue = true;
    @Log
    protected Boolean grandparentProtectedTrue = true;

    @Log
    public Double grandparentPublicRand() {
        return Math.random();
    }

    @Log
    private Double grandparentPrivateRand() {
        return Math.random();
    }

    @Log
    protected Double grandparentProtectedRand() {
        return Math.random();
    }
}
