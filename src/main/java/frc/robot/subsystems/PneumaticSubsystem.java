package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class PneumaticSubsystem extends Subsystem {
    private static PneumaticSubsystem _instance;

    private Compressor _compressor;
    private DoubleSolenoid _elevatorSolenoid;
    private DoubleSolenoid _aimSolenoid;

    private PneumaticSubsystem(){
        _compressor = new Compressor(0);
        _compressor.setClosedLoopControl(true);

        _elevatorSolenoid = new DoubleSolenoid(0, 2);
        _aimSolenoid = new DoubleSolenoid(1, 3);
    }


    public static PneumaticSubsystem getInstance(){
        if(_instance == null)
            _instance = new PneumaticSubsystem();

        return _instance;
    }

    public boolean isCompressorEnable(){
        return _compressor.enabled();
    }

    public boolean isCompressorPressureSwitch(){
        return _compressor.getPressureSwitchValue();
    }

    public double getCompressorCurrent(){
        return _compressor.getCompressorCurrent();
    }

    public void compressorOff(){
        _compressor.setClosedLoopControl(false);
    }

    public void compressorOn(){
        _compressor.setClosedLoopControl(true);
    }

    public void solenoidOff(){
        _elevatorSolenoid.set(DoubleSolenoid.Value.kOff);
        _aimSolenoid.set(DoubleSolenoid.Value.kOff);
    }
    public void elevatorForward(){
        _elevatorSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void aimForward(){
        _aimSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void elevatorReverse(){
        _elevatorSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void aimReverse(){
        _aimSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    @Override
    protected void initDefaultCommand(){}
}


