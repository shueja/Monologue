package frc.robot.inheritance;

import monologue.Annotations.*;
import monologue.Logged;

public class Parent extends GrandParent implements Logged {
    @Log
    public Boolean parentPublicTrue = true;
    @Log
    private Boolean parentPrivateTrue = true;
    @Log
    protected Boolean parentProtectedTrue = true;

    @Log
    public Double parentPublicRand() {
        return Math.random();
    }

    @Log
    private Double parentPrivateRand() {
        return Math.random();
    }

    @Log
    protected Double parentProtectedRand() {
        return Math.random();
    }
}
