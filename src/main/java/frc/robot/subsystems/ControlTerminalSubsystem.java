package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.util.Color;

import frc.robot.config.RobotMap;


public class ControlTerminalSubsystem extends Subsystem {
	private static ControlTerminalSubsystem _instance;
	private TalonSRX _controlTerminal;

	private final I2C.Port _i2cPort = I2C.Port.kOnboard;
	private final ColorSensorV3 _colorSensor = new ColorSensorV3(_i2cPort);
	private final ColorMatch _colorMatcher = new ColorMatch();

	private final Color _kBlueTarget = ColorMatch.makeColor(0.143, 0.227, 0.429); //g 427
	private final Color _kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
	private final Color _kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
	private final Color _kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

	public ControlTerminalSubsystem() {
		WPI_TalonSRX controlTerminal = new WPI_TalonSRX(RobotMap.Talon.CONTROL_TERMINAL.getChannel());

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

		_colorMatcher.addColorMatch(_kBlueTarget);
		_colorMatcher.addColorMatch(_kGreenTarget);
		_colorMatcher.addColorMatch(_kRedTarget);
		_colorMatcher.addColorMatch(_kYellowTarget);
	}

	public Color getDetectedColor() {
		return _colorSensor.getColor();
	}

	public String getColorString() {
		ColorMatchResult match = _colorMatcher.matchClosestColor(getDetectedColor());

		if (match.color == _kBlueTarget) {
			return "Blue";
		} else if (match.color == _kRedTarget) {
			return "Red";
		} else if (match.color == _kGreenTarget) {
			return "Green";
		} else if (match.color == _kYellowTarget) {
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

	public static ControlTerminalSubsystem getInstance() {
		if (_instance == null)
			_instance = new ControlTerminalSubsystem();
		return _instance;
	}

	@Override
	protected void initDefaultCommand() {

	}
}
