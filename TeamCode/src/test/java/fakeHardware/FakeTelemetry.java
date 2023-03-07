package fakeHardware;

import com.qualcomm.robotcore.robocol.TelemetryMessage;

import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

import java.util.Map;
import java.util.Set;

public class FakeTelemetry extends TelemetryImpl {
    private boolean outputToStdout = false;

    private String currentTelemetryData;

    public FakeTelemetry() {
        super(null);
    }

    public void setOutputToStdout(boolean outputToStdout) {
        this.outputToStdout = outputToStdout;
    }

    public String getCurrentTelemetryData() {
        return currentTelemetryData;
    }

    void markDirty()
    {
        this.isDirty = true;
    }

    void markClean()
    {
        this.isDirty = false;
    }

    boolean isDirty()
    {
        return this.isDirty;
    }

    // This is copied out of the SDK, to mimic the ordering/presentation
    // that is in real telemetry. This allows us to write tests that has data
    // show up in the same way it does on the DS phone.
    @Override
    protected boolean tryUpdate(UpdateReason updateReason)
    {
        synchronized (theLock)
        {
            boolean result = false;

            boolean intervalElapsed = this.transmissionTimer.milliseconds() > msTransmissionInterval;

            boolean wantToTransmit  = updateReason==UpdateReason.USER
                    || updateReason==UpdateReason.LOG
                    || (updateReason==UpdateReason.IFDIRTY && (isDirty() /* || log.isDirty() */));

            boolean recompose = updateReason==UpdateReason.USER
                    || isDirty();         // only way we get dirty is from a previous UpdateReason.USER

            if (wantToTransmit)
            {
                // Evaluate any delayed actions we've been asked to do
                for (Runnable action : this.actions)
                {
                    action.run();
                }

                // Build an object to cary our telemetry data
                TelemetryMessage transmitter = new TelemetryMessage();
                this.saveToTransmitter(recompose, transmitter);

                // Transmit if there's anything to transmit
                if (transmitter.hasData()) {
                    StringBuilder output = new StringBuilder();

                    Map<String, String> toPrint = transmitter.getDataStrings();

                    Set<Map.Entry<String, String>> lines = toPrint.entrySet();

                    for (Map.Entry<String, String> entry : lines) {
                        output.append(entry.getValue());
                        output.append("\n");
                    }

                    currentTelemetryData = output.toString();

                    if (outputToStdout) {
                        System.out.println("\n[Telemetry Start] ------------------------\n");
                        System.out.println(currentTelemetryData);
                        System.out.println("\n[Telemetry End] ------------------------\n");
                    }
                }

                // We've definitely got nothing lingering to transmit
                // this.log.markClean();
                markClean();

                // Update for the next time around
                this.transmissionTimer.reset();
                result = true;
            }
            else if (updateReason==UpdateReason.USER)
            {
                // Next time we get an IFDIRTY update, we'll try again. Note that the log has its
                // own independent dirty status; thus if *it* is dirty, then an IFDIRTY update will
                // automatically try again.
                this.markDirty();
            }

            // In all cases, if it's a user requesting the update, then the next add clears
            if (updateReason==UpdateReason.USER)
            {
                // Postponing the clear vs doing it right now allows future log updates to
                // transmit before the user does more addData()
                this.clearOnAdd = isAutoClear();
            }

            return result;
        }
    }
}