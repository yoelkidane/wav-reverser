# Wav Reverser
A Java-based program for reversing WAV audio files using various custom stack implementations.



## Overview
This program is a tool for reversing **WAV audio files**. It accomplishes this by converting a given WAV file into a **DAT format**. This DAT file has time-stamped sample values and reverses this content
using a custom stack implementation (either **ArrayStack** or **ListStack**). It then converts the reversed DAT format back into a WAV file. This process results in a **"Backmasked"** audio output.


## Installation
### Prerequisites
- Git
- Java 8 or higher

### Steps

1. **Clone the repository**
```
git clone https://github.com/yoelkidane/wav-reverser.git
cd wav-reverser
```

2. **Build the program**: Compile the project using `javac`:
```bash
javac BackMasking.java
```

  
3. **Run the program:** Execute the program using:
```bash
java BackMasking <stack type> <input.wav> <output.wav> [keep]
```
`<stack type>`: Choose either "array" or "list" for the stack implementation.<br>
`<input.wav>`: Path to the input WAV file.<br>
`<output.wav>`: Desired path to reversed WAV file.<br>
`[keep]` (optional): Add this argument at the end of the command if you would like to view the intermediate DAT files.<br>



## Sample Output
Test your program by running it with different WAV files and listening to the backmasked audio.
```
Converted input.wav to temp.dat
Reversed temp.dat -> reversed.dat
Successfully converted reversed.dat to endfile.wav
Deleted file: temp.dat
Deleted file: reversed.dat
Processing complete: endfile.wav
```
