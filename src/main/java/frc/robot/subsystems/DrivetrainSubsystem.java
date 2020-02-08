package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj2.command.PIDSubsystem;

import frc.robot.config.MotorSpeeds;
import frc.robot.config.RobotMap;
import frc.robot.util.Conversions;


public class DrivetrainSubsystem extends PIDSubsystem {
	private static DrivetrainSubsystem _instance;
	private TalonFX _leftWithEncoder;
	private TalonFX _rightWithEncoder;
	private DifferentialDrive _wheels;
	private CommandType _type;

	public enum CommandType {
		STRAIGHT, TURN;
	}

	private DrivetrainSubsystem() {
		super(new PIDController(1.0, 0.0, 0.0));

		WPI_TalonFX leftMaster = new WPI_TalonFX(RobotMap.Talon.LEFT_MASTER.getChannel());
		WPI_TalonFX rightMaster = new WPI_TalonFX(RobotMap.Talon.RIGHT_MASTER.getChannel());
		WPI_TalonFX leftSlave = new WPI_TalonFX(RobotMap.Talon.LEFT_SLAVE.getChannel());
		WPI_TalonFX rightSlave = new WPI_TalonFX(RobotMap.Talon.RIGHT_SLAVE.getChannel());

		SpeedControllerGroup leftGroup = new SpeedControllerGroup(leftMaster, leftSlave);
		SpeedControllerGroup rightGroup = new SpeedControllerGroup(rightMaster, rightSlave);

		_type = CommandType.STRAIGHT;

		_wheels = new DifferentialDrive(leftGroup, rightGroup);
		_wheels.setSafetyEnabled(false);

		_leftWithEncoder = leftMaster;
		_rightWithEncoder = rightMaster;

		TalonFXConfiguration config = new TalonFXConfiguration();
		config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;

		_leftWithEncoder.configAllSettings(config);
		_leftWithEncoder.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, RobotMap.K_TIMEOUT_MS);
		_leftWithEncoder.setInverted(false);
		_leftWithEncoder.setSelectedSensorPosition(0);

		_rightWithEncoder.configAllSettings(config);
		_rightWithEncoder.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, RobotMap.K_TIMEOUT_MS);
		_rightWithEncoder.setInverted(false);
		_rightWithEncoder.setSelectedSensorPosition(0);

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

		_wheels.tankDrive(leftMoveValue * MotorSpeeds.DRIVE_ACCELERATION_SPEED, rightMoveValue * MotorSpeeds.DRIVE_ACCELERATION_SPEED);
	}

	public static DrivetrainSubsystem getInstance(){
		if(_instance == null)
			_instance = new DrivetrainSubsystem();

		return _instance;
	}

	public void zeroDrivetrain() {
		_leftWithEncoder.setSelectedSensorPosition(0);
		_rightWithEncoder.setSelectedSensorPosition(0);
	}

	public double getCurrentLeftPosition() {
		return _leftWithEncoder.getSelectedSensorPosition(0);
	}

	public double getCurrentLeftVelocity() {
		return _leftWithEncoder.getSelectedSensorVelocity(0);
	}

	public double getCurrentRightPosition() {
		return _rightWithEncoder.getSelectedSensorPosition(0);
	}

	public double getCurrentRightVelocity() {
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
		if(_rightWithEncoder != null && _leftWithEncoder != null) {
			_rightWithEncoder.getSensorCollection().setIntegratedSensorPosition(0, RobotMap.K_TIMEOUT_MS);
			_leftWithEncoder.getSensorCollection().setIntegratedSensorPosition(0, RobotMap.K_TIMEOUT_MS);

			switch (_type) {
				case STRAIGHT:
					super.setSetpoint(Conversions.inchToEncoderPosition(dis));
					break;
				case TURN:
					super.setSetpoint(Conversions.angleToEncoderPosition(dis));
					break;
			}
			System.out.println("total : " + Conversions.inchToEncoderPosition(dis));
		} else
			super.setSetpoint(dis);
	}

	@Override
	protected void useOutput(double output, double setpoint) {
		System.out.println("Output : " + output + ", setpoint : " + setpoint);
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
		System.out.println("Getting measurement");
		double setpoint = getController().getSetpoint();
		if(Math.abs(setpoint - getCurrentLeftPosition()) < Math.abs(setpoint - getCurrentRightPosition())) {
			return getCurrentRightPosition();
		} else {
			return getCurrentLeftPosition();
		}
	}
}
