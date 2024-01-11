package frc.robot.inheritance;

import monologue.Annotations.*;
import monologue.Logged;

public class Parent extends GrandParent implements Logged {
    @Log.NT
    public Boolean parentPublicTrue = true;
    @Log.NT
    private Boolean parentPrivateTrue = true;
    @Log.NT
    protected Boolean parentProtectedTrue = true;

    @Log.NT
    public Double parentPublicRand() {
        return Math.random();
    }

    @Log.NT
    private Double parentPrivateRand() {
        return Math.random();
    }

    @Log.NT
    protected Double parentProtectedRand() {
        return Math.random();
    }
}
