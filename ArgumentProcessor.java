/**
 * Copyright (C) Sotirios Liaskos (liaskos@yorku.ca) - All Rights Reserved
 * 
 * This source code is protected under international copyright law.  All rights
 * reserved and protected by the copyright holder.
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holder.  If you encounter this file and do not have
 * permission, please contact the copyright holder and delete this file.
 */
package ca.yorku.cmg.lob.exchange;

import java.io.File;
import java.util.HashMap;
import java.util.Map;



/**
 * A utility class for processing command-line arguments.
 * Parses and validates arguments to ensure they correspond to valid file paths or options.
 */
class ArgumentProcessor {

	
    /**
     * Parses command-line arguments and validates the file paths provided for specific options.
     *
     * <p>Supported options:</p>
     * <ul>
     *   <li>{@code -s <path>} : Path to the CSV file with Securities</li>
     *   <li>{@code -a <path>} : Path to the CSV file with Accounts</li>
     *   <li>{@code -i <path>} : Path to the CSV file with Initial Positions</li>
     *   <li>{@code -o <path>} : Path to the CSV file with Orders to process</li>
     *   <li>{@code -h} : Print usage information</li>
     * </ul>
     *
     * @param args the array of command-line arguments
     * @return a {@linkplain Map} where keys are option names (e.g., "securities", "accounts") and 
     *         values are the corresponding file paths, or {@code null} if parsing fails
     */
    public Map<String, String> parseArguments(String[] args) {
        if (args.length == 0) {
            printUsage();
            return null;
        }

        Map<String, String> arguments = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s":
                    if (i + 1 < args.length) {
                        String securitiesPath = args[++i];
                        if (validateFile(securitiesPath)) {
                            arguments.put("securities", securitiesPath);
                        } else {
                            return null; // File not valid, exit
                        }
                    } else {
                        System.out.println("Error: Missing value for -s option.");
                        printUsage();
                        return null;
                    }
                    break;

                case "-a":
                    if (i + 1 < args.length) {
                        String accountsPath = args[++i];
                        if (validateFile(accountsPath)) {
                            arguments.put("accounts", accountsPath);
                        } else {
                            return null; // File not valid, exit
                        }
                    } else {
                        System.out.println("Error: Missing value for -a option.");
                        printUsage();
                        return null;
                    }
                    break;

                case "-i":
                    if (i + 1 < args.length) {
                        String positionsPath = args[++i];
                        if (validateFile(positionsPath)) {
                            arguments.put("initial_positions", positionsPath);
                        } else {
                            return null; // File not valid, exit
                        }
                    } else {
                        System.out.println("Error: Missing value for -i option.");
                        printUsage();
                        return null;
                    }
                    break;

                case "-o":
                    if (i + 1 < args.length) {
                        String ordersPath = args[++i];
                        if (validateFile(ordersPath)) {
                            arguments.put("orders", ordersPath);
                        } else {
                            return null; // File not valid, exit
                        }
                    } else {
                        System.out.println("Error: Missing value for -o option.");
                        printUsage();
                        return null;
                    }
                    break;

                case "-h":
                    printUsage();
                    return null;

                default:
                    System.out.println("Error: Unknown option " + args[i]);
                    printUsage();
                    return null;
            }
        }

        return arguments;
    }

    /**
     * Validates the existence and readability of a file at the specified path.
     *
     * @param filePath the path to the file
     * @return {@code true} if the file exists and is readable; {@code false} otherwise
     */
    private boolean validateFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Error: File not found - " + filePath);
            return false;
        }
        if (!file.canRead()) {
            System.out.println("Error: File cannot be read - " + filePath);
            return false;
        }
        return true;
    }

    /**
     * Prints the usage information for the application.
     */
    private void printUsage() {
        System.out.println("Usage:");
        System.out.println("  -s <path> : Path to the CSV file with Securities");
        System.out.println("  -a <path> : Path to the CSV file with Accounts");
        System.out.println("  -i <path> : Path to the CSV file with Initial positions");
        System.out.println("  -o <path> : Path to the CSV file with Orders to process");
        System.out.println("  -h        : Print this usage information");
    }
}