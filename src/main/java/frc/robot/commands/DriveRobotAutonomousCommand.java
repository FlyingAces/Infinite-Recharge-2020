package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.autonomouspaths.RunChaCha;


public class DriveRobotAutonomousCommand extends SequentialCommandGroup {
	public DriveRobotAutonomousCommand() {
        super(
        		new DriveToCommand(84.0),
		        new RunChaCha()
		);
	}
}
