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

import java.util.ArrayList;

import ca.yorku.cmg.lob.trader.Trader;


/**
 * A class that manages a list of {@linkplain Account} objects.
 * Provides functionality for adding accounts, retrieving traders by ID,
 * finding accounts associated with specific traders, and printing balances for debugging.
 */
public class AccountsList {
	private ArrayList<Account> accounts = new ArrayList<>();
	
    /**
     * Adds an {@linkplain Account} to the list.
     *
     * @param a the {@linkplain Account} to be added
     */
	public void addAccount(Account a) {
		accounts.add(a);
	}
	
	/**
	 * Get a trader object by ID
	 * @param tid The id of the trader (as it appears in, e.g., files and lists) 
	 * @return A {@linkplain ca.yorku.cmg.lob.trader.Trader} object that matches the ID. Null if none was found.
	 */
	public Trader getTraderByID(int tid) {
		Trader t = null;
		for (Account a: accounts) {
			t = a.getTrader(); 
			if (t.getID() == tid) {
				return (t);
			}
		}
		return (t);
	}
	
	
	/**
	 * Get the {@linkplain ca.yorku.cmg.lob.exchange.Exchange} {@linkplain ca.yorku.cmg.lob.exchange.Account} object corresponding to a trader {@code t} 
	 * @param t The {@linkplain ca.yorku.cmg.lob.trader.Trader} object for which the account is sought. 
	 * @return The {@linkplain ca.yorku.cmg.lob.exchange.Account} object corresponding to the trader, {@ code null} if the trader does not have an account.
	 */
	public Account getTraderAccount(Trader t) {
		Account foundAccount = null;
		
		for (Account a : accounts) {
			if (a.getTrader() == t) {
				foundAccount = a;
			}
		}
		return(foundAccount);
	}
	
	
    /**
     * Generates a string representation of the balances of all accounts for debugging purposes.
     *
     * @param header {@code true} to include a header in the output, {@code false} otherwise
     * @return a formatted string displaying the balances of all accounts and their total
     */
	public String debugPrintBalances(boolean header) {
		String out = "";
		if (header) {
			out = "[_Institution_____________________ Balance_________]\n";
		}
		long total = 0;
		
		for (Account a : accounts) {
			String ttl = a.getTrader().getTitle();
			total += a.getBalance();
			out += String.format("[%2d %30s %16s]\n",
	        // Truncate the string if it exceeds the desired length
	        a.getTrader().getID(),(ttl.length() > 30 ? ttl.substring(0, 30) : ttl), String.format("$%,.2f",a.getBalance()/100.0));
		}
		out += String.format("[%30s %16s]\n",
		        "                           TOTAL:", String.format("$%,.2f",total/100.0));
		return(out);
	}
	
}
