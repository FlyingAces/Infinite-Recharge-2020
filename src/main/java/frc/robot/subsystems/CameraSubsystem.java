package frc.robot.subsystems;


import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.config.RobotMap;

import org.opencv.core.Mat;


public class CameraSubsystem implements Subsystem {
    private CvSink _cvSink;
    private static CameraSubsystem _instance;

    public CameraSubsystem() {
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture("cam0", RobotMap.CAMERA_FRONT);

        camera.setResolution(RobotMap.CAMERA_IMG_WIDTH, RobotMap.CAMERA_IMG_HEIGHT);
        camera.setBrightness(25);

        _cvSink = CameraServer.getInstance().getVideo(camera);
    }

    public static CameraSubsystem getInstance() {
        if (_instance == null)
            _instance = new CameraSubsystem();
        return _instance;
    }

    public Mat getCameraPicture() {
        Mat image = new Mat();
        _cvSink.grabFrame(image);
        return image;
    }

    @Override
    public void setDefaultCommand(Command defaultCommand) {

    }
}
