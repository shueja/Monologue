// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import monologue.MonoRobot;
import monologue.Tracer;
import monologue.Monologue;
import monologue.Annotations.*;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.BooleanEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import static monologue.LogLevel.*;

import java.util.ArrayList;


public class Robot extends MonoRobot {
  @LogOnceNT private boolean flippingBool = false;
  private int samples = 0;
  @LogNT(level = DEBUG) int debugSamples = 0;
  @LogNT(level = FILE_IN_COMP) int lowbandwidthSamples = 0;
  @LogNT(level = COMP) int compSamples = 0; 
  boolean dataLog = false;

  ArrayList<Internal> m_internals = new ArrayList<>();
  double totalOfAvgs = 0;
  double avgsTaken = 0;

  @SuppressWarnings("unused")
  private Geometry geometry = new Geometry();

  private Translation2d translation2d = new Translation2d(1.0, 2.0);

  @LogNT private Field2d field = new Field2d();

  @LogNT private Mechanism2d mech = new Mechanism2d(1, 1);
  @LogFile private long[] array = {0, 1, 2};

  BooleanEntry debugEntry = NetworkTableInstance.getDefault().getBooleanTopic("/debug").getEntry(false);

  @Override
  public void robotInit() {
    debugEntry.set(true);
    
  }

  @Override
  public void robotPeriodic() {
    Tracer.startTrace("robotPeriodic");
    Monologue.setDebug(debugEntry.get());
    Tracer.traceFunc("Monologue Update", Monologue::updateAll);
    field.getRobotObject().setPose(new Pose2d(samples / 100.0, 0, new Rotation2d()));
    log("stringValue", samples, COMP);
    log("stringValueDebug", samples, DEBUG);
    log("structTestDebug", translation2d, DEBUG);
    SmartDashboard.putBoolean(getPath(), dataLog);
    samples++;
    debugSamples++;
    lowbandwidthSamples++;
    compSamples++;
    flippingBool = !flippingBool;
    translation2d = new Translation2d(
      (Math.random()+0.52) * translation2d.getX(),
      (Math.random()+0.52) * translation2d.getY()
    );
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
