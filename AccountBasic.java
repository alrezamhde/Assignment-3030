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
 * A basic exchange {@linkplain Account}.
 * This type of account applies a fixed fee for all trades.
 */
public class AccountBasic extends Account {

    /**
     * Constructs an {@linkplain AccountBasic} with an associated {@linkplain Trader}
     * and an initial balance.
     *
     * @param trader      the {@linkplain Trader} associated with this account
     * @param initBalance the initial balance of the account
     */
    AccountBasic(Trader trader, long initBalance) {
        super(trader, initBalance);
    }

    /**
     * Returns the fixed fee for all trades.
     *
     * @param t the {@linkplain ITrade} for which the fee is applied (unused in this implementation)
     * @return the fixed fee amount, which is always 450
     */
    @Override
    public int getFee(ITrade t) {
        return 450;
    }
}
