// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import monologue.Monologue.LogBoth;
import monologue.Logged;
import edu.wpi.first.math.geometry.*;

/** Add your docs here. */
public class Geometry implements Logged {
  @LogBoth
  public String getStringPath()
  {
    return getFullPath();
  }
  @LogBoth private Pose2d m_pose2d = new Pose2d();

  @LogBoth
  private Pose2d getPose2d() {
    return m_pose2d;
  }

  @LogBoth private Translation2d m_translation2d = new Translation2d();

  @LogBoth
  private Translation2d getTranslation2d() {
    return m_translation2d;
  }

  @LogBoth private Transform2d m_transform2d = new Transform2d();

  @LogBoth
  private Transform2d getTransform2d() {
    return m_transform2d;
  }

  @LogBoth private Rotation2d m_rotation2d = new Rotation2d();

  @LogBoth
  private Rotation2d getRotation2d() {
    return m_rotation2d;
  }

  @LogBoth private Pose3d m_pose3d = new Pose3d();

  @LogBoth
  private Pose3d getPose3d() {
    return m_pose3d;
  }

  @LogBoth private Translation3d m_translation3d = new Translation3d();

  @LogBoth
  private Translation3d getTranslation3d() {
    return m_translation3d;
  }

  @LogBoth private Transform3d m_transform3d = new Transform3d();

  @LogBoth
  private Transform3d getTransform3d() {
    return m_transform3d;
  }

  @LogBoth private Rotation3d m_rotation3d = new Rotation3d();

  @LogBoth
  private Rotation3d getRotation3d() {
    return m_rotation3d;
  }
}
