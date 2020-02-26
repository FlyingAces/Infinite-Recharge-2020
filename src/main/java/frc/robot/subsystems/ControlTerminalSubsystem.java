package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import frc.robot.config.RobotMap;

import java.awt.*;


public class ControlTerminalSubsystem implements Subsystem {
    private static ControlTerminalSubsystem _instance;

    private Compressor _compressor;
    private DoubleSolenoid _elevatorSolenoid;
    private DoubleSolenoid _aimSolenoid;
    private SolenoidState _state;

    private TalonSRX _controlTerminal;

    private final I2C.Port _i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 _colorSensor = new ColorSensorV3(_i2cPort);
    private final ColorMatch _colorMatcher = new ColorMatch();


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

    private ControlTerminalSubsystem(){
        WPI_TalonSRX controlTerminal = new WPI_TalonSRX(RobotMap.Talon.CONTROL_TERMINAL.getChannel());

        _compressor = new Compressor(RobotMap.Pneumatics.COMPRESSOR_MODULE.getChannel());
        _compressor.setClosedLoopControl(true);

        _elevatorSolenoid = new DoubleSolenoid(RobotMap.Pneumatics.ELEVATOR_SOLENOID_FWD_CHANNEL.getChannel(), RobotMap.Pneumatics.ELEVATOR_SOLENOID_REV_CHANNEL.getChannel());
        _aimSolenoid = new DoubleSolenoid(RobotMap.Pneumatics.INTAKE_SOLENOID_FWD_CHANNEL.getChannel(), RobotMap.Pneumatics.INTAKE_SOLENOID_REV_CHANNEL.getChannel());

        _state = SolenoidState.OFF;
        _controlTerminal = controlTerminal;

        _controlTerminal.configFactoryDefault();
        _controlTerminal.setNeutralMode(NeutralMode.Brake);
        _controlTerminal.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.configPeakOutputForward(1.0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.configPeakOutputReverse(-1.0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.setSensorPhase(true);
        _controlTerminal.setSelectedSensorPosition(-(_controlTerminal.getSensorCollection().getPulseWidthPosition() & 0xfff), 0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.getSensorCollection().setQuadraturePosition(0, RobotMap.K_TIMEOUT_MS);

        _colorMatcher.addColorMatch(RobotMap.BLUE_TARGET);
        _colorMatcher.addColorMatch(RobotMap.GREEN_TARGET);
        _colorMatcher.addColorMatch(RobotMap.RED_TARGET);
        _colorMatcher.addColorMatch(RobotMap.YELLOW_TARGET);
    }


    public static ControlTerminalSubsystem getInstance(){
        if(_instance == null)
            _instance = new ControlTerminalSubsystem();

        return _instance;
    }

    public Color getDetectedColor() {
        return _colorSensor.getColor();
    }

    public String getColorString() {
        ColorMatchResult match = _colorMatcher.matchClosestColor(getDetectedColor());

        if (match.color == RobotMap.BLUE_TARGET) {
            return "Blue";
        } else if (match.color == RobotMap.RED_TARGET) {
            return "Red";
        } else if (match.color == RobotMap.GREEN_TARGET) {
            return "Green";
        } else if (match.color == RobotMap.YELLOW_TARGET) {
            return "Yellow";
        } else {
            return "Unknown";
        }
    }

    public int getEncoderPosition() {
        return _controlTerminal.getSelectedSensorPosition(0);
    }

    public void resetEncoder() {
        _controlTerminal.getSensorCollection().setQuadraturePosition(0, RobotMap.K_TIMEOUT_MS);
    }


    public void runControlTerminal(double speed) {
        _controlTerminal.set(ControlMode.PercentOutput, speed);
    }

    public boolean isCompressorPressureSwitch(){
        return _compressor.getPressureSwitchValue();
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
