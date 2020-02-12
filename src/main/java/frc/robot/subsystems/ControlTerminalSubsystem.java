package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;


import frc.robot.config.RobotMap;


public class ControlTerminalSubsystem implements Subsystem {
	private static ControlTerminalSubsystem _instance;
	private TalonSRX _controlTerminal;
	private TalonSRX _elevator;

	private final I2C.Port _i2cPort = I2C.Port.kOnboard;
	private final ColorSensorV3 _colorSensor = new ColorSensorV3(_i2cPort);
	private final ColorMatch _colorMatcher = new ColorMatch();

	private ElevatorDirection _elevatorDirection;

	public enum ElevatorDirection {
		UP(1),
		DOWN(-1);

		ElevatorDirection(double direction) {
			_direction = direction;
		}

		private double _direction;

		public double getDirection() {
			return _direction;
		}
	}

	public ControlTerminalSubsystem() {
		WPI_TalonSRX controlTerminal = new WPI_TalonSRX(RobotMap.Talon.CONTROL_TERMINAL.getChannel());
		WPI_TalonSRX elevator = new WPI_TalonSRX(RobotMap.Talon.CONTROL_TERMINAL_ELEVATOR.getChannel());

		_controlTerminal = controlTerminal;
		_elevator = elevator;

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

		_elevator.configFactoryDefault();
		_elevator.setNeutralMode(NeutralMode.Brake);
		_elevator.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
		_elevator.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
		_elevator.configPeakOutputForward(1.0, RobotMap.K_TIMEOUT_MS);
		_elevator.configPeakOutputReverse(-1.0, RobotMap.K_TIMEOUT_MS);
		_elevator.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, RobotMap.K_TIMEOUT_MS);
		_elevator.setSensorPhase(true);
		_elevator.setSelectedSensorPosition(-(_controlTerminal.getSensorCollection().getPulseWidthPosition() & 0xfff), 0, RobotMap.K_TIMEOUT_MS);
		_elevator.getSensorCollection().setQuadraturePosition(0, RobotMap.K_TIMEOUT_MS);

		_elevator.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, RobotMap.K_TIMEOUT_MS);
		_elevator.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, RobotMap.K_TIMEOUT_MS);


		_colorMatcher.addColorMatch(RobotMap.BLUE_TARGET);
		_colorMatcher.addColorMatch(RobotMap.GREEN_TARGET);
		_colorMatcher.addColorMatch(RobotMap.RED_TARGET);
		_colorMatcher.addColorMatch(RobotMap.YELLOW_TARGET);

		_elevatorDirection = ElevatorDirection.DOWN;
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
		if (_elevator.isRevLimitSwitchClosed() == 1 || _elevatorDirection == ElevatorDirection.DOWN) {
			_controlTerminal.set(ControlMode.PercentOutput, 0.0);
		} else {
			_controlTerminal.set(ControlMode.PercentOutput, speed);
		}
	}

	public ElevatorDirection getElevatorDirection() {
		return _elevatorDirection;
	}

	public boolean isElevatorFinished() {
		switch (_elevatorDirection ) {
			case DOWN:
				return _elevator.isRevLimitSwitchClosed() == 1;
			case UP:
				return _elevator.isFwdLimitSwitchClosed() == 1;
			default:
				return true;
		}
	}

	public void runElevator(ElevatorDirection direction){
		_elevatorDirection = direction;
		_elevator.set(ControlMode.PercentOutput, _elevatorDirection.getDirection());
	}

	public void stopElevator() {
		_elevator.set(ControlMode.PercentOutput, 0.0);
	}

	public static ControlTerminalSubsystem getInstance() {
		if (_instance == null)
			_instance = new ControlTerminalSubsystem();
		return _instance;
	}

	@Override
	public void setDefaultCommand(Command defaultCommand) {

	}
}
