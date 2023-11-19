// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import monologue.Logged;
import monologue.Monologue;
import monologue.Annotations.*;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.BooleanEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DriverStation.MatchType;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import frc.robot.inheritance.Child;

import static monologue.LogLevel.*;

import java.util.ArrayList;
import java.util.List;


public class Robot extends TimedRobot implements Logged {
  @LogOnceNT private boolean flippingBool = false;
  private int samples = 0;
  @LogNT(level = DEBUG) int debugSamples = 0;
  @LogNT(level = FILE_IN_COMP) int lowbandwidthSamples = 0;
  @LogNT(level = COMP) int compSamples = 0;

  ArrayList<Internal> internals = new ArrayList<>(List.of(
    new Internal(""),
    new Internal(""),
    new Internal("")
  ));
  double totalOfAvgs = 0;
  double avgsTaken = 0;

  @SuppressWarnings("unused")
  private Geometry geometry = new Geometry();

  @IgnoreLogged
  private Geometry geometryIgnored = new Geometry();

  @SuppressWarnings("unused")
  private Child child = new Child();

  private Translation2d translation2d = new Translation2d(1.0, 2.0);

  @LogNT private Field2d field = new Field2d();

  @LogNT private Mechanism2d mech = new Mechanism2d(1, 1);
  @LogFile private long[] array = {0, 1, 2};

  BooleanEntry debugEntry = NetworkTableInstance.getDefault().getBooleanTopic("/debug").getEntry(false);

  public Robot() {
    super();
    Monologue.setupMonologue(this, "/Robot", true);
  }

  @Override
  public void robotInit() {
    debugEntry.set(true);
  }

  @Override
  public void robotPeriodic() {
    Monologue.setDebug(debugEntry.get());
    Monologue.updateAll();
    field.getRobotObject().setPose(new Pose2d(samples / 100.0, 0, new Rotation2d()));
    log("stringValue", samples, COMP);
    log("stringValueDebug", samples, DEBUG);
    log("structTestDebug", translation2d, DEBUG);
    samples++;
    debugSamples++;
    lowbandwidthSamples++;
    compSamples++;
    flippingBool = !flippingBool;
    Internal.staticBool = !Internal.staticBool;
    translation2d = new Translation2d(
      (Math.random()+0.55) * translation2d.getX(),
      (Math.random()+0.55) * translation2d.getY()
    );
  }

  @Override
    public void driverStationConnected() {
        //if we are in a match disable debug
        Monologue.setDebug(
            DriverStation.getMatchType() == MatchType.None
        );
    }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {}

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}

  @Override
  public String getPath() {
    return "Robot";
  }

  @LogNT
  public String getStringPath() {
    return getFullPath();
  }
}
