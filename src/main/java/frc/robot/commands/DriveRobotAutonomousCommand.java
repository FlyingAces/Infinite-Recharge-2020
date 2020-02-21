package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


public class DriveRobotAutonomousCommand extends SequentialCommandGroup {
	public DriveRobotAutonomousCommand() {
        super(
        		new DriveToCommand(0.0)
		);
	}
}
