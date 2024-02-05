package frc.robot.inheritance;

import monologue.Annotations.*;

public class Child extends Parent {
    @Log
    public Boolean childPublicTrue = true;
    @Log
    private Boolean childPrivateTrue = true;
    @Log
    protected Boolean childProtectedTrue = true;

    @Log
    public Double childPublicRand() {
        return Math.random();
    }

    @Log
    private Double childPrivateRand() {
        return Math.random();
    }

    @Log
    protected Double childProtectedRand() {
        return Math.random();
    }
}
