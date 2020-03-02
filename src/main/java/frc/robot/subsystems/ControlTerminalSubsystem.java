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


public class ControlTerminalSubsystem implements Subsystem {
    private static ControlTerminalSubsystem _instance;

    public static enum ColorTypes {
        RED(ColorMatch.makeColor(0.561, 0.232, 0.114)),
        BLUE(ColorMatch.makeColor(0.143, 0.427, 0.429)),
        YELLOW(ColorMatch.makeColor(0.361, 0.524, 0.113)),
        GREEN(ColorMatch.makeColor(0.197, 0.561, 0.240));

        private ColorTypes(Color color) {
            _color = color;
        }

        private Color _color;

        private Color getColor() {
            return _color;
        }
    }

    private Compressor _compressor;
    private DoubleSolenoid _elevatorSolenoid;
    private SolenoidState _state;

    private TalonSRX _controlTerminal;

    private final I2C.Port _i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 _colorSensor = new ColorSensorV3(_i2cPort);
    private final ColorMatch _colorMatcher = new ColorMatch();

    public enum SolenoidState {
        OFF(DoubleSolenoid.Value.kOff),
        UP(DoubleSolenoid.Value.kForward),
        DOWN(DoubleSolenoid.Value.kReverse);

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

        _state = SolenoidState.OFF;
        solenoidOff();

        _controlTerminal = controlTerminal;

        _controlTerminal.configFactoryDefault();
        _controlTerminal.setNeutralMode(NeutralMode.Brake);
        _controlTerminal.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.configPeakOutputForward(1.0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.configPeakOutputReverse(-1.0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.setSelectedSensorPosition(-(_controlTerminal.getSensorCollection().getPulseWidthPosition() & 0xfff), 0, RobotMap.K_TIMEOUT_MS);
        _controlTerminal.getSensorCollection().setQuadraturePosition(0, RobotMap.K_TIMEOUT_MS);

        _colorMatcher.addColorMatch(ColorTypes.BLUE.getColor());
        _colorMatcher.addColorMatch(ColorTypes.YELLOW.getColor());
        _colorMatcher.addColorMatch(ColorTypes.GREEN.getColor());
        _colorMatcher.addColorMatch(ColorTypes.RED.getColor());
    }

    public static ControlTerminalSubsystem getInstance(){
        if(_instance == null)
            _instance = new ControlTerminalSubsystem();

        return _instance;
    }

    public Color getDetectedColor() {
        return _colorSensor.getColor();
    }

    public ColorTypes getColorType() {
        ColorMatchResult match = _colorMatcher.matchClosestColor(getDetectedColor());

        if (match.color == ColorTypes.BLUE.getColor()) {
            return ColorTypes.BLUE;
        } else if (match.color == ColorTypes.RED.getColor()) {
            return ColorTypes.RED;
        } else if (match.color == ColorTypes.GREEN.getColor()) {
            return ColorTypes.GREEN;
        } else if (match.color == ColorTypes.YELLOW.getColor()) {
            return ColorTypes.YELLOW;
        }
        return null;
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
            case UP:
                return _elevatorSolenoid.get() == DoubleSolenoid.Value.kForward;
            case DOWN:
                return _elevatorSolenoid.get() == DoubleSolenoid.Value.kReverse;
            default:
                return true;
        }
    }

    @Override
    public void setDefaultCommand(Command defaultCommand) {

    }
}
