package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class PneumaticSubsystem extends Subsystem {
    private static PneumaticSubsystem _instance;

    private Compressor _compressor;
    private DoubleSolenoid _elevatorSolenoid;
    private DoubleSolenoid _aimSolenoid;

    public enum SolenoidState {
        OFF(DoubleSolenoid.Value.kOff),
        FORWARD(DoubleSolenoid.Value.kForward),
        REVERSE(DoubleSolenoid.Value.kReverse);

        SolenoidState(DoubleSolenoid.Value state) {
            _state = state;
        }

        private DoubleSolenoid.Value _state;

        public DoubleSolenoid.Value getState() {
            return _state;
        }
    }

    private SolenoidState _state;

    private PneumaticSubsystem(){
        _compressor = new Compressor(0);
        _compressor.setClosedLoopControl(true);

        _elevatorSolenoid = new DoubleSolenoid(0, 2);
        _aimSolenoid = new DoubleSolenoid(1, 3);

        _state = SolenoidState.OFF;
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


    public SolenoidState getSolenoidState(){
         return _state;
    }
    public void runElevator(SolenoidState state){
        _state = state;
        _elevatorSolenoid.set(_state.getState());
    }

    public boolean isElevatorFinished(){
        switch (_state){
            case OFF:
                return true;
            case FORWARD:
                return _elevatorSolenoid.get() == DoubleSolenoid.Value.kForward;
            case REVERSE:
                return _elevatorSolenoid.get() == DoubleSolenoid.Value.kReverse;
            default:
                return true;
        }
    }
    public void runAimer(SolenoidState state){
        _state = state;
        _aimSolenoid.set(_state.getState());
    }

    public boolean isAimerFinished(){
        switch (_state){
            case OFF:
                return true;
            case FORWARD:
                return _aimSolenoid.get() == DoubleSolenoid.Value.kForward;
            case REVERSE:
                return _aimSolenoid.get() == DoubleSolenoid.Value.kReverse;
            default:
                return true;
        }

    }

    @Override
    protected void initDefaultCommand(){}
}


