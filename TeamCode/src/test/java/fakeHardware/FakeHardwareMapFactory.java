package fakeHardware;

import androidx.annotation.NonNull;
import fakeHardware.drive.FakeCRServo;
import fakeHardware.drive.FakeDcMotor;
import fakeHardware.drive.FakeRevBlinkinLedDriver;
import fakeHardware.drive.FakeServo;
import fakeHardware.sensors.FakeDigitalChannel;
import fakeHardware.sensors.FakeDistanceSensor;
import fakeHardware.sensors.FakeRevTouchSensor;
import fakeHardware.sensors.FakeVoltageSensor;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FakeHardwareMapFactory {

    public static final String MOTOR_TAG_NAME = "Motor";
    public static final String SERVO_TAG_NAME = "Servo";
    public static final String LYNX_USB_DEVICE_TAG_NAME = "LynxUsbDevice";
    public static final String ROBOT_TAG_NAME = "Robot";
    public static final String LYNX_MODULE_TAG_NAME = "LynxModule";
    public static final String REV_DISTANCE_SENSOR_TAG_NAME = "REV_VL53L0X_RANGE_SENSOR";
    public static final String DIGITAL_DEVICE_TAG_NAME = "DigitalDevice";
    public static final String REV_TOUCH_SENSOR_TAG_NAME = "RevTouchSensor";
    public static final String REV_BLINKINLED_DRIVER_TAG_NAME = "RevBlinkinLedDriver";
    public static final String CR_SERVO_TAG_NAME = "ContinuousRotationServo";

    /**
     * Loads the hardware map with the name &quot;hardwareMapName&quot; from the location used by
     * the FTC SDK when storing hardware map XML files alongside your robot sourcecode and provides
     * fake implementations for the devices found.
     *
     * @param hardwareMapName the name of the hardware map XML file
     * @return a HardwareMap that provides fake implementations for devices found in the file
     */

    public static HardwareMap getFakeHardwareMap(@NonNull final String hardwareMapName) throws IOException, ParserConfigurationException, SAXException {
        String path = String.format("src/main/res/xml/%s", hardwareMapName);

        return getFakeHardwareMap(new File(path));
    }

    /**
     * Loads the hardware map from the given path
     */
    public static HardwareMap getFakeHardwareMap(@NonNull final File hardwareMapFile) throws IOException, ParserConfigurationException, SAXException {
        HardwareMapCreator hwMapCreator = new HardwareMapCreator();

        if (!hardwareMapFile.exists()) {
            throw new IOException("Hardware map file '" + hardwareMapFile.getAbsolutePath() + "' does not exist");
        }

        if (!hardwareMapFile.canRead()) {
            throw new IOException("Hardware map file '" + hardwareMapFile.getAbsolutePath() + "' is not readable");
        }

        hwMapCreator.parseUsingDocBuilder(new FileInputStream(hardwareMapFile));

        return hwMapCreator.hardwareMap;
    }

    private static class HardwareMapCreator {
        private Set<String> deviceNames = new HashSet<>();

        private HardwareMap hardwareMap = new HardwareMap(null,null);

        private void parseUsingDocBuilder(InputStream fileInput) throws ParserConfigurationException, IOException, SAXException {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(fileInput);

            // FIXME: Create LynxUsbModules by "lookback" from device nodes so that we can
            //        build fake motor controllers and the like eventually
            addAllMotors(doc);
            addAllServos(doc);

            addAllDigitalChannels(doc);
            addAllRevTouchSensors(doc);
            addAllRevBlinkinledDrivers(doc);

            addAllDistanceSensors(doc);

            // FIXME: Add implementations for things we don't support, but need:
            // IMU, LynxColorSensor, RevColorSensorV3, AnalogInput

            hardwareMap.voltageSensor.put("Voltage Sensor", new FakeVoltageSensor());
        }

        private void addAllDistanceSensors(Document doc) {
            NodeList distanceSensors = doc.getElementsByTagName(REV_DISTANCE_SENSOR_TAG_NAME);

            addDevices(distanceSensors, new DeviceFromXml() {
                @Override
                public void addDeviceToHardwareMap(String name, int portNumber) {
                    final FakeDistanceSensor fakeDistanceSensor = new FakeDistanceSensor();

                    hardwareMap.put(name, fakeDistanceSensor);

                    // No DeviceMapping for the Rev2mDistanceSensor
                }
            });
        }

        private void addAllDigitalChannels(Document doc) {
            NodeList digitalChannels = doc.getElementsByTagName(DIGITAL_DEVICE_TAG_NAME);

            addDevices(digitalChannels, new DeviceFromXml() {
                @Override
                public void addDeviceToHardwareMap(String name, int portNumber) {
                    final FakeDigitalChannel fakeDigitalChannel = new FakeDigitalChannel();

                    hardwareMap.put(name, fakeDigitalChannel);

                    hardwareMap.digitalChannel.put(name, fakeDigitalChannel);
                }
            });
        }

        private void addAllRevTouchSensors(Document doc) {
            NodeList revTouchSensors = doc.getElementsByTagName(REV_TOUCH_SENSOR_TAG_NAME);

            addDevices(revTouchSensors, new DeviceFromXml() {
                @Override
                public void addDeviceToHardwareMap(String name, int portNumber) {
                    final FakeRevTouchSensor fakeRevTouchSensor = new FakeRevTouchSensor(portNumber);

                    hardwareMap.put(name, fakeRevTouchSensor);

                    hardwareMap.touchSensor.put(name, fakeRevTouchSensor);
                }
            });
        }

        private void addAllRevBlinkinledDrivers(Document doc) {
            NodeList blinkinLeds = doc.getElementsByTagName(REV_BLINKINLED_DRIVER_TAG_NAME);

            addDevices(blinkinLeds, new DeviceFromXml() {
                @Override
                public void addDeviceToHardwareMap(String name, int portNumber) {
                    hardwareMap.put(name, new FakeRevBlinkinLedDriver(portNumber));
                }
            });
        }

        private void addAllMotors(Document doc) {
            // FIXME: There are other motor tag names:
            // Bonus points - how do you add these without a lot of copy-and-paste code?
            //                NeveRest3.7v1Gearmotor
            //                NeveRest20Gearmotor
            //                NeveRest40Gearmotor
            //                NeveRest60Gearmotor
            //                Matrix12vMotor
            //                TetrixMotor
            //                goBILDA5201SeriesMotor name="[...]" port="[...]" />
            //                goBILDA5202SeriesMotor
            //                RevRobotics20HDHexMotor name="[...]" port="[...]" />
            //                RevRobotics40HDHexMotor name="[...]" port="[...]" />
            //                RevRoboticsCoreHexMotor name="[...]" port="[...]" />

            NodeList dcMotors = doc.getElementsByTagName(MOTOR_TAG_NAME);

            addDevices(dcMotors, new DeviceFromXml() {
                @Override
                public void addDeviceToHardwareMap(String name, int portNumber) {
                    final FakeDcMotor fakeDcMotor = new FakeDcMotor();

                    hardwareMap.put(name, fakeDcMotor);

                    hardwareMap.dcMotor.put(name, fakeDcMotor);
                }
            });
        }

        private void addAllServos(Document doc) {
            // FIXME: We don't yet support ContinuousRotationServo or RevSPARKMini
            NodeList servos = doc.getElementsByTagName(SERVO_TAG_NAME);

            addDevices(servos, new DeviceFromXml() {
                @Override
                public void addDeviceToHardwareMap(String name, int portNumber) {
                    final FakeServo fakeServo = new FakeServo();

                    hardwareMap.put(name, fakeServo);

                    hardwareMap.servo.put(name, fakeServo);
                }
            });

            NodeList crServos = doc.getElementsByTagName(CR_SERVO_TAG_NAME);

            addDevices(crServos, new DeviceFromXml() {
                @Override
                public void addDeviceToHardwareMap(String name, int portNumber) {
                    final FakeCRServo fakeCrServo = new FakeCRServo();

                    hardwareMap.put(name, fakeCrServo);

                    hardwareMap.crservo.put(name, fakeCrServo);
                }
            });
        }

        private void addDevices(NodeList deviceNodeList, DeviceFromXml deviceAdder) {
            for (int i = 0; i < deviceNodeList.getLength(); i++) {
                Node deviceNode = deviceNodeList.item(i);
                NamedNodeMap attributesByName = deviceNode.getAttributes();

                Node nameNode = attributesByName.getNamedItem("name");
                String nameValue = nameNode.getNodeValue();

                if (nameValue != null) {
                    nameValue = nameValue.trim();
                }

                if (deviceNames.contains(nameValue)) {
                    // This isn't exactly real hardware map behavior, but it prevents
                    // problems at runtime if you are using
                    throw new IllegalArgumentException(String.format(
                            "Non unique device name '%s' for device type '%s'",
                            nameValue, deviceNode.getNodeName()));
                }

                deviceNames.add(nameValue);

                Node portNode = attributesByName.getNamedItem("port");
                int portValue = Integer.valueOf(portNode.getNodeValue());

                deviceAdder.addDeviceToHardwareMap(nameValue, portValue);
            }
        }
    }

    interface DeviceFromXml {
        void addDeviceToHardwareMap(String name, int portNumber);
    }
}