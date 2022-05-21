package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
@Autonomous
public class SensorTest extends LinearOpMode {

    private DistanceSensor Range;

    @Override
    public void runOpMode() throws InterruptedException {


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Range = hardwareMap.get(DistanceSensor.class, "Range");

        // you can also cast this to a Rev2mDistanceSensor if you want to use added
        // methods associated with the Rev2mDistanceSensor class.
        Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor) Range;


        Trajectory Drive = drive.trajectoryBuilder(new Pose2d(0, 0, Math.toRadians(0)), false)
                .forward(10)
                .build();

        waitForStart();
        while (opModeIsActive()) {

            if (isStopRequested()) {
                return;
            }
            drive.StrafeWithWhile(.2, 20);
            drive.followTrajectory(Drive);
            break;

        }


    }

   /* public void StrafeTillSense ( double power, double Length)
    {
        if (isStopRequested()) { return; }

        while (Range.getDistance(DistanceUnit.INCH) > Length && opModeIsActive()) {

            drive.PowerOn(.5);

        }
            drive.Off();
    }*/
}
