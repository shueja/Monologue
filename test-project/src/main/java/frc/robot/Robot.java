// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import monologue.Monologue;
import monologue.Monologue.LogBoth;
import monologue.Monologue.LogFile;
import monologue.Monologue.LogNT;
import monologue.Logged;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.Logger;
import java.util.ArrayList;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot implements Logged, Loggable {
  @LogBoth(once=true) private int samples = 0;
  boolean useOblog = false;
  boolean dataLog = false;

  ArrayList<Internal> m_internals = new ArrayList<>();
  private LinearFilter filter = LinearFilter.movingAverage(50);
  double totalOfAvgs = 0;
  double avgsTaken = 0;

  private Geometry m_geometry = new Geometry();

  @LogNT @LogFile private Field2d field = new Field2d();

  @LogBoth private Mechanism2d mech = new Mechanism2d(1, 1);
  @LogBoth private long[] array = {0, 1, 2};

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    SmartDashboard.putBoolean("bool", true);
    Monologue.dataLogger.addNetworkTable(
        NetworkTableInstance.getDefault().getTable("SmartDashboard"));
    // for (int i = 0; i < 100; i++) {
    //   m_internals.add(new Internal(i + ""));
    // }
    // DataLogManager.start();
    NetworkTableInstance.getDefault().getTopic("name").getGenericEntry();
    if (useOblog) {
      Logger.configureLoggingAndConfig(this, false);
    } else {
      Monologue.setupLogging(this, "/Robot", true);
    }
    put("imperative", new Transform2d());
  }

  @LogBoth
  public String getStringPath()
  {
    return getFullPath();
  }
  @Override
  public void robotPeriodic() {
    var timeBefore = Timer.getFPGATimestamp() * 1e6;
    if (useOblog) {
      Logger.updateEntries();
    } else {
      Monologue.update();
    }
    var timeAfter = Timer.getFPGATimestamp() * 1e6;
    samples++;
    double avg = filter.calculate(timeAfter - timeBefore);
    if (samples % 500 == 0 && samples < (500 * 8) + 50) {
      System.out.println(avg);
      totalOfAvgs += avg;
      avgsTaken++;
    }
    if (samples == 500 * 8) {
      System.out.println("Final Result: Oblog:" + useOblog + " DataLog:" + dataLog);
      System.out.println(totalOfAvgs / avgsTaken);
    }
    field.getRobotObject().setPose(new Pose2d(samples / 100.0, 0, new Rotation2d()));
    // m_internals.forEach(Internal::update);
    put("stringValue", samples + "");
    SmartDashboard.putBoolean(getPath(), dataLog);
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
    // TODO Auto-generated method stub
    return "Robot";
  }
}
