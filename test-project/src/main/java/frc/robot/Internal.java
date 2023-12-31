package frc.robot;

import static monologue.Annotations.*;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import monologue.Logged;

public class Internal implements Logged {

  @LogNT
  static Boolean staticBool = true;

  @LogNT
  SwerveModuleState state = new SwerveModuleState(1, new Rotation2d(0.5));
  @LogNT
  ArmFeedforward ff = new ArmFeedforward(1, 2, 3, 4);
  private int calls = 0;
  private String name;

  public Internal(String name) {
    this.name = name;
  };

  @Override
  public String getPath() {
    return name;
  }

  @LogNT
  public double getNumber() {
    calls++;
    return calls + calls / 1000.0;
  }
}
