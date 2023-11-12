package frc.robot;

import monologue.Logged;
import monologue.Monologue.LogNT;
import monologue.Monologue.MonoShuffleboard;
import monologue.Monologue.MonoShuffleboardLayout;
import monologue.Monologue.MonoShuffleboardTab;

@MonoShuffleboardLayout
public class SbTest implements Logged {

    @MonoShuffleboard
    double value1 = 0.2;
    @MonoShuffleboard
    Double value2 = 0.45;
    @LogNT
    Double value3 = 0.77;


    public SbTest() {

    }

    @MonoShuffleboard(size = {2, 1})
    public Boolean randTrue() {
        return Math.random() > 0.5;
    }
}
