// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import monologue.Annotations.LogNT;
import monologue.Logged;
import edu.wpi.first.math.geometry.*;

/** Add your docs here. */
public class Geometry implements Logged {
  @LogNT
  public String getStringPath()
  {
    return getFullPath();
  }
  @LogNT private Pose2d m_pose2d = new Pose2d();

  @LogNT
  private Pose2d getPose2d() {
    return m_pose2d;
  }

  @LogNT private Translation2d m_translation2d = new Translation2d();

  @LogNT
  private Translation2d getTranslation2d() {
    return m_translation2d;
  }

  @LogNT private Transform2d m_transform2d = new Transform2d();

  @LogNT
  private Transform2d getTransform2d() {
    return m_transform2d;
  }

  @LogNT private Rotation2d m_rotation2d = new Rotation2d();

  @LogNT
  private Rotation2d getRotation2d() {
    return m_rotation2d;
  }

  @LogNT private Pose3d m_pose3d = new Pose3d();

  @LogNT
  private Pose3d getPose3d() {
    return m_pose3d;
  }

  @LogNT private Translation3d m_translation3d = new Translation3d();

  @LogNT
  private Translation3d getTranslation3d() {
    return m_translation3d;
  }

  @LogNT private Transform3d m_transform3d = new Transform3d();

  @LogNT
  private Transform3d getTransform3d() {
    return m_transform3d;
  }

  @LogNT private Rotation3d m_rotation3d = new Rotation3d();

  @LogNT
  private Rotation3d getRotation3d() {
    return m_rotation3d;
  }
}
