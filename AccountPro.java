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

import ca.yorku.cmg.lob.trader.Trader;
import ca.yorku.cmg.lob.tradestandards.ITrade;

/**
 * An advanced exchange {@linkplain Account}.
 * This type of account calculates fees based on trade quantity, 
 * with a minimum fee of 1000 cents (10 dollars).
 */
public class AccountPro extends Account {

    /**
     * Constructs an {@linkplain AccountPro} with an associated {@linkplain Trader}
     * and an initial balance.
     *
     * @param trader      the {@linkplain Trader} associated with this account
     * @param initBalance the initial balance of the account
     */
    AccountPro(Trader trader, long initBalance) {
        super(trader, initBalance);
    }

    /**
     * Calculates the fee for a trade based on its quantity.
     * The fee (in cents) is equal to the trade quantity multiplied by 1, 
     * but not less than 1000 cents.
     *
     * @param t the {@linkplain ITrade} for which the fee is calculated
     * @return the calculated fee, which is the greater of the quantity or 1000 (in cents)
     */
    @Override
    public int getFee(ITrade t) {
        return Math.max(t.getQuantity() * 1, 1000);
    }
}