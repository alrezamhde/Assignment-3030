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

import java.util.HashMap;
import java.util.Map;


/**
 * A class that represents a position book, which tracks the number of shares of various securities owned.
 * Positions are stored in a map with the security ticker as the key and the number of shares owned as the value.
 */
public class PositionBook {
	private Map<String,Integer> positions = new HashMap<String,Integer>();

	/**
	 * Updates position in a security (number of security shares owned) to a given quantity. 
	 * 
	 * @param tkr The ticker of the security. 
	 * @param quantity The new amount owned by the account.
	 */
	public void updatePosition(String tkr, int quantity){
		positions.put(tkr, quantity);
	}

	/**
	 * Get the number of shares in a security owned
	 * @param tkr The ticker of the security
	 * @return The quanity of shares in the given security. It is 0 if the security is not part of the book.
	 */
	public int getPosition(String tkr) {
		Integer pos = positions.get(tkr);
		if (pos == null) {
			pos = 0;
		}
		return pos;
	}
	
    /**
     * Adds the specified quantity to the current position of a security.
     * If the security does not already exist in the book, it creates a new entry with the specified quantity.
     *
     * @param tkr the ticker symbol of the security
     * @param addedQty the quantity of shares to be added to the current position
     */
	public void addToPosition(String tkr, int addedQty) {
		Integer currPos = positions.get(tkr);
		if (currPos == null) {
			currPos = 0;
		}
		positions.put(tkr,addedQty + currPos);
	}

	
    /**
     * Deducts the specified quantity from the current position of a security.
     * If the security does not exist or if there are insufficient shares to remove, an error is logged.
     *
     * @param tkr the ticker symbol of the security
     * @param removeQty the quantity of shares to be removed from the current position
     */
	public void deductFromPosition(String tkr, int removeQty) {
		Integer currPos = positions.get(tkr);
		if (currPos == null) {
			System.err.println("Error in PositionBook#deductFromPosition: position in " + tkr + " does not exist");
			return;
		} else if (currPos < removeQty) {
			System.err.println("Error in PositionBook#deductFromPosition: current position in " + tkr + " insufficient.");
			return;
		}
		positions.put(tkr,currPos - removeQty);
	}

}
