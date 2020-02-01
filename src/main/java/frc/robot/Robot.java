/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.subsystems.ControlTerminalSubsystem;
import frc.robot.subsystems.ControllerSubsystem;


public class Robot extends TimedRobot {
    private ControlTerminalSubsystem _controlTerminal;

    public Robot() {
        _controlTerminal = ControlTerminalSubsystem.getInstance();
    }

    @Override
    public void robotInit() {
        ControllerSubsystem.getInstance();
    }

    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("Red", _controlTerminal.getDetectedColor().red);
        SmartDashboard.putNumber("Green", _controlTerminal.getDetectedColor().green);
        SmartDashboard.putNumber("Blue", _controlTerminal.getDetectedColor().blue);
        SmartDashboard.putString("Detect Color", _controlTerminal.getColorString());
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    @Override
    public void testPeriodic() {
        Scheduler.getInstance().run();
    }
}
