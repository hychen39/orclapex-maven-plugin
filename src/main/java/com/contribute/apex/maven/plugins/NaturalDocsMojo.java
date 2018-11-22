package com.contribute.apex.maven.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Run Natural Docs to generate your project's technical documentation.
 * http://www.naturaldocs.org/
 */
@Mojo(name = "run-natural-docs",
        defaultPhase = LifecyclePhase.COMPILE)
public class NaturalDocsMojo extends AbstractMojo {

    /**
     * The path to the folder containing the Natural Docs executable.
     */
    @Parameter(property = "run-natural-docs.naturalDocsHome",
            required = true)
    private File naturalDocsHome;
    /**
     * Natural Docs will build the documentation from the files in these
     * directories and all its subdirectories. It is possible to specify
     * multiple directories.
     */
    @Parameter(property = "run-natural-docs.inputSourceDirectories" /*,
            required = true*/)
    private List<File> inputSourceDirectories;
    /**
     * The output format. The supported formats are HTML and FramedHTML.
     */
    @Parameter(property = "run-natural-docs.outputFormat"
            /*,required = true*/)
    private String outputFormat;
    /**
     * The folder in which Natural Docs will generate the technical
     * documentation.
     */
    @Parameter(property = "run-natural-docs.outputDirectory"
            /*, required = true*/)
    private File outputDirectory;
    /**
     * Natural Docs needs a place to store the project's specific configuration
     * and data files.
     */
    @Parameter(property = "run-natural-docs.projectDirectory"
            /*, required = true*/)
    private File projectDirectory;
    /**
     * Working directory to execute.
     *
     */
    @Parameter(property = "run-natural-docs.workingDirectory"
            /*, required = true*/)
    private File workingDirectory;
    /**
     * Excludes a subdirectory from being scanned. You can specify it multiple
     * times to exclude multiple subdirectories.
     */
    @Parameter(property = "run-natural-docs.excludedSubdirectories")
    private List<File> excludedSubdirectories;
    /**
     * Rebuilds everything from scratch. All source files will be rescanned and
     * all output files will be rebuilt.
     */
    @Parameter(property = "run-natural-docs.rebuild")
    private boolean rebuild;
    /**
     * Rebuilds all output files from scratch.
     */
    @Parameter(property = "run-natural-docs.rebuildOutput")
    private boolean rebuildOutput;
    /**
     * Tells Natural Docs to only include what you explicitly document in the
     * output, and not to find undocumented classes, functions, and variables.
     */
    @Parameter(property = "run-natural-docs.documentedOnly")
    private boolean documentedOnly;
    /**
     * Tells Natural Docs to only use the file name for its menu and page
     * titles.
     */
    @Parameter(property = "run-natural-docs.onlyFileTitles")
    private boolean onlyFileTitles;
    /**
     * Tells Natural Docs to not automatically create group topics if you don't
     * add them yourself.
     */
    @Parameter(property = "run-natural-docs.noAutoGroup")
    private boolean noAutoGroup;
    /**
     * Suppresses all non-error output.
     */
    @Parameter(property = "run-natural-docs.quiet")
    private boolean quiet;

    /**
     * The executable file of the NaturalDoc, including the full path.
     * Example: "C:\\NaturalDocs\\NaturalDocs.exe";
     */
    @Parameter(property = "run-natural-docs.naturalDocExe", required = true)
    private String naturalDocExe;

    /**
     * Configure folder for the NaturalDoc 2.0
     */
    @Parameter(property = "run-natural-docs.configFolder")
    private String configFolder;

    // unsupported optional parameters:
    //   --images / --style / --tab-length / --highlight

    /**
     * The method called by Maven when the 'run-natural-docs' goal gets
     * executed.
     */

