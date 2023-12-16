package frc.robot;

import static monologue.Annotations.*;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import monologue.Logged;

public class Internal implements Logged {

  @LogNT
  static Boolean staticBool = true;

  @LogNT
  SwerveModuleState state = new SwerveModuleState(1, new Rotation2d(0.5));
  private int calls = 0;
  private String name;

  public Internal(String name) {
    this.name = name + "asdfghjklqwertyuiopzxcvbnm,qwertyuio";
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
