package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj2.command.PIDSubsystem;

import frc.robot.config.MotorSpeeds;
import frc.robot.config.RobotMap;
import frc.robot.util.Conversions;


public class DrivetrainSubsystem extends PIDSubsystem {
	private static DrivetrainSubsystem _instance;
	private TalonFX _leftMaster;
	private TalonFX _rightMaster;
	private DifferentialDrive _wheels;
	private CommandType _type;
	private NetworkTable _table;

	private double _prevOutput;
	private double _leftPrevTankOutput;
	private double _rightPrevTankOutput;

	public enum CommandType {
		STRAIGHT, TURN;
	}

	private DrivetrainSubsystem() {
		super(new PIDController(0.00025, 0.00001, 0.0));

		WPI_TalonFX leftMaster = new WPI_TalonFX(RobotMap.Talon.LEFT_MASTER.getChannel());
		WPI_TalonFX rightMaster = new WPI_TalonFX(RobotMap.Talon.RIGHT_MASTER.getChannel());
		WPI_TalonFX leftSlave = new WPI_TalonFX(RobotMap.Talon.LEFT_SLAVE.getChannel());
		WPI_TalonFX rightSlave = new WPI_TalonFX(RobotMap.Talon.RIGHT_SLAVE.getChannel());

		leftMaster.configFactoryDefault();
		leftSlave.configFactoryDefault();
		rightMaster.configFactoryDefault();
		leftSlave.configFactoryDefault();

		rightSlave.follow(rightMaster);
		leftSlave.follow(leftMaster);

		rightMaster.setInverted(TalonFXInvertType.Clockwise);
		leftMaster.setInverted(TalonFXInvertType.CounterClockwise);

		rightSlave.setInverted(InvertType.FollowMaster);
		leftSlave.setInverted(InvertType.FollowMaster);

		_type = CommandType.STRAIGHT;

		_wheels = new DifferentialDrive(leftMaster, rightMaster);
		_wheels.setSafetyEnabled(false);
		_wheels.setRightSideInverted(false);

		_leftMaster = leftMaster;
		_rightMaster = rightMaster;

		TalonFXConfiguration config = new TalonFXConfiguration();
		config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;

		_leftMaster.configAllSettings(config);
		_leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, RobotMap.K_TIMEOUT_MS);
		_leftMaster.setSelectedSensorPosition(0);

