package frc.robot.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Pneumatics;

public class TMPClaw extends CommandBase {
  
  private Pneumatics piston;
  private Value prev;
  private Value val;

  public TMPClaw(Pneumatics pis) {
    piston = pis;

    addRequirements(pis);
  }

  @Override
  public void initialize() {
    piston.change();
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return true;
  }

  /*
  public Value getCurrentValue(){
    return piston.getvalue();
  }
  */
}