/*
 * The MIT License
 *
 * Copyright 2014 Tadeo Valencia.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.tadeoval.faceghost.executor;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.tadeoval.faceghost.gui.logger.GuiLogger;

/**
 *
 * @author Tadeo Valencia
 */
public class ScriptExecutor {
    
    private final String username;
    private final String password;
    private final String albumSource;
    private final String albumDestination;
    private final GuiLogger guiLog;
    
    /**
     * 
     * @param username
     * @param password
     * @param albumSource
     * @param albumDestination
     * @param log 
     */
    public ScriptExecutor(String username, String password, String albumSource, String albumDestination, GuiLogger log) {
        this.username = username;
        this.password = password;
        this.albumSource = albumSource;
        this.albumDestination = albumDestination;
        this.guiLog = log;
    }
    
    /**
     * Runs the casper script with the given parameters provided in the constructor
     */
    public void runCommand() {
        CommandLine cmdLine = new CommandLine("casperjs");
        cmdLine.addArgument("./src/org/tadeoval/faceghost/scripts/faceghost.js");
        cmdLine.addArgument(this.albumSource);
        cmdLine.addArgument("--username=" + this.username);
        cmdLine.addArgument("--password=" + this.password);
        cmdLine.addArgument("--folder=${folder}");
        HashMap map = new HashMap();
        map.put("folder", new File(this.albumDestination));
        cmdLine.setSubstitutionMap(map);
        
        
        PumpStreamHandler psh = new PumpStreamHandler(this.guiLog, this.guiLog);

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        // 5 minutes timeout
        ExecuteWatchdog watchdog = new ExecuteWatchdog(5*60*1000);
        
        Executor executor = new DefaultExecutor();
        executor.setExitValue(0);
        executor.setWatchdog(watchdog);
        executor.setStreamHandler(psh);
         
        // Thread to avoid freezing the UI
        new Thread(() -> {
            try {
                executor.execute(cmdLine, resultHandler);
                
                // some time later the result handler callback was invoked so we
                // can safely request the exit value
                resultHandler.waitFor();
                if(resultHandler.hasResult()) {
                    int exitValue = resultHandler.getExitValue();
                    System.out.println(exitValue);
                }
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(ScriptExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    
}
