package org.jrichard.util;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Set a property to the user-uid and user-gid that is running
 * the maven-call.
 */
@Mojo(name = "user-id", defaultPhase = LifecyclePhase.INITIALIZE)
public class UidMojo
        extends AbstractMojo {

    private static final int FAILED_CODE = 1;
    private static final int SUCCESS_CODE = 0;

    private final static String ID_CMD = "id";
    private final static String USER_PARAM = "-u";
    private final static String GROUP_PARAM = "-g";


    @Parameter(property = "uidPropertyName", defaultValue = "user.uid")
    private String _uidPropertyName;

    @Parameter(property = "gidPropertyName", defaultValue = "user.gid")
    private String _gidPropertyName;

    public void execute() throws MojoExecutionException {

        try {
            final String uid = getId(USER_PARAM);
            final String gid = getId(GROUP_PARAM);
            final Properties props = new Properties();
            props.setProperty(_uidPropertyName, uid);
            props.setProperty(_gidPropertyName, gid);
            System.setProperties(props);
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private String getId(String param) throws IOException, MojoExecutionException {
        final CommandLine cmdLine = CommandLine.parse(ID_CMD);
        cmdLine.addArgument(param);
        final CommandLineOutput commandLineOutput = executeCommandLine(cmdLine);
        if (commandLineOutput.errCode == SUCCESS_CODE) {
            return commandLineOutput.output;
        }
        throw new MojoExecutionException("Unable to invoke \"id -u\" command");
    }

    private CommandLineOutput executeCommandLine(CommandLine cmdLine) throws IOException {
        final DefaultExecutor executor = new DefaultExecutor();
        final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        final StringBuilder builder = new StringBuilder();
        for (String argument : cmdLine.getArguments()) {
            builder.append(" " + argument);
        }
        getLog().debug("######## executeCommandLine: " + cmdLine.getExecutable() + builder.toString());

        try (final ByteArrayOutputStream stdout = new ByteArrayOutputStream()) {

            final PumpStreamHandler psh = new PumpStreamHandler(stdout);
            executor.setStreamHandler(psh);
            executor.execute(cmdLine, resultHandler);
            resultHandler.waitFor();
            final int errCode = resultHandler.getExitValue();
            getLog().debug("Echo command executed, any errors:  " + (errCode == SUCCESS_CODE ? "No" : "Yes"));

            getLog().debug("Output : " + stdout.toString());

            return new CommandLineOutput(errCode, stdout.toString());

        } catch (InterruptedException e) {
            getLog().error("", e);
            return new CommandLineOutput(FAILED_CODE, e.getMessage());
        }
    }

    public static class CommandLineOutput {

        private final int errCode;
        private final String output;


        public CommandLineOutput(int errCode, String output) {
            this.errCode = errCode;
            this.output = output;
        }

        public int getErrCode() {
            return errCode;
        }

        public String getOutput() {
            return output;
        }
    }

    public void setGidPropertyName(String gidPropertyName) {
        this._gidPropertyName = gidPropertyName;
    }

    public void setUidPropertyName(String uidPropertyName) {
        this._uidPropertyName = uidPropertyName;
    }
}
