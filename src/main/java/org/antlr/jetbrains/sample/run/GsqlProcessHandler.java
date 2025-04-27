package org.antlr.jetbrains.sample.run;

import com.intellij.execution.process.ProcessHandler;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;

public class GsqlProcessHandler extends ProcessHandler {
    private boolean myTerminated = false;

    @Override
    protected void destroyProcessImpl() {
        myTerminated = true;
    }

    @Override
    protected void detachProcessImpl() {
        myTerminated = true;
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
        return myTerminated;
    }

    @Override
    public boolean isProcessTerminating() {
        return false;
    }

    @Override
    public void startNotify() {
        super.startNotify();
        myTerminated = true;
        notifyProcessTerminated(0);
    }
}
