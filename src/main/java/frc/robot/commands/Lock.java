package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SwerveSubsystem;

public class Lock extends CommandBase{
    
    private SwerveSubsystem swerve;

    public Lock(SwerveSubsystem subs){
        swerve = subs;

        addRequirements(subs);
    }

    @Override
    public void initialize(){
        //swerve.lock();
    }

    @Override
    public void execute(){
        //swerve.stopModules();
        swerve.test();
        
    }

    @Override
    public void end(boolean interrupted){

    }

    @Override
    public boolean isFinished(){
         return false;
    }
}
