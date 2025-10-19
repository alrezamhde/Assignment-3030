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
 * Abstract class representing a financial account associated with a {@linkplain ca.yorku.cmg.lob.trader.Trader}.
 * Manages the account's balance, fee application, and positions in a {@linkplain ca.yorku.cmg.lob.exchange.PositionBook}.
 */
public abstract class Account {

    /** The {@linkplain ca.yorku.cmg.lob.trader.Trader} associated with this account. */
    private Trader trader;

    /** The {@linkplain ca.yorku.cmg.lob.exchange.PositionBook} managing the positions for this account. */
    private PositionBook book;

    /** The current balance of the account in monetary units. */
    private long balance;

    /**
     * Constructs an {@linkplain ca.yorku.cmg.lob.exchange.Account} with an associated {@linkplain ca.yorku.cmg.lob.trader.Trader} and an initial balance.
     *
     * @param trader      the {@linkplain ca.yorku.cmg.lob.trader.Trader} associated with this account
     * @param initBalance the initial balance of the account
     */
    Account(Trader trader, long initBalance) {
        this.trader = trader;
        this.balance = initBalance;
        this.book = new PositionBook();
    }

    /**
     * Returns the {@linkplain ca.yorku.cmg.lob.trader.Trader} associated with this account.
     *
     * @return the {@linkplain ca.yorku.cmg.lob.trader.Trader} associated with this account
     */
    public Trader getTrader() {
        return trader;
    }

    /**
     * Returns the current balance of the account.
     *
     * @return the current balance
     */
    public long getBalance() {
        return balance;
    }

    /**
     * Adds a specified amount to the account balance.
     *
     * @param amount the amount to add to the balance
     */
    public void addMoney(int amount) {
        balance += amount;
    }

    /**
     * Withdraws a specified amount from the account balance, if sufficient funds are available.
     *
     * @param amount the amount to withdraw
     */
    public void withdrawMoney(int amount) {
        if (balance >= amount) {
            balance -= amount;
        }
    }

    /**
     * Applies a fee associated with a {@linkplain ca.yorku.cmg.lob.tradestandards.ITrade} to the account balance.
     *
     * @param t the {@linkplain ITrade} for which the fee is applied
     */
    public void applyFee(ITrade t) {
        balance -= this.getFee(t);
    }

    /**
     * Returns the fee associated with a given {@linkplain ca.yorku.cmg.lob.tradestandards.ITrade}.
     * This method must be implemented by subclasses.
     *
     * @param t the {@linkplain ITrade} for which the fee is calculated
     * @return the fee amount
     */
    public abstract int getFee(ITrade t);

    /**
     * Updates the quantity (number of units) of a specific position in the {@linkplain ca.yorku.cmg.lob.exchange.PositionBook}.
     *
     * @param tkr    the ticker symbol of the position
     * @param newQty the new quantity of the position (number of units) 
     */
    public void updatePosition(String tkr, int newQty) {
        this.book.updatePosition(tkr, newQty);
    }

    /**
     * Retrieves the current quantity (number of units) of a specific position from the {@linkplain ca.yorku.cmg.lob.exchange.PositionBook}.
     *
     * @param tkr the ticker symbol of the position
     * @return the quantity of the position (number of units) 
     */
    public int getPosition(String tkr) {
        return this.book.getPosition(tkr);
    }

    /**
     * Adds a specified quantity to a position in the {@linkplain ca.yorku.cmg.lob.exchange.PositionBook}.
     *
     * @param tkr       the ticker symbol of the position
     * @param addedQty  the quantity to add
     */
    public void addToPosition(String tkr, int addedQty) {
        this.book.addToPosition(tkr, addedQty);
    }

    /**
     * Deducts a specified quantity from a position in the {@linkplain ca.yorku.cmg.lob.exchange.PositionBook}.
     *
     * @param tkr    the ticker symbol of the position
     * @param newQty the quantity to deduct
     */
    public void deductFromPosition(String tkr, int newQty) {
        this.book.deductFromPosition(tkr, newQty);
    }
}
