package frc.robot.inheritance;

import monologue.Annotations.*;

public class Child extends Parent {
    @LogNT
    public Boolean childPublicTrue = true;
    @LogNT
    private Boolean childPrivateTrue = true;
    @LogNT
    protected Boolean childProtectedTrue = true;

    @LogNT
    public Double childPublicRand() {
        return Math.random();
    }

    @LogNT
    private Double childPrivateRand() {
        return Math.random();
    }

    @LogNT
    protected Double childProtectedRand() {
        return Math.random();
    }
}
