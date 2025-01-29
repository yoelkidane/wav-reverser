import javax.sound.sampled.*;
import java.io.*;

/**
 * This class handles the process of reversing an audio file.
 *
 * It includes methods to convert WAV to DAT, reverse the DAT file, and convert back to WAV.
 * It supports custom stack implementations ArrayStack and ListStack for the reversal of data.
 */
public class BackMasking {

    private static BKStack stack; // Custom stack implementation


    /**
     * Main method to run the program.
     *
     * @param args Command-line arguments: <stack type> <input wav> <output wav> [keep]
     */
    public static void main(String[] args) {
        if (args.length < 3 || args.length > 4) {
            System.err.println("Usage: java BackMasking <stack type> <input wav> <output wav> [keep]");
            System.exit(1);
        }

        String stackType = args[0];
        String inputWav = args[1];
        String tempDat = "temp.dat";
        String reversedDat = "reversed.dat";
        String outputWav = args[2];
        boolean keepFiles = args.length == 4 && args[3].equalsIgnoreCase("keep");

        // Initialize the stack based on user input
        if (stackType.equalsIgnoreCase("array")) {
            stack = new ArrayStack();
        } else if (stackType.equalsIgnoreCase("list")) {
            stack = new ListStack();
        } else {
            System.err.println("Invalid stack type. Please choose 'array' or 'list'.");
            System.exit(1);
        }

        // WAV convert to DAT
        convertToDat(inputWav, tempDat);

        // Reverse the DAT file
        reverseDatFile(tempDat, reversedDat);

        // DAT convert to WAV
        convertDatToWav(reversedDat, outputWav);

        // If not prompted, deleted extra files
        if (!keepFiles) {
            deleteFile(tempDat);
            deleteFile(reversedDat);
        }

        System.out.println("Processing complete: " + outputWav);
    }

    /*
        Helper Methods
    */

    /**
     * Converts a WAV file to a DAT file with time-stamped sample values.
     *
     * @param inputFile Path to the input WAV file.
     * @param outputFile Path to the output DAT file.
     */
    private static void convertToDat(String inputFile, String outputFile) {
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(inputFile));
             PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)))) {

            AudioFormat format = audioStream.getFormat();
            int sampleRate = (int) format.getSampleRate();
            writer.println("; Sample Rate " + sampleRate);

            // Assuming 16-bit PCM
            byte[] buffer = new byte[2];
            int stepCount = 0;
            while (audioStream.read(buffer) != -1) {
                // Convert bytes to PCM
                short sample = (short) ((buffer[1] << 8) | (buffer[0] & 0xFF));
                double timeStep = (double) stepCount / sampleRate;
                writer.println(timeStep + "\t" + sample);
                stepCount++;
            }

            System.out.println("Converted " + inputFile + " to " + outputFile);
        } catch (Exception e) {
            System.err.println("Error converting WAV to .dat: " + e.getMessage());
        }
    }

    /**
     * Reverses the content of a DAT file.
     *
     * @param inputFile Path to the input DAT file.
     * @param outputFile Path to the output reversed DAT file.
     */
    private static void reverseDatFile(String inputFile, String outputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)))) {

            // Read the sample header
            String header = reader.readLine();
            if (header == null || !header.startsWith("; Sample Rate")) {
                throw new IOException("Invalid .dat format: Missing sample rate");
            }
            // Maintain the files header
            writer.println(header);

            while (!stack.isEmpty()) {
                stack.pop();
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(";")) {
                    String[] parts = line.split("\t");
                    if (parts.length > 1) {
                        // Store the non header lines in stack
                        stack.push(Double.parseDouble(parts[1]));
                    }
                }
            }

            double timeStep = 0;
            double sampleRate = Double.parseDouble(header.split(" ")[3]);
            while (!stack.isEmpty()) {
                // Re-create the original file in reverse order
                writer.println(timeStep + "\t" + stack.pop());
                timeStep += 1.0 / sampleRate;
            }

            System.out.println("Reversed " + inputFile + " -> " + outputFile);
        } catch (Exception e) {
            System.err.println("Error reversing .dat file: " + e.getMessage());
        }
    }

    /**
     * Converts a reversed DAT file back to a WAV file.
     *
     * @param inputFile Path to the input reversed DAT file.
     * @param outputFile Path to the output WAV file.
     */
    private static void convertDatToWav(String inputFile, String outputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {

            // Find the sample rate heading
            String line = reader.readLine();
            if (line == null || !line.startsWith("; Sample Rate")) {
                throw new IOException("Invalid .dat format: Missing sample rate");
            }
            int sampleRate = Integer.parseInt(line.split(" ")[3]);

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(";")) continue;

                String[] parts = line.split("\t");
                if (parts.length < 2) continue;

                double sampleValue = Double.parseDouble(parts[1]);

                // Convert double value (-1.0 to 1.0) into signed 16-bit PCM
                short pcmSample = (short) (sampleValue * Short.MAX_VALUE);

                // Write in little-endian format
                bytes.write(pcmSample & 0xFF);
                bytes.write((pcmSample >> 8) & 0xFF);
            }

            byte[] audioData = bytes.toByteArray();
            AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
            AudioInputStream audioStream = new AudioInputStream(
                    new ByteArrayInputStream(audioData), format, audioData.length / 2);

            AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, new File(outputFile));

            System.out.println("Successfully converted " + inputFile + " to " + outputFile);
        } catch (Exception e) {
            System.err.println("Error converting .dat to WAV: " + e.getMessage());
        }
    }

    /**
     * Deletes a specified file when prompted in args.
     *
     * @param filename Path to the file to be deleted.
     */
    private static void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Deleted intermediate file: " + filename);
            } else {
                System.err.println("Failed to delete file: " + filename);
            }
        }
    }


}
