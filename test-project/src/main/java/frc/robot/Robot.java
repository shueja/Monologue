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
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.BooleanEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.IntegerArrayLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
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
  @Log.NT.Once private boolean flippingBool = false;
  private int samples = 0;
  @Log.NT(level = NOT_FILE_ONLY) int debugSamples = 0;
  @Log.NT(level = DEFAULT) int lowbandwidthSamples = 0;
  @Log.NT(level = OVERRIDE_FILE_ONLY) int compSamples = 0;

  ArrayList<Internal> internals = new ArrayList<>(List.of(
    new Internal(""),
    new Internal(""),
    new Internal("")
  ));
  double totalOfAvgs = 0;
  double avgsTaken = 0;

  @Log.NT SwerveModuleState state = new SwerveModuleState(1, new Rotation2d(0.5));

  @Log.NT SwerveModuleState[] stateArr = new SwerveModuleState[] {
    new SwerveModuleState(1, new Rotation2d(0.5)),
    new SwerveModuleState(1, new Rotation2d(0.5))
  };

  @SuppressWarnings("unused")
  private Geometry geometry = new Geometry();

  @IgnoreLogged
  private Geometry geometryIgnored = new Geometry();

  @SuppressWarnings("unused")
  private Child child = new Child();

  private Translation2d translation2d = new Translation2d(1.0, 2.0);


  @Log.NT private Field2d field = new Field2d();

  @Log.NT private Mechanism2d mech = new Mechanism2d(1, 1);
  @Log.NT private int[] array = {0, 1, 2};
  @Log.NT private int number = 0;

  BooleanEntry fileOnlyEntry = NetworkTableInstance.getDefault().getBooleanTopic("/fileOnly").getEntry(false);

  public Robot() {
    super();
    Monologue.setupMonologue(this, "/Robot", true, false);
  }

  @Override
  public void robotInit() {
    fileOnlyEntry.set(false);
    System.out.println("before first update");
  }

  @Override
  public void robotPeriodic() {
    Monologue.setFileOnly(fileOnlyEntry.get());
    Monologue.updateAll();
    field.getRobotObject().setPose(new Pose2d(samples / 100.0, 0, new Rotation2d()));
    log("stringValue", samples, OVERRIDE_FILE_ONLY);
    log("stringValueDebug", samples, NOT_FILE_ONLY);
    log("structTestDebug", translation2d, NOT_FILE_ONLY);
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
    //array[0] = samples;
  }

  @Override
    public void driverStationConnected() {
        //if we are in a match disable debug
        Monologue.setFileOnly(
            DriverStation.getMatchType() != MatchType.None
        );
    }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {

  }

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

  @Log.NT
  public String getStringPath() {
    return getFullPath();
  }
}
