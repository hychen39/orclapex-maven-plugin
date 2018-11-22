Oracle APEX Maven plugin 1.0.4
========================

## About

This version supports NatrualDocs 2.X.

The version 1.0.3 and before only supports NaturalDocs 1.x.

## Enhancement

We can use the `Project.txt` as the primary configuration source. 
`Project.txt` is introduced after NaturalDocs 2.0.

The command line for using the `Project.txt:

```
NaturalDocs.exe [project_configuration_folder] [additional_options]
```

NatrualDocs will generate the `Project.txt` in the `project_configuration_foleder`. See [Project.txt page](https://www.naturaldocs.org/reference/project.txt/)
to learn more.

### Added parameters

Serveral parameters are added for using the `run-natural-docs` goal in the `orclapex` plugin. They are:

- `naturalDocExe`: the NaturalDocs runnable file. The full path is obtained by concatenating `naturalDocsHome` 
  and `naturalDocExe`.
- `configFolder`: the configuration folder for using the NaturalDocs, for example: `natural_doc_config`. 
    If `projectDirectory` is specified, the full path for the configuration folder is 
    `projectDirectory` + `File.seperator()` + `natural_doc_config`  
- `workingDirectory`: the working directory. Use the parameter to specify a new working directory.

### Java Version required to compile the source codes.

Java 7 or after.