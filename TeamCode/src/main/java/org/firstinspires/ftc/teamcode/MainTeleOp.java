package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorColor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp

public class MainTeleOp extends LinearOpMode {

    NormalizedColorSensor colorSensor;

    View relativeLayout;



    /**adb connect 192.168.43.1:5555

     * The runOpMode() method is the root of this Op Mode, as it is in all LinearOpModes.
     * Our implementation here, though is a bit unusual: we've decided to put all the actual work
     * in the runSample() method rather than directly in runOpMode() itself. The reason we do that is
     * that in this sample we're changing the background color of the robot controller screen as the
     * Op Mode runs, and we want to be able to *guarantee* that we restore it to something reasonable
     * and palatable when the Op Mode ends. The simplest way to do that is to use a try...finally
     * block around the main, core logic, and an easy way to make that all clear was to separate
     * the former from the latter in separate methods.
     */
    @Override public void runOpMode() {

        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("Color", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        try {
            runSample(); // actually execute the sample
        } finally {

            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.WHITE);
                }
            });
        }
    }

    protected void runSample() {

        float gain = 10;


        final float[] hsvValues = new float[3];

        // xButtonPreviouslyPressed and xButtonCurrentlyPressed keep track of the previous and current
        // state of the X button on the gamepad
        boolean xButtonPreviouslyPressed = false;
        boolean xButtonCurrentlyPressed = false;

        // Get a reference to our sensor object. It's recommended to use NormalizedColorSensor over
        // ColorSensor, because NormalizedColorSensor consistently gives values between 0 and 1, while
        // the values you get from ColorSensor are dependent on the specific sensor you're using.
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "Color");

        // If possible, turn the light on in the beginning (it might already be on anyway,
        // we just make sure it is if we can).
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)colorSensor).enableLight(true);
        }

        // Wait for the start button to be pressed.
        waitForStart();

        // Loop until we are asked to stop
        while (opModeIsActive()) {
            // Explain basic gain information via telemetry
            telemetry.addLine("Hold the A button on gamepad 1 to increase gain, or B to decrease it.\n");
            telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value\n");

            // Update the gain value if either of the A or B gamepad buttons is being held
            if (gamepad1.a) {
                // Only increase the gain by a small amount, since this loop will occur multiple times per second.
                gain += 0.005;
            } else if (gamepad1.b && gain > 1) { // A gain of less than 1 will make the values smaller, which is not helpful.
                gain -= 0.005;
            }

            // Show the gain value via telemetry
            telemetry.addData("Gain", gain);

            // Tell the sensor our desired gain value (normally you would do this during initialization,
            // not during the loop)
            colorSensor.setGain(gain);

            // Check the status of the X button on the gamepad
            xButtonCurrentlyPressed = gamepad1.x;

            // If the button state is different than what it was, then act
            if (xButtonCurrentlyPressed != xButtonPreviouslyPressed) {
                // If the button is (now) down, then toggle the light
                if (xButtonCurrentlyPressed) {
                    if (colorSensor instanceof SwitchableLight) {
                        SwitchableLight light = (SwitchableLight)colorSensor;
                        light.enableLight(!light.isLightOn());
                    }
                }
            }
            xButtonPreviouslyPressed = xButtonCurrentlyPressed;

            // Get the normalized colors from the sensor
            NormalizedRGBA colors = colorSensor.getNormalizedColors();

            /* Use telemetry to display feedback on the driver station. We show the red, green, and blue
             * normalized values from the sensor (in the range of 0 to 1), as well as the equivalent
             * HSV (hue, saturation and value) values. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
             * for an explanation of HSV color. */

            // Update the hsvValues array by passing it to Color.colorToHSV()
            Color.colorToHSV(colors.toColor(), hsvValues);

            telemetry.addLine()
                    .addData("Red", "%.3f", colors.red)
                    .addData("Green", "%.3f", colors.green)
                    .addData("Blue", "%.3f", colors.blue);
            telemetry.addLine()
                    .addData("Hue", "%.3f", hsvValues[0])
                    .addData("Saturation", "%.3f", hsvValues[1])
                    .addData("Value", "%.3f", hsvValues[2]);
            telemetry.addData("Alpha", "%.3f", colors.alpha);

            /* If this color sensor also has a distance sensor, display the measured distance.
             * Note that the reported distance is only useful at very close range, and is impacted by
             * ambient light and surface reflectivity. */
            if (colorSensor instanceof DistanceSensor) {
                telemetry.addData("Distance (cm)", "%.3f", ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM));
            }

            telemetry.update();

            // Change the Robot Controller's background color to match the color detected by the color sensor.
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(hsvValues));
                }
            });
        }
    }
}

