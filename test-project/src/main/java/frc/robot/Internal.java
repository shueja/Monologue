package frc.robot;

import monologue.Annotations.LogNT;
import monologue.Logged;

public class Internal implements Logged {

  @LogNT
  static Boolean staticBool = true;

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
