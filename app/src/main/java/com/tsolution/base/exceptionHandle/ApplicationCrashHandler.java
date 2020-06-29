package com.tsolution.base.exceptionHandle;

import androidx.lifecycle.MutableLiveData;

import android.util.Log;

import com.tsolution.base.RetrofitClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ApplicationCrashHandler implements Thread.UncaughtExceptionHandler {
    /**
     * Storage for the original default crash handler.
     */
    private Thread.UncaughtExceptionHandler defaultHandler;
    private String userName;
    protected MutableLiveData<Throwable> appException;


    /**
     * Simple string for category log
     */
    private static final String TAG = "ApplicationCrashHandler";

    /**
     * Installs a new exception handler.
     */
    public static void installHandler(MutableLiveData<Throwable> appEx) {


        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof ApplicationCrashHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ApplicationCrashHandler(appEx));
        }
    }

    private ApplicationCrashHandler(MutableLiveData<Throwable> appEx) {
        this.appException = appEx;
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

    }

    /**
     * Called when there is an uncaught exception elsewhere in the code.
     * @param t the thread that caused the error
     * @param e the exception that caused the error
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // Place a breakpoint here to catch application crashes
        Log.wtf(TAG, String.format("Exception: %s\n%s", e.toString(), getStackTrace(e)));
        AndroidCrashLogDto dto = new AndroidCrashLogDto(RetrofitClient.USER_NAME, getStackTrace(e));
        RetrofitClient.getClient().create(SOService.class).appCrash(dto);
        appException.postValue(e);
        //messageBox("doStuff", e.getMessage());
        // Call the default handler
        defaultHandler.uncaughtException(t, e);
    }

    /**
     * Convert an exception into a printable stack trace.
     * @param e the exception to convert
     * @return the stack trace
     */
    private String getStackTrace(Throwable e) {
        final Writer sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);

        e.printStackTrace(pw);
        String stacktrace = sw.toString();
        pw.close();

        return stacktrace;
    }
}
