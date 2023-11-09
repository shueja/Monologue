# Monologue: Annotation-based Telemetry and Data Logging for Java Teams
*monologue. n. A one-sided conversation.*

Monologue is a Java annotation-based logging library for FRC. With Monologue, extensive telemetry and on-robot logging can be added to your robot code with minimal code footprint and design restrictions. 

*"Isn't this what Oblog does?"*

Monologue is intended as a successor to the popular logging library  Oblog. Though Oblog still works for many teams, its original purpose as a better way to define Shuffleboard code-driven layouts has become less useful, since Shuffleboard has become unstable and relatively unmaintained. Additionally, Oblog's dependence on the Shuffleboard API means it is not easily adaptable to logging to DataLogManager/WPILog, or using the new-for-2024 struct and protobuf formats for structured data types like the WPILib geometry types. 

Enter Monologue, which combines the ease of use of annotations with the new features below:

- Automatic DataLog logging and NT logging in one annotation.
- Individually-updateable NT and DataLog providers, meaning NT logging can be disabled on-field to save bandwidth/loop time, while still logging everything to the WPILog.
- Additional Imperative API to log temporary/non-class-scope values that can't be annotated.
- Struct-based support for logging WPILib geometry types (other similar data types to come later)
- Use of NT4 APIs directly, instead of using the Shuffleboard APIs.

Check out the [wiki](https://github.com/shueja/Monologue/wiki) for more details and installation instructions!
