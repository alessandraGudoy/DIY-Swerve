package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SwerveSubsystem;

public class Rotatinator extends CommandBase {
  private SwerveSubsystem swerve;
  private DoubleSupplier x, y;
  private PIDController pid;

  public Rotatinator(SwerveSubsystem subs, DoubleSupplier x, DoubleSupplier y) {
    swerve = subs;
    this.x = x;
    this.y = y;

    pid = new PIDController(0.002, 0, 0);

    addRequirements(subs);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {

    double yaw = swerve.getRobotRotation().getDegrees();
    double desriedYaw = Math.atan2(x.getAsDouble(), y.getAsDouble());

    //swerve

  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}
