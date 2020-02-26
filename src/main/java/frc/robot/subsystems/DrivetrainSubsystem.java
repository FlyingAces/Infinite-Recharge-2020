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
	private TalonFX _leftMaster;
	private TalonFX _leftSlave;
	private TalonFX _rightMaster;
	private TalonFX _rightSlave;

	private DifferentialDrive _wheels;
	private CommandType _type;
	private double _pidZoneBuffer;

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

		_leftMaster = leftMaster;
		_rightMaster = rightMaster;

		TalonFXConfiguration config = new TalonFXConfiguration();
		config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;

		_leftMaster.configAllSettings(config);
		_leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, RobotMap.K_TIMEOUT_MS);
		_leftMaster.setInverted(false);
		_leftMaster.setSelectedSensorPosition(0);

		_rightMaster.configAllSettings(config);
		_rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, RobotMap.K_TIMEOUT_MS);
		_rightMaster.setInverted(true);
		_rightMaster.setSelectedSensorPosition(0);


		_pidZoneBuffer = 12.0; // Inches

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
		_leftMaster.setSelectedSensorPosition(0);
		_rightMaster.setSelectedSensorPosition(0);
	}

	public double getCurrentLeftPosition() {
		return _leftMaster.getSelectedSensorPosition(0);
	}

	public double getCurrentLeftVelocity() {
		return _leftMaster.getSelectedSensorVelocity(0);
	}

	public double getCurrentRightPosition() {
		return _rightMaster.getSelectedSensorPosition(0);
	}

	public double getCurrentRightVelocity() {
		return _rightMaster.getSelectedSensorVelocity(0);
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

	/***
	 * Set the zone in which the PID output takes effect.
	 * The PID zone is 0.0 by default.
	 * @param inches The effective PID zone in inches.
	 */
	public void setPIDZoneBuffer(double inches) {
		_pidZoneBuffer = inches;
	}

	@Override
	public void setSetpoint(double dis){
		if(_rightMaster != null && _leftMaster != null) {
			zeroDrivetrain();

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
		System.out.println("Output: " + output + " || setpoint: " + setpoint);

		double percentOutput = Conversions.encoderPositionToInches(Math.abs(output)) / _pidZoneBuffer;
		System.out.println("Percent output: " + percentOutput);
		System.out.println("Left vel.: " + getCurrentLeftVelocity() + " || Right vel.: " + getCurrentRightVelocity());

		double leftMoveValue = (percentOutput > 1)? 1.0 : percentOutput;
		double rightMoveValue = (percentOutput > 1)? 1.0 : percentOutput;

		double rightSpeed = Math.abs(getCurrentRightVelocity());
		double leftSpeed = Math.abs(getCurrentLeftVelocity());

		if(rightSpeed > leftSpeed)
			rightMoveValue *= leftSpeed / rightSpeed;
		else if(leftSpeed > rightSpeed)
			leftMoveValue *= rightSpeed / leftSpeed;

		if (output < 0) {
			leftMoveValue *= -1;
			rightMoveValue *= -1;
		}
		System.out.println("Left: " + leftMoveValue + " || Right: " + rightMoveValue);


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
		//System.out.println("Getting measurement");

		double setpoint = getController().getSetpoint();

		if(Math.abs(setpoint - getCurrentLeftPosition()) < Math.abs(setpoint - getCurrentRightPosition())) {
			return getCurrentRightPosition();
		} else {
			return getCurrentLeftPosition();
		}
	}
}
