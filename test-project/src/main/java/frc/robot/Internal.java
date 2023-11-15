package frc.robot;

import monologue.Annotations.LogNT;
import monologue.Logged;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

public class Internal implements Logged, Loggable {

  private int calls = 0;
  private String name;
  private String showName;

  public Internal(String name) {
    this.name = name + "asdfghjklqwertyuiopzxcvbnm,qwertyuio";
    showName = this.name;
  };

  @Override
  public String getPath() {
    // TODO Auto-generated method stub
    return name;
  }

  @Override
  public String configureLogName() {
    // TODO Auto-generated method stub
    return name;
  }

  // @Log
  // @LogNT
  // public String name="name";
  // @Log
  // @LogFile
  public String getName() {
    calls++;
    return showName;
  }

  @Log
  @LogNT
  public double getNumber() {
    calls++;
    return calls + calls / 1000.0;
  }

  public void update() {
    showName = name + calls;
  }
}
