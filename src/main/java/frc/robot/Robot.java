/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


import frc.robot.commands.CommandWithController;
import frc.robot.commands.DriveRobotAutonomousCommand;
import frc.robot.commands.TestCommand;
import frc.robot.subsystems.ControllerSubsystem;


public class Robot extends TimedRobot {
    private Command _teleopCommand;
    private Command _autonomousCommand;
    private Command _testCommand;

    @Override
    public void robotInit() {
        _teleopCommand = new CommandWithController();
        _autonomousCommand = new DriveRobotAutonomousCommand();
        _testCommand = new TestCommand(0);

        ControllerSubsystem.getInstance();
    }

    @Override
    public void testInit() {
        _testCommand.schedule();
    }

    @Override
    public void autonomousInit() {
        _autonomousCommand.schedule();
    }

    @Override
    public void teleopInit() {
        _teleopCommand.schedule();
    }

    @Override
    public void autonomousPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void teleopPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void testPeriodic() {
        CommandScheduler.getInstance().run();
    }
}
