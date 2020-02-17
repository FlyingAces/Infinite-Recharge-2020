package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.controller.PIDController;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

import frc.robot.config.RobotMap;
import frc.robot.util.Conversions;


public class TestSubsystem extends PIDSubsystem {
	private static TestSubsystem _instance;

	private TalonFX _testMotor;

	public TestSubsystem() {
		super(new PIDController(20.0, 0.1, 0.0));

		WPI_TalonFX testMotor = new WPI_TalonFX(RobotMap.Talon.TEST.getChannel());

		_testMotor = testMotor;

		TalonFXConfiguration config = new TalonFXConfiguration();
		config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
		_testMotor.configAllSettings(config);
		_testMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, RobotMap.K_TIMEOUT_MS);
		_testMotor.setInverted(false);
	}

	public void runMotor(double speed) {
		_testMotor.set(ControlMode.PercentOutput, speed);
	}

	public double getMotorPosition() {
		return _testMotor.getSelectedSensorPosition(0);
	}

	public void zeroPosition() {
		_testMotor.setSelectedSensorPosition(0);
	}

	public double getMotorVelocity() {
		return _testMotor.getSelectedSensorVelocity(0);
	}

	public static TestSubsystem getInstance() {
		if (_instance == null)
			_instance = new TestSubsystem();
		return _instance;
	}

	@Override
	public void setDefaultCommand(Command defaultCommand) {

	}

	@Override
	protected void useOutput(double output, double setpoint) {
		double percentOutput = Conversions.encoderPositionToInches(Math.abs(setpoint) - Math.abs(output)) / 12.0;
		_testMotor.set(ControlMode.PercentOutput, percentOutput);
	}

	@Override
	protected double getMeasurement() {
		return _testMotor.getSelectedSensorPosition(0);
	}
}
