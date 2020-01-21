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
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import frc.robot.subsystems.ControllerSubsystem;
import frc.robot.commands.DetectColor;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

public class Robot extends TimedRobot {
    private static final String DEFAULT_AUTO = "Default";
    private static final String CUSTOM_AUTO = "My Auto";
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();
    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    private String autoSelected;
    //private final SendableChooser<String> chooser = new SendableChooser<>();
    private Command _teleopCommand;
    private String Colors;
    int y = 0;
    int b = 0;
    int r = 0;
    int g = 0;

    @Override
    public void robotInit() {
        m_colorMatcher.addColorMatch(kBlueTarget);
        m_colorMatcher.addColorMatch(kGreenTarget);
        m_colorMatcher.addColorMatch(kRedTarget);
        m_colorMatcher.addColorMatch(kYellowTarget);

        /*
        chooser.setDefaultOption("Default Auto", DEFAULT_AUTO);
        chooser.addOption("My Auto", CUSTOM_AUTO);
        SmartDashboard.putData("Auto choices", chooser);
         */
        //ControllerSubsystem.getInstance();

    }

    @Override
    public void robotPeriodic() {

        Color detectedColor = m_colorSensor.getColor();

        String colorString;
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

        if (match.color == kBlueTarget) {
            colorString = "Blue";
            if (b == 0){
                System.out.println("Blue");
                b = 1;
                y = 0;
                r = 0;
                g = 0;
            }


        } else if (match.color == kRedTarget) {
            colorString = "Red";
            if (r == 0){
                System.out.println("Red");
                b = 0;
                y = 0;
                r = 1;
                g = 0;
            }
        } else if (match.color == kGreenTarget) {
            colorString = "Green";
            if (g == 0){
                System.out.println("Green");
                b = 0;
                y = 0;
                r = 0;
                g = 1;
            }
        } else if (match.color == kYellowTarget) {
            colorString = "Yellow";
            if (y == 0){
                System.out.println("Yellow");
                b = 0;
                y = 1;
                r = 0;
                g = 0;
            }
        } else {
            colorString = "Unknown";
        }
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("Confidence", match.confidence);
        SmartDashboard.putString("Detected Color", colorString);


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