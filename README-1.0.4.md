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

Two parameters are added for using the `run-natural-docs` goal in the `orclapex` plugin. They are:

- `naturalDocExe`: the full name for the NaturalDocs runnable file, for example: `C:\NaturalDocs\NaturalDocs.exe`.
- `configFolder`: the configuration folder for using the NaturalDocs, for example: `D:\apex_prj\natural_doc_config` 


### Java Version required to compile the source codes.

Java 7 or after.