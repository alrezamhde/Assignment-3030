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


import java.util.Map;


public class App {
	
    public static void main(String[] args) {
        ArgumentProcessor processor = new ArgumentProcessor();
        Map<String, String> arguments = processor.parseArguments(args);

        if (arguments == null) {
            return; // Invalid arguments or help requested, so exit
        }

    	Exchange exc = new Exchange();

    	
    	exc.readSecurityListfromFile(arguments.get("securities")); 
    	exc.readAccountsListFromFile(arguments.get("accounts"));
    	exc.readInitialPositionsFromFile(arguments.get("initial_positions"));
    	String init_balances = exc.printBalances(true);
    	exc.processOrderFile(arguments.get("orders"));
    	
    	System.out.println(exc.printAskTable(false));
    	System.out.println(exc.printBidTable(false));
    	System.out.println(exc.printTradesLog(true));
    	System.out.println(init_balances);
    	System.out.println(exc.printBalances(true));
    	System.out.println(exc.printFeesCollected(true));
    }
}
