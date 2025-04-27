package org.antlr.jetbrains.sample.run;

import com.intellij.execution.process.ProcessHandler;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;

public class GsqlAutoTerminatingProcessHandler extends ProcessHandler {
    @Override
    protected void destroyProcessImpl() {
        notifyProcessTerminated(0);
    }

    @Override
    protected void detachProcessImpl() {
        notifyProcessTerminated(0);
    }

    @Override
    public boolean detachIsDefault() {
        return false;
    }

    @Nullable
    @Override
    public OutputStream getProcessInput() {
        return null;
    }

    @Override
    public boolean isProcessTerminated() {
        return true; // Always report as terminated
    }

    @Override
    public boolean isProcessTerminating() {
        return false;
    }

    @Override
    public void startNotify() {
        super.startNotify();
        // Terminate immediately after starting
        notifyProcessTerminated(0);
    }
}
