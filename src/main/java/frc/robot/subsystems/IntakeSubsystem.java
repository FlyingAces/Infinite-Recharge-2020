package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.config.RobotMap;

public class IntakeSubsystem extends Subsystem {
    private static IntakeSubsystem _instance;
    private TalonSRX _intake;

    public IntakeSubsystem() {
        super("IntakeSubsystem");

        _intake = new WPI_TalonSRX(RobotMap.Talon.INTAKE.getChannel());
        
        _intake.configFactoryDefault();
        _intake.setNeutralMode(NeutralMode.Brake);
        _intake.configNominalOutputForward(0, RobotMap.K_TIMEOUT_MS);
        _intake.configNominalOutputReverse(0, RobotMap.K_TIMEOUT_MS);
        _intake.configPeakOutputForward(1.0, RobotMap.K_TIMEOUT_MS);
        _intake.configPeakOutputReverse(-1.0, RobotMap.K_TIMEOUT_MS);
    }

    @Override
    protected void initDefaultCommand() {

    }
}
