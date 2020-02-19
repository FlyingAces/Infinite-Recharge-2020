package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import frc.robot.config.RobotMap;


public class PneumaticSubsystem implements Subsystem {
    private static PneumaticSubsystem _instance;

    private Compressor _compressor;
    private DoubleSolenoid _elevatorSolenoid;
    private DoubleSolenoid _aimSolenoid;
    private SolenoidState _state;

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

    private PneumaticSubsystem(){
        _compressor = new Compressor(RobotMap.Pneumatics.COMPRESSOR_MODULE.getChannel());
        _compressor.setClosedLoopControl(true);

        _elevatorSolenoid = new DoubleSolenoid(RobotMap.Pneumatics.ELEVATOR_SOLENOID_FWD_CHANNEL.getChannel(), RobotMap.Pneumatics.ELEVATOR_SOLENOID_REV_CHANNEL.getChannel());
        _aimSolenoid = new DoubleSolenoid(RobotMap.Pneumatics.INTAKE_SOLENOID_FWD_CHANNEL.getChannel(), RobotMap.Pneumatics.INTAKE_SOLENOID_REV_CHANNEL.getChannel());

        _state = SolenoidState.OFF;
    }


    public static PneumaticSubsystem getInstance(){
        if(_instance == null)
            _instance = new PneumaticSubsystem();

        return _instance;
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
        _elevatorSolenoid.set(SolenoidState.OFF.getState());
        _aimSolenoid.set(SolenoidState.OFF.getState());
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
                return _aimSolenoid.get() == SolenoidState.FORWARD.getState();
            case REVERSE:
                return _aimSolenoid.get() == SolenoidState.REVERSE.getState();
            default:
                return true;
        }
    }

    @Override
    public void setDefaultCommand(Command defaultCommand) {

    }
}
