package frc.robot;

import static monologue.Annotations.*;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import monologue.Logged;

public class Internal implements Logged {

  @Log.NT
  static Boolean staticBool = true;

  @Log.NT
  SwerveModuleState state = new SwerveModuleState(1, new Rotation2d(0.5));
  @Log.NT
  ArmFeedforward ff = new ArmFeedforward(1, 2, 3, 4);
  ArmFeedforward ff2 = new ArmFeedforward(4, 3, 2, 1);
  private int calls = 0;
  private String name;

  public Internal(String name) {
    this.name = name;
    log("construct", ff);
  };

  @Override
  public String getPath() {
    return name;
  }

  @Log.NT
  public double getNumber() {
    calls++;
    if (calls == 500) {
      log("construct", ff);
    } else if (calls == 1000) {
      log("construct", ff2);
    }
    return calls + calls / 1000.0;
  }
}
