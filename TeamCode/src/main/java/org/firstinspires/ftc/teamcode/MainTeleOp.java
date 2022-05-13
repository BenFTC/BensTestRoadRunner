package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorColor;

@TeleOp

public class MainTeleOp extends LinearOpMode {

    public SensorColor Colour;
    final double SCALE_FACTOR = 110;

    @Override
    public void runOpMode() throws InterruptedException {
        Colour = (SensorColor) hardwareMap.colorSensor.get("Color");

        waitForStart();

        while (opModeIsActive()) {

            Color.RGBToHSV((int) (Colour.red * SCALE_FACTOR));
            (int) (Colour.green() * SCALE_FACTOR),

        }
    }
}
