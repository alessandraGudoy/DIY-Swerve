package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Compressor;

public class Pneumatics extends SubsystemBase {

  private DoubleSolenoid piston;
  private Compressor compressor;
  // private Solenoid piston;

  public Pneumatics() {
    piston = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0, 8);
    piston.set(Value.kReverse);

    // piston = new Solenoid(PneumaticsModuleType.CTREPCM, 0);

    compressor = new Compressor(PneumaticsModuleType.REVPH);
    compressor.enableDigital();
  }

  public void forward(){
     piston.set(Value.kForward);
  }

  public void reverse(){
     piston.set(Value.kReverse);
  }

  public void change(/*Value newVal*/){
    //piston.set(newVal);
    piston.toggle();
  }

  public Value getvalue(){
    return piston.get();
  }

  @Override
  public void periodic() {
    SmartDashboard.putString("FWD / REV", String.valueOf(piston.get()));
    //SmartDashboard.putBoolean("ON / OFF", getValue());

    SmartDashboard.putString("FWD / REV toString()", piston.get().toString());


    SmartDashboard.putNumber("COMPRESSOR ANALOG VOLTAGE", compressor.getAnalogVoltage());
    SmartDashboard.putNumber("COMPRESSOR PRESSURE", compressor.getPressure());
  }
}