package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

import frc.robot.commands.DriveRobot;
import frc.robot.config.MotorSpeeds;
import frc.robot.config.RobotMap;
import frc.robot.util.Conversions;


public class DrivetrainSubsystem extends PIDSubsystem {
	private static DrivetrainSubsystem _instance;
	private TalonSRX _leftWithEncoder;
	private TalonSRX _rightWithEncoder;
	private DifferentialDrive _wheels;
	private CommandType _type;

	public enum CommandType {
		STRAIGHT, TURN;
	}

	private DrivetrainSubsystem() {
		super(new PIDController(1.0, 0.0, 0.0));

		WPI_TalonSRX leftMaster = new WPI_TalonSRX(RobotMap.Talon.LEFT_MASTER.getChannel());
		WPI_TalonSRX rightMaster = new WPI_TalonSRX(RobotMap.Talon.RIGHT_MASTER.getChannel());
		WPI_TalonSRX leftSlave = new WPI_TalonSRX(RobotMap.Talon.LEFT_SLAVE.getChannel());
		WPI_TalonSRX rightSlave = new WPI_TalonSRX(RobotMap.Talon.RIGHT_SLAVE.getChannel());

		SpeedControllerGroup leftGroup = new SpeedControllerGroup(leftMaster, leftSlave);
		SpeedControllerGroup rightGroup = new SpeedControllerGroup(rightMaster, rightSlave);

		_type = CommandType.STRAIGHT;

		_wheels = new DifferentialDrive(leftGroup, rightGroup);
		_wheels.setSafetyEnabled(false);

		_leftWithEncoder = leftMaster;
		_rightWithEncoder = rightMaster;

		_leftWithEncoder.configFactoryDefault();
		_leftWithEncoder.setNeutralMode(NeutralMode.Brake);
		_leftWithEncoder.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
		_leftWithEncoder.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
		_leftWithEncoder.configPeakOutputForward(1.0, RobotMap.K_TIMEOUT_MS);
		_leftWithEncoder.configPeakOutputReverse(-1.0, RobotMap.K_TIMEOUT_MS);
		_leftWithEncoder.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, RobotMap.K_TIMEOUT_MS);
		_leftWithEncoder.setSensorPhase(true);
		_leftWithEncoder.setSelectedSensorPosition(-(_leftWithEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff), 0, RobotMap.K_TIMEOUT_MS);
		_leftWithEncoder.getSensorCollection().setQuadraturePosition(0, RobotMap.K_TIMEOUT_MS);

		_rightWithEncoder.configFactoryDefault();
		_rightWithEncoder.setNeutralMode(NeutralMode.Brake);
		_rightWithEncoder.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
		_rightWithEncoder.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
		_rightWithEncoder.configPeakOutputForward(1.0, RobotMap.K_TIMEOUT_MS);
		_rightWithEncoder.configPeakOutputReverse(-1.0, RobotMap.K_TIMEOUT_MS);
		_rightWithEncoder.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, RobotMap.K_TIMEOUT_MS);
		_rightWithEncoder.setSensorPhase(true);
		_rightWithEncoder.setSelectedSensorPosition((_rightWithEncoder.getSensorCollection().getPulseWidthPosition() & 0xfff), 0, RobotMap.K_TIMEOUT_MS);
		_rightWithEncoder.getSensorCollection().setQuadraturePosition(0, RobotMap.K_TIMEOUT_MS);

		getController().setTolerance(10.0); // Encoder ticks
	}

	public void arcadeDrive(double moveValue, double rotateValue) {
		if (DriverStation.getInstance().isOperatorControl()) {
			moveValue *= MotorSpeeds.TELEOP_SPEED_MULTIPLIER;
		} else {
			moveValue *= MotorSpeeds.AUTONOMOUS_SPEED_MULTIPLIER;
		}

		_wheels.arcadeDrive(moveValue * MotorSpeeds.DRIVE_ACCELERATION_SPEED, rotateValue * MotorSpeeds.DRIVE_TURN_SPEED);
	}

	public void tankDrive(double leftMoveValue, double rightMoveValue) {
		if(DriverStation.getInstance().isOperatorControl()) {
			rightMoveValue *= MotorSpeeds.TELEOP_SPEED_MULTIPLIER;
			leftMoveValue *= MotorSpeeds.TELEOP_SPEED_MULTIPLIER;
		} else {
			rightMoveValue *= MotorSpeeds.AUTONOMOUS_SPEED_MULTIPLIER;
			leftMoveValue *= MotorSpeeds.AUTONOMOUS_SPEED_MULTIPLIER;
		}

		_wheels.tankDrive(leftMoveValue, rightMoveValue);
	}

	public static DrivetrainSubsystem getInstance(){
		if(_instance == null)
			_instance = new DrivetrainSubsystem();

		return _instance;
	}

	public int getCurrentLeftPosition() {
		return _leftWithEncoder.getSelectedSensorPosition(0);
	}

	public int getCurrentLeftVelocity() {
		return _leftWithEncoder.getSelectedSensorVelocity(0);
	}

	public int getCurrentRightPosition() {
		return _rightWithEncoder.getSelectedSensorPosition(0);
	}

	public int getCurrentRightVelocity() {
		return _rightWithEncoder.getSelectedSensorVelocity(0);
	}

	public boolean isOnTarget() {
		return getController().atSetpoint();
	}

	public void setCommandType(CommandType type) {
		_type = type;
	}

	public void setSetpoint(double dis, CommandType type) {
		setCommandType(type);
		setSetpoint(dis);
	}

	@Override
	public void setSetpoint(double dis){
		_rightWithEncoder.getSensorCollection().setQuadraturePosition(0, RobotMap.K_TIMEOUT_MS);
		_leftWithEncoder.getSensorCollection().setQuadraturePosition(0, RobotMap.K_TIMEOUT_MS);

		switch(_type) {
			case STRAIGHT:
				super.setSetpoint(Conversions.inchToEncoderPosition(dis));
				break;
			case TURN:
				super.setSetpoint(Conversions.angleToEncoderPosition(dis));
				break;
		}
	}

	@Override
	protected void useOutput(double output, double setpoint) {
		double leftMoveValue = output;
		double rightMoveValue = output;

		double rightSpeed = Math.abs(getCurrentRightVelocity());
		double leftSpeed = Math.abs(getCurrentLeftVelocity());

		if(rightSpeed > leftSpeed)
			rightMoveValue *= leftSpeed / rightSpeed;
		else if(leftSpeed > rightSpeed)
			leftMoveValue *= rightSpeed / leftSpeed;

		switch (_type) {
			case STRAIGHT:
				tankDrive(leftMoveValue, rightMoveValue);
				break;
			case TURN:
				tankDrive(leftMoveValue, -rightMoveValue);
				break;
		}
	}

	@Override
	protected double getMeasurement() {
		double setpoint = getController().getSetpoint();
		if(Math.abs(setpoint - getCurrentLeftPosition()) < Math.abs(setpoint - getCurrentRightPosition())) {
			return getCurrentRightPosition();
		} else {
			return getCurrentLeftPosition();
		}
	}
}
