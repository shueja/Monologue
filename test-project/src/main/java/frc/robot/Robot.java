// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import monologue.MonoRobot;
import monologue.Tracer;
import monologue.Monologue;
import monologue.Monologue.LogFile;
import monologue.Monologue.LogNT;
import monologue.Monologue.MonoShuffleboard;
import monologue.Monologue.MonoShuffleboardTab;
import monologue.LogLevel;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;


@MonoShuffleboardTab
public class Robot extends MonoRobot {
  @LogNT(once=true) private int samples = 0;
  boolean dataLog = false;

  ArrayList<Internal> m_internals = new ArrayList<>();
  double totalOfAvgs = 0;
  @MonoShuffleboard(pos = {3, 1})
  double avgsTaken = 0;

  @SuppressWarnings("unused")
  private Geometry geometry = new Geometry();

  @SuppressWarnings("unused")
  private SbTest sbTest = new SbTest();

  @LogNT private Field2d field = new Field2d();

  @LogNT private Mechanism2d mech = new Mechanism2d(1, 1);
  @LogFile private long[] array = {0, 1, 2};

  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {
    Tracer.startTrace("robotPeriodic");
    Tracer.traceFunc("Monologue Update", Monologue::updateAll);
    field.getRobotObject().setPose(new Pose2d(samples / 100.0, 0, new Rotation2d()));
    log("stringValue", samples, LogLevel.FILE_IN_COMP);
    SmartDashboard.putBoolean(getPath(), dataLog);
    Tracer.endTrace();
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
