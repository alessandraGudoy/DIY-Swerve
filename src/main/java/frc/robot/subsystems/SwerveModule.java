package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.*;

public class SwerveModule extends SubsystemBase{
    private CANSparkMax turningMotor;
    private WPI_TalonFX drivingMotor;

    private CANCoder absoluteEncoder;

    private RelativeEncoder turningEnc;
    private TalonFXSensorCollection drivingEnc;

    private PIDController turningPID;

    private double encoderOffset;
    private boolean encoderReversed;

    private double kp, ki, kd;

    public SwerveModule(int neoPort, int talonPort, int cancoderPort, double encoderOffset, boolean encoderReversed, boolean driveReversed, boolean turnReversed){
        turningMotor = new CANSparkMax(neoPort, MotorType.kBrushless);
        drivingMotor = new WPI_TalonFX(talonPort);

        absoluteEncoder = new CANCoder(cancoderPort);
        absoluteEncoder.configAbsoluteSensorRange(AbsoluteSensorRange.Unsigned_0_to_360);

        turningEnc = turningMotor.getEncoder();
        drivingEnc = new TalonFXSensorCollection(drivingMotor);

        turningPID = new PIDController(SwerveConsts.kp, SwerveConsts.ki, SwerveConsts.kd);
        turningPID.enableContinuousInput(-Math.PI, Math.PI); // System is circular;  Goes from -Math.PI to 0 to Math.PI

        this.encoderOffset = encoderOffset;
        this.encoderReversed = encoderReversed;
        
        turningEnc.setPositionConversionFactor(SwerveConsts.turningEncoderRotationConversion);
        turningEnc.setVelocityConversionFactor(SwerveConsts.turningEncoderSpeedConversion);

        drivingMotor.setInverted(driveReversed);
        turningMotor.setInverted(turnReversed);
        turningMotor.setIdleMode(IdleMode.kBrake);

        resetEncoders();

    }

    /* * * ENCODER VALUES * * */

    public double getDrivePosition(){
        return drivingEnc.getIntegratedSensorPosition();
    }

    // neo encoder in degrees 
    public double getTurningPosition(){
        return (turningEnc.getPosition() % 900) / 900 * 360;
    }

    public double getDriveSpeed(){
        return drivingEnc.getIntegratedSensorVelocity();
    }

    public double getTurningSpeed(){
        return turningEnc.getVelocity();
    }

    //absolute encoder in radians 
    public double getAbsoluteEncoder(){
        double angle = Math.toRadians(absoluteEncoder.getAbsolutePosition());
        //double angle = (absoluteEncoder.getAbsolutePosition() * 2.0 * Math.PI) - encoderOffset; // in radians
        if(encoderReversed){
            return (angle * -1);
        } else {
            return angle;
        }
    }

    // set turning enc to value of absolute encoder
    public void resetEncoders(){
        drivingEnc.setIntegratedSensorPosition(0, 0);
        turningEnc.setPosition(getAbsoluteEncoder());
    }

    public SwerveModuleState getState(){
        // Rotation2d = rotation represented by a point on the unit circle
        // Rotation2d(double) => constructs a Rotation2d given the angle in radians
        
        // SwerveModuleState = state of a swerve module
        // SwerveModuleState(speed (in meters per second), angle of module (Using Rotation2d))
        return new SwerveModuleState(getDriveSpeed(), new Rotation2d(getAbsoluteEncoder()));
    }

    public void setDesiredState(SwerveModuleState state){
        // To make keep robot from going back to 0 position
        if(Math.abs(state.speedMetersPerSecond) < 0.01){
            stop();
            return;
        }

        state = SwerveModuleState.optimize(state, getState().angle);

        // set speed
        drivingMotor.set(state.speedMetersPerSecond / SwerveConsts.maxSpeed_mps * SwerveConsts.voltage); 
        turningMotor.set(turningPID.calculate(getAbsoluteEncoder(), state.angle.getRadians()) * SwerveConsts.voltage);

        // Print to SmartDashboard
        SmartDashboard.putNumber("Swerve["+absoluteEncoder.getDeviceID()+"] desired enc", state.angle.getRadians()); //desired enc 
        SmartDashboard.putString("Swerve["+absoluteEncoder.getDeviceID()+"] state", state.toString());  
    }

    // test
    public void lockInPlace(){
        SwerveModuleState lock = new SwerveModuleState(0, new Rotation2d(getAbsoluteEncoder()));
        if(Math.abs(lock.speedMetersPerSecond) < 0.01){
            stop();
            return;
        }

        lock = SwerveModuleState.optimize(lock, getState().angle);
        drivingMotor.set(0);
        turningMotor.set(turningPID.calculate(getAbsoluteEncoder(), lock.angle.getRadians()) * SwerveConsts.voltage);
    }

    public void stop(){
        drivingMotor.set(0);
        turningMotor.set(0);
    }

    @Override
    public void periodic(){
        // kp = SmartDashboard.getNumber("kP", 0);
        // SmartDashboard.putNumber("kP", kp);
        // ki = SmartDashboard.getNumber("kI", 0);
        // SmartDashboard.putNumber("kI", ki);
        // kd = SmartDashboard.getNumber("kD", 0);
        // SmartDashboard.putNumber("kD", kd);

        // turningPID.setPID(kp, ki, kd);

        SmartDashboard.putNumber("Swerve["+absoluteEncoder.getDeviceID()+"] turning enc", getTurningPosition());
        SmartDashboard.putNumber("Swerve["+absoluteEncoder.getDeviceID()+"] CANCoder", getAbsoluteEncoder());
        SmartDashboard.putNumber("Swerve["+absoluteEncoder.getDeviceID()+"] Drive Speed", getDriveSpeed());
        SmartDashboard.putNumber("Swerve["+absoluteEncoder.getDeviceID()+"] NEO Output", drivingMotor.getMotorOutputPercent());
    }

    // constructor
    // double solenoid, neo550 (CANSparkMax), relative encoder
    // methods to close, open
    // rotate clockwise rotate counter clockwise

}
