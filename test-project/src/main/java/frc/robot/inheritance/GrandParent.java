package frc.robot.inheritance;

import monologue.Annotations.*;

public abstract class GrandParent {
    @LogNT
    public Boolean grandparentPublicTrue = true;
    @LogNT
    private Boolean grandparentPrivateTrue = true;
    @LogNT
    protected Boolean grandparentProtectedTrue = true;

    @LogNT
    public Double grandparentPublicRand() {
        return Math.random();
    }

    @LogNT
    private Double grandparentPrivateRand() {
        return Math.random();
    }

    @LogNT
    protected Double grandparentProtectedRand() {
        return Math.random();
    }
}
