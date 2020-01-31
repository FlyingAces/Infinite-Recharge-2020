package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatchResult;
import edu.wpi.first.wpilibj.I2C;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.config.RobotMap;


public class TestSubsystem implements Subsystem {
	private static TestSubsystem _instance;

	private TalonSRX _testMotor;
	private static final I2C.Port i2cPort = I2C.Port.kOnboard;

	private static final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
	private static final ColorMatch m_colorMatcher = new ColorMatch();


	public TestSubsystem() {
		m_colorMatcher.addColorMatch(RobotMap.kBlueTarget);
		m_colorMatcher.addColorMatch(RobotMap.kGreenTarget);
		m_colorMatcher.addColorMatch(RobotMap.kRedTarget);
		m_colorMatcher.addColorMatch(RobotMap.kYellowTarget);

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

	public static String detectColors(){
		Color detectedColor = m_colorSensor.getColor();

		String colorString;
		ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

		if (match.color == RobotMap.kBlueTarget) {
			colorString = "Blue";


		} else if (match.color == RobotMap.kRedTarget) {
			colorString = "Red";

		} else if (match.color == RobotMap.kGreenTarget) {
			colorString = "Green";

		} else if (match.color == RobotMap.kYellowTarget) {
			colorString = "Yellow";

		} else {
			colorString = "Unknown";
		}
		SmartDashboard.putNumber("Red", detectedColor.red);
		SmartDashboard.putNumber("Green", detectedColor.green);
		SmartDashboard.putNumber("Blue", detectedColor.blue);
		SmartDashboard.putNumber("Confidence", match.confidence);
		SmartDashboard.putString("Detected Color", colorString);
		return colorString;
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
