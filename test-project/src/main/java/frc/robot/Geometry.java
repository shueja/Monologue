// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import monologue.Annotations.*;
import monologue.Logged;
import edu.wpi.first.math.geometry.*;

/** Add your docs here. */
public class Geometry implements Logged {
  @Log private Pose2d m_pose2d = new Pose2d();

  @Log
  private Pose2d getPose2d() {
    return m_pose2d;
  }

  @Log private Translation2d m_translation2d = new Translation2d();

  @Log
  private Translation2d getTranslation2d() {
    return m_translation2d;
  }

  @Log private Transform2d m_transform2d = new Transform2d();

  @Log
  private Transform2d getTransform2d() {
    return m_transform2d;
  }

  @Log private Rotation2d m_rotation2d = new Rotation2d();

  @Log
  private Rotation2d getRotation2d() {
    return m_rotation2d;
  }

  @Log private Pose3d m_pose3d = new Pose3d();

  @Log
  private Pose3d getPose3d() {
    return m_pose3d;
  }

  @Log private Translation3d m_translation3d = new Translation3d();

  @Log
  private Translation3d getTranslation3d() {
    return m_translation3d;
  }

  @Log private Transform3d m_transform3d = new Transform3d();

  @Log
  private Transform3d getTransform3d() {
    return m_transform3d;
  }

  @Log private Rotation3d m_rotation3d = new Rotation3d();

  @Log
  private Rotation3d getRotation3d() {
    return m_rotation3d;
  }
}
