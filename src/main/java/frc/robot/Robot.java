/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;
import frc.robot.config.RobotMap;

public class Robot extends TimedRobot {
    private static final String DEFAULT_AUTO = "Default";
    private static final String CUSTOM_AUTO = "My Auto";



    private String autoSelected;
    //private final SendableChooser<String> chooser = new SendableChooser<>();
    private Command _teleopCommand;
    private String Colors;


    @Override
    public void robotInit() {


        /*
        chooser.setDefaultOption("Default Auto", DEFAULT_AUTO);
        chooser.addOption("My Auto", CUSTOM_AUTO);
        SmartDashboard.putData("Auto choices", chooser);
         */
        //ControllerSubsystem.getInstance();

    }

    @Override
    public void robotPeriodic() {



    }

    @Override
    public void autonomousInit() {
        //autoSelected = chooser.getSelected();
        // autoSelected = SmartDashboard.getString("Auto Selector",
        // defaultAuto);
        //System.out.println("Auto selected: " + autoSelected);
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();

        /*
        switch (autoSelected)
        {
            case CUSTOM_AUTO:
                // Put custom auto code here
                break;
            case DEFAULT_AUTO:
            default:
                // Put default auto code here
                break;
        }
         */
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