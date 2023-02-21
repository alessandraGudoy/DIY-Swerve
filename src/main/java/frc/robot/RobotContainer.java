package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DriverControl;
import frc.robot.commands.Lock;
import frc.robot.commands.MoveMotor;
import frc.robot.commands.Rotatinate;
import frc.robot.subsystems.Motor;
import frc.robot.subsystems.SwerveSubsystem;

public class RobotContainer {

  private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
  private final Motor elv = new Motor(13);
  private final Motor motor = new Motor(14);
  private final XboxController m_Controller = new XboxController(0);
  private final Joystick joystick = new Joystick(1);

  public RobotContainer() {
    swerveSubsystem.setDefaultCommand(new DriverControl(swerveSubsystem, 
      () -> -m_Controller.getLeftY(), 
      () -> -m_Controller.getLeftX(),
      () -> -m_Controller.getRightX(), 
      () -> m_Controller.getRightBumper()));

    elv.setDefaultCommand(new MoveMotor(elv, () -> 0));

    motor.setDefaultCommand(new MoveMotor(motor, () -> joystick.getY()));
    configureBindings();
  }

  private void configureBindings() {
    new JoystickButton(m_Controller, 1).onTrue(new InstantCommand(() -> swerveSubsystem.resetNavx()));
    new JoystickButton(m_Controller, 5).toggleOnTrue(new Lock(swerveSubsystem));

    // Rotatinator
    //new JoystickButton(m_Controller, 2).whileTrue(new Rotatinate(swerveSubsystem, () -> m_Controller.getLeftX(), () -> m_Controller.getLeftY()));

    new JoystickButton(joystick, 11).whileTrue(new MoveMotor(elv, ()-> -0.5));
    new JoystickButton(joystick, 12).whileTrue(new MoveMotor(elv, () -> 0.5));
  }


  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}
