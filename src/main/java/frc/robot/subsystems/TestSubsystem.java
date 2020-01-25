package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.config.RobotMap;


public class TestSubsystem implements Subsystem {
	private static TestSubsystem _instance;

	private TalonSRX _testMotor;

	public TestSubsystem() {
		WPI_TalonSRX testMotor = new WPI_TalonSRX(RobotMap.Talon.TEST.getChannel());

		_testMotor = testMotor;

		_testMotor.configFactoryDefault();
		_testMotor.setNeutralMode(NeutralMode.Brake);
		_testMotor.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
		_testMotor.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
		_testMotor.configPeakOutputForward(1.0, RobotMap.K_TIMEOUT_MS);
		_testMotor.configPeakOutputReverse(-1.0, RobotMap.K_TIMEOUT_MS);
		_testMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, RobotMap.K_TIMEOUT_MS);
		_testMotor.setSensorPhase(true);
		_testMotor.setSelectedSensorPosition(-(_testMotor.getSensorCollection().getPulseWidthPosition() & 0xfff), 0, RobotMap.K_TIMEOUT_MS);
		_testMotor.getSensorCollection().setQuadraturePosition(0, RobotMap.K_TIMEOUT_MS);
	}

	public void runMotor(double speed) {
		_testMotor.set(ControlMode.PercentOutput, speed);
	}

	public int getMotorPosition() {
		return _testMotor.getSelectedSensorPosition();
	}

	public int getMotorVelocity() {
		return _testMotor.getSelectedSensorVelocity();
	}

	public static TestSubsystem getInstance() {
		if (_instance == null)
			_instance = new TestSubsystem();
		return _instance;
	}

	@Override
	public void setDefaultCommand(Command defaultCommand) {

	}
}
