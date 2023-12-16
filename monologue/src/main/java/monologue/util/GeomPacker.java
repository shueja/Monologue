package monologue.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;

public class GeomPacker {
  public static double[] pack(Translation2d obj) {
    double[] arr = new double[2];
    arr[0] = obj.getX();
    arr[1] = obj.getY();
    return arr;
  }

  public static Translation2d unpackTranslation2d(double[] arr) {
    return new Translation2d(arr[0], arr[1]);
  }

  public static double[] pack(Rotation2d obj) {
    double[] arr = new double[1];
    arr[0] = obj.getRadians();
    return arr;
  }

  public static Rotation2d unpackRotation2d(double[] arr) {
    return new Rotation2d(arr[0]);
  }

  public static double[] pack(Pose2d obj) {
    double[] arr = new double[3];
    arr[0] = obj.getTranslation().getX();
    arr[1] = obj.getTranslation().getY();
    arr[2] = obj.getRotation().getRadians();
    return arr;
  }

  public static Pose2d unpackPose2d(double[] arr) {
    return new Pose2d(arr[0], arr[1], new Rotation2d(arr[2]));
  }

  public static double[] pack(Transform2d obj) {
    double[] arr = new double[3];
    arr[0] = obj.getTranslation().getX();
    arr[1] = obj.getTranslation().getY();
    arr[2] = obj.getRotation().getRadians();
    return arr;
  }

  public static Transform2d unpackTransform2d(double[] arr) {
    return new Transform2d(new Translation2d(arr[0], arr[1]), new Rotation2d(arr[2]));
  }

  public static double[] pack(Translation3d obj) {
    double[] arr = new double[3];
    arr[0] = obj.getX();
    arr[1] = obj.getY();
    arr[2] = obj.getZ();
    return arr;
  }

  public static Translation3d unpackTranslation3d(double[] arr) {
    return new Translation3d(arr[0], arr[1], arr[2]);
  }

  public static double[] pack(Rotation3d obj) {
    double[] arr = new double[3];
    arr[0] = obj.getX();
    arr[1] = obj.getY();
    arr[2] = obj.getZ();
    return arr;
  }

  public static Rotation3d unpackRotation3d(double[] arr) {
    return new Rotation3d(arr[0], arr[1], arr[2]);
  }

  public static double[] pack(Pose3d obj) {
    double[] arr = new double[7];
    arr[0] = obj.getTranslation().getX();
    arr[1] = obj.getTranslation().getY();
    arr[2] = obj.getTranslation().getZ();
    arr[3] = obj.getRotation().getX();
    arr[4] = obj.getRotation().getY();
    arr[5] = obj.getRotation().getZ();
    return arr;
  }

  public static Pose3d unpackPose3d(double[] arr) {
    return new Pose3d(
        new Translation3d(arr[0], arr[1], arr[2]), new Rotation3d(arr[3], arr[4], arr[5]));
  }

  public static double[] pack(Transform3d obj) {
    double[] arr = new double[7];
    arr[0] = obj.getTranslation().getX();
    arr[1] = obj.getTranslation().getY();
    arr[2] = obj.getTranslation().getZ();
    arr[3] = obj.getRotation().getX();
    arr[4] = obj.getRotation().getY();
    arr[5] = obj.getRotation().getZ();
    return arr;
  }

  public static Transform3d unpackTransform3d(double[] arr) {
    return new Transform3d(
        new Translation3d(arr[0], arr[1], arr[2]), new Rotation3d(arr[3], arr[4], arr[5]));
  }
}
