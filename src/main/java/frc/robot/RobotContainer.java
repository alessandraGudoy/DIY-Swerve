package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DriverControl;
import frc.robot.commands.Endgame;
import frc.robot.commands.Lock;
import frc.robot.commands.MoveMotor;
import frc.robot.commands.Rotatinate;
import frc.robot.commands.TMPClaw;
import frc.robot.subsystems.Motor;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.SwerveSubsystem;

public class RobotContainer {

  private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
  private final Motor elv = new Motor(13);
  private final Motor pivot = new Motor(14);
  // private final Motor wrist = new Motor(15);
  //private final Pneumatics claw = new Pneumatics();
  private final XboxController m_Controller = new XboxController(0);
  private final Joystick joystick = new Joystick(1);

  public RobotContainer() {
    swerveSubsystem.setDefaultCommand(new DriverControl(swerveSubsystem, 
      () -> -m_Controller.getLeftY(), 
      () -> -m_Controller.getLeftX(),
      () -> -m_Controller.getRightX(), 
      () -> m_Controller.getRightBumper()));

    elv.setDefaultCommand(new MoveMotor(elv, () -> 0));
    pivot.setDefaultCommand(new MoveMotor(pivot, () -> 0));
    // wrist.setDefaultCommand(new MoveMotor(wrist, () -> 0));

    configureBindings();
  }

  private void configureBindings() {
    //new JoystickButton(m_Controller, 1).onTrue(new InstantCommand(() -> swerveSubsystem.resetNavx()));
    new JoystickButton(m_Controller, 5).toggleOnTrue(new Lock(swerveSubsystem));

    // Rotatinator
    //new JoystickButton(m_Controller, 2).whileTrue(new Rotatinate(swerveSubsystem, () -> m_Controller.getLeftX(), () -> m_Controller.getLeftY()));

    new JoystickButton(m_Controller, 1).whileTrue(new MoveMotor(elv, ()-> -0.5));
    new JoystickButton(m_Controller, 4).whileTrue(new MoveMotor(elv, () -> 0.5));

    new JoystickButton(m_Controller, 2).whileTrue(new MoveMotor(pivot, ()-> -0.5));
    new JoystickButton(m_Controller, 3).whileTrue(new MoveMotor(pivot, () -> 0.5));

    //new JoystickButton(m_Controller, 7).onTrue(new TMPClaw(claw));

    // new JoystickButton(m_Controller, 2).whileTrue(new MoveMotor(wrist, ()-> -0.5));
    // new JoystickButton(m_Controller, 3).whileTrue(new MoveMotor(wrist, () -> 0.5));

    //new JoystickButton(m_Controller, 4).toggleOnTrue(new Endgame(swerveSubsystem, ()->m_Controller.getLeftY()));
  }


  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}
