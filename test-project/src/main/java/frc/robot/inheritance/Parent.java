package frc.robot.inheritance;

import monologue.Annotations.*;
import monologue.Logged;

public class Parent extends GrandParent implements Logged {
    @LogNT
    public Boolean parentPublicTrue = true;
    @LogNT
    private Boolean parentPrivateTrue = true;
    @LogNT
    protected Boolean parentProtectedTrue = true;

    @LogNT
    public Double parentPublicRand() {
        return Math.random();
    }

    @LogNT
    private Double parentPrivateRand() {
        return Math.random();
    }

    @LogNT
    protected Double parentProtectedRand() {
        return Math.random();
    }
}
