package frc.robot.commands.autonomouspaths;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.commands.DriveToCommand;
import frc.robot.commands.RunElevatorCommand;
import frc.robot.commands.TurnToCommand;


public class RunChaCha extends SequentialCommandGroup {
	public RunChaCha() {
		super(
				new DriveToCommand(12),
				new WaitCommand(0.3),
				new DriveToCommand(-12),
				new WaitCommand(0.3),
				new TurnToCommand(360),
				new WaitCommand(0.3),
				new RunElevatorCommand(),
				new WaitCommand(0.3),
				new RunElevatorCommand(),
				new WaitCommand(0.3),
				new RunElevatorCommand(),
				new WaitCommand(0.3),
				new RunElevatorCommand()
				);
	}
}