    public void execute() throws MojoExecutionException, MojoFailureException {
        // New parameters to running NaturalDOc 2.0
        // Run the natural doc using java
//        naturalDocExe = "C:\\NaturalDocs\\NaturalDocs.exe";
//        File naturalDocHome = new File("C:\\NaturalDocs");
//        String configFolder = "D:\\apex_prj\\im_space_mgt_sys_maven\\natural_doc_config";
        //
        ProcessBuilder processBuilder;
        List<String> commandLineArguments = new ArrayList<String>();



    // debug show parameters
        String fullNaturalDocExec = naturalDocsHome + File.separator + naturalDocExe;
        String fullConfigFolder = projectDirectory != null? projectDirectory + File.separator + configFolder :
                                    configFolder;
        getLog().debug("Exec:" + fullNaturalDocExec);
        getLog().debug("configFolder:" + fullConfigFolder);

        // validate parameters
        validateParameters();

        // make the command line arguments
        commandLineArguments.add(fullNaturalDocExec);

        // required parameters for using Natural Doc 1.x version.
        if (configFolder == null) {
            for (int i = 0; i < inputSourceDirectories.size(); i++) {
                commandLineArguments.add("-i");
                commandLineArguments.add(inputSourceDirectories.get(i).getPath());
            }
            commandLineArguments.add("-o");
            commandLineArguments.add(outputFormat);
            commandLineArguments.add(outputDirectory.getPath());
            commandLineArguments.add("-p");
            commandLineArguments.add(projectDirectory.getPath());
        } else {
            // use the config folder for using Natural Doc 2.x version.
            commandLineArguments.add(fullConfigFolder);
        }

        // optional parameters
        if (excludedSubdirectories != null) {
            for (int i = 0; i < excludedSubdirectories.size(); i++) {
                commandLineArguments.add("-xi");
                commandLineArguments.add(excludedSubdirectories.get(i).getPath());
            }
        }
        if (rebuild) {
            commandLineArguments.add("-r");
        }
        if (rebuildOutput) {
            commandLineArguments.add("-ro");
        }
        if (documentedOnly) {
            commandLineArguments.add("-do");
        }
        if (onlyFileTitles) {
            commandLineArguments.add("-oft");
        }
        if (noAutoGroup) {
            commandLineArguments.add("-nag");
        }
        if (quiet) {
            commandLineArguments.add("-q");
        }

        String commandToExecute = commandLineArguments.toString();
        getLog().debug("Executing Natural Docs: " + commandToExecute);

        // Ready to execute
        processBuilder = new ProcessBuilder(commandLineArguments);
        getLog().debug("Current working directory: " + System.getProperty("user.dir"));

        if (workingDirectory != null) {
            getLog().debug("New working directory specified by the <workingDirectory> tag: " + workingDirectory.toString());
            processBuilder.directory(workingDirectory);
        }
        processBuilder.redirectErrorStream(true);

        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print out the message for executing results
        try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()))
        ) {
            String output = null;
            while ((output = stdInput.readLine()) != null) {
                getLog().info(output);
            }
        } catch (IOException ex) {
            throw new MojoExecutionException("An unexpected error occurred while executing Natural Docs", ex);
        }
    }

    /**
     * Validate the entered configuration parameters.
     */
    private void validateParameters() throws MojoExecutionException {
        if (!naturalDocsHome.isDirectory()) {
            throw new MojoExecutionException("The specified naturalDocsHome is not a folder: " + naturalDocsHome.getAbsolutePath());
        }
        if (configFolder == null) {
            // check the input source directory
            for (int i = 0; i < inputSourceDirectories.size(); i++) {
                if (!inputSourceDirectories.get(i).isDirectory()) {
                    throw new MojoExecutionException("The specified inputSourceDirectory is not a folder: " + inputSourceDirectories.get(i).getAbsolutePath());
                }
            }

            if (!(outputFormat.toLowerCase().equals("html") || outputFormat.toLowerCase().equals("framedhtml"))) {
                throw new MojoExecutionException("Unknown output format: " + outputFormat + ". Valid formats are HTML and FramedHTML.");
            }

            if (!outputDirectory.isDirectory()) {
                throw new MojoExecutionException("The specified outputDirectory is not a folder: " + outputDirectory.getAbsolutePath());
            }
            if (!projectDirectory.isDirectory()) {
                throw new MojoExecutionException("The specified projectDirectory is not a folder: " + projectDirectory.getAbsolutePath());
            }
        }


        for (int i = 0; i < excludedSubdirectories.size(); i++) {
            if (!excludedSubdirectories.get(i).isDirectory()) {
                throw new MojoExecutionException("The specified excludedSubdirectory is not a folder: " + excludedSubdirectories.get(i).getAbsolutePath());
            }
        }
    }
}