		_rightMaster.configAllSettings(config);
		_rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, RobotMap.K_TIMEOUT_MS);
		_rightMaster.setSelectedSensorPosition(0);

		_table = NetworkTableInstance.getDefault().getTable("limelight");

		getController().setTolerance(Conversions.inchToEncoderPosition(.1)); // Encoder ticks
	}

	private double linearAccelerationControl(double output, double prevOutput) {
		double newOutput = output;

		if(output > 0) {
			if (prevOutput > 0 && (output - prevOutput) > MotorSpeeds.GEAR_SHIFT_INCREMENT)
				newOutput = prevOutput * (MotorSpeeds.GEAR_SHIFT_INCREMENT + 1);
			else if(prevOutput == 0)
				newOutput = MotorSpeeds.GEAR_SHIFT_INCREMENT;

		} else if (output < 0) {
			if (prevOutput < 0 && (output - prevOutput) < -MotorSpeeds.GEAR_SHIFT_INCREMENT)
				newOutput = prevOutput * (MotorSpeeds.GEAR_SHIFT_INCREMENT + 1);
			else if(prevOutput == 0)
				newOutput = -MotorSpeeds.GEAR_SHIFT_INCREMENT;
		}

		if (newOutput > 0 && newOutput < 0.25) {
			newOutput = 0.25;
		} else if (newOutput  < 0 && newOutput > -0.25) {
			newOutput = -0.25;
		}

		return newOutput;
	}

	public void arcadeDrive(double moveValue, double rotateValue) {
		if (DriverStation.getInstance().isOperatorControl()) {
			moveValue *= MotorSpeeds.TELEOP_SPEED_MULTIPLIER;
		} else {
			moveValue *= MotorSpeeds.AUTONOMOUS_SPEED_MULTIPLIER;
		}

		switch (ControlTerminalSubsystem.getInstance().getSolenoidState()) {
			case DOWN:
				moveValue *= MotorSpeeds.DRIVETRAIN_CRAWL_MULTIPLIER;
				rotateValue *= MotorSpeeds.DRIVETRAIN_CRAWL_MULTIPLIER;
			default:
				break;
		}

		moveValue = linearAccelerationControl(moveValue * MotorSpeeds.DRIVE_ACCELERATION_SPEED, _prevOutput);
		_prevOutput = moveValue;

		double offsetLeftTurn = Math.abs(MotorSpeeds.LEFT_TURN_OFFSET_MULTIPLIER * moveValue);

		_wheels.arcadeDrive(moveValue, (rotateValue * MotorSpeeds.DRIVE_TURN_SPEED) - offsetLeftTurn);
	}

	public void tankDrive(double leftMoveValue, double rightMoveValue) {
		if(DriverStation.getInstance().isOperatorControl()) {
			rightMoveValue *= MotorSpeeds.TELEOP_SPEED_MULTIPLIER;
			leftMoveValue *= MotorSpeeds.TELEOP_SPEED_MULTIPLIER;
		} else {
			rightMoveValue *= MotorSpeeds.AUTONOMOUS_SPEED_MULTIPLIER;
			leftMoveValue *= MotorSpeeds.AUTONOMOUS_SPEED_MULTIPLIER;
		}

		rightMoveValue = linearAccelerationControl(rightMoveValue, _rightPrevTankOutput);
		leftMoveValue = linearAccelerationControl(leftMoveValue, _leftPrevTankOutput);

		_wheels.tankDrive(leftMoveValue , rightMoveValue);

		_leftPrevTankOutput = leftMoveValue;
		_rightPrevTankOutput = rightMoveValue;
	}

	public static DrivetrainSubsystem getInstance(){
		if(_instance == null)
			_instance = new DrivetrainSubsystem();

		return _instance;
	}

	public void resetEncoders() {
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
		return  (getCurrentLeftPosition() != 0 && getCurrentRightPosition() != 0) &&
				(getCurrentLeftVelocity() == 0 && getCurrentRightVelocity() == 0);
		//System.out.println("Set point: " + getController().getSetpoint() + " Tolerance: " + getController().to);
		//return getController().atSetpoint();
	}

	public void setCommandType(CommandType type) {
		_type = type;
	}

	public void setSetpoint(double dis, CommandType type) {
		setCommandType(type);

		resetEncoders();
		switch (_type) {
			case STRAIGHT:
				setSetpoint(Conversions.inchToEncoderPosition(dis));
				break;
			case TURN:
				setSetpoint(Conversions.angleToEncoderPosition(dis));
				break;
		}
	}

	@Override
	protected void useOutput(double output, double setpoint) {

		double leftMoveValue = (output > 1)? 1.0 : (output < -1)? -1.0 : output;
		double rightMoveValue = (output > 1)? 1.0 : (output < -1)? -1.0 : output;

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
		//tankDrive(leftMoveValue, rightMoveValue);
	}
	
	@Override
	protected double getMeasurement() {
		double setpoint = getController().getSetpoint();
		//System.out.println("Getting measurement setpoint:" +  setpoint + " left : " + getCurrentLeftPosition() + " right : " + getCurrentRightPosition());

		return getCurrentLeftPosition();
		/*
		if(Math.abs(setpoint - getCurrentLeftPosition()) < Math.abs(setpoint - getCurrentRightPosition())) {
			return getCurrentRightPosition();
		} else {
			return getCurrentLeftPosition();
		}
		 */
	}
}
