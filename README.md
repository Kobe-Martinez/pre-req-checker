# PreReqChecker

The project, `PreReqChecker`, focuses on analyzing and managing course prerequisite structures from Rutgers University. It’s a robust tool designed for validating prerequisites, verifying course eligibility, and generating academic plans for students majoring in Computer Science. The tool processes input using structured files (like adjacency lists) and outputs results for various tasks related to course planning.


## Table of Contents

- [Features](#features)
- [Usage](#usage)
- [Code Structure](#code-structure)
- [Requirements](#requirements)
- [File Outputs](#file-outputs)
- [License](#license)
- [Important Note](#important-note)


## Features

- **Course Prerequisite Validation**
  
  - Ensures the prerequisites for a course are valid and structured correctly

- **Eligibility Checking**
  
  - Verifies if a student is eligible to take a specific course based on completed prerequisites

- **Schedule Planning**
  
  - Generates a schedule of courses required to fulfill academic goals

- **Adjacency List Support**
  
  - Processes course prerequisite structures using adjacency lists for flexibility and efficiency
    

## Usage

1. **Prepare the Data**
   
   - Input files like `adjlist.in` and `validprereq.in` should follow the required format

3. **Run the Program**
   
   - Compile and execute the Java program:
     ```bash
     javac -d bin src/*.java
     java -cp bin PreReqChecker <input_file>
     ```

5. **View Output**
   
   - Results will be saved to corresponding output files (e.g., `adjlist.out`, `eligible.out`)


## Code Structure

- **PreReqChecker.java**
  
  - Main program handling course validation and eligibility checking

- **Helper Classes**
  
  - Contains utility methods for processing adjacency lists and prerequisite chains

- **Input and Output Files**
  
  - Structured files for defining prerequisites and storing results


## Requirements

- **Java Development Kit (JDK)**: Version 8 or higher


## File Outputs

- `adjlist.out`: Processed adjacency list of course prerequisites
  
- `eligible.out`: List of courses a student is eligible to take
  
- `needtotake.out`: Courses required to complete a specific academic goal
  
- `scheduleplan.out`: Optimized course schedule for a given input
  
- `validprereq.out`: Validation results for prerequisite chains

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Important Note

This tool is designed for academic planning and prerequisite validation. Ensure input files are correctly formatted to avoid errors in processing.

