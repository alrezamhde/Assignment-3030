package ca.yorku.cmg.lob.exchange;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ca.yorku.cmg.lob.orderbook.Ask;
import ca.yorku.cmg.lob.orderbook.Bid;
import ca.yorku.cmg.lob.orderbook.OrderOutcome;
import ca.yorku.cmg.lob.orderbook.Orderbook;
import ca.yorku.cmg.lob.orderbook.Trade;
import ca.yorku.cmg.lob.security.Security;
import ca.yorku.cmg.lob.security.SecurityList;
import ca.yorku.cmg.lob.trader.Trader;
import ca.yorku.cmg.lob.trader.TraderInstitutional;
import ca.yorku.cmg.lob.trader.TraderRetail;
import ca.yorku.cmg.lob.tradestandards.IOrder;
import ca.yorku.cmg.lob.tradestandards.ITrade;

public class Exchange {

    Orderbook book;
    SecurityList securities = new SecurityList();
    AccountsList accounts = new AccountsList();
    ArrayList<Trade> tradesLog = new ArrayList<Trade>();
    long totalFees = 0;

    public Exchange() {
        book = new Orderbook();
    }

    public boolean validateOrder(IOrder o) {
        if (securities.getSecurityByTicker(o.getSecurity().getTicker()) == null) {
            System.err.println("Order validation: ticker " + o.getSecurity().getTicker() + " not supported.");
            return false;
        }
        if (accounts.getTraderAccount(o.getTrader()) == null) {
            System.err.println("Order validation: trader with ID " + o.getTrader().getID() + " not registered with the exchange.");
            return false;
        }
        int pos = accounts.getTraderAccount(o.getTrader()).getPosition(o.getSecurity().getTicker());
        long bal = accounts.getTraderAccount(o.getTrader()).getBalance();
        if ((o instanceof Ask) && (pos < o.getQuantity())) {
            System.err.println("Order validation: seller with ID " + o.getTrader().getID() + ": has " + pos + " and tries to sell " + o.getQuantity());
            return false;
        }
        if ((o instanceof Bid) && (bal < o.getValue())) {
            System.err.println(String.format("Order validation: buyer with ID %d does not have enough balance: has $%,.2f and tries to buy for $%,.2f",
                    o.getTrader().getID(), bal / 100.0, o.getValue() / 100.0));
            return false;
        }
        return true;
    }

    public void submitOrder(IOrder o, long time) {
        if (!validateOrder(o)) {
            return;
        }
        OrderOutcome oOutcome;
        if (o instanceof Bid) {
            oOutcome = book.getAsks().processOrder(o, time);
            if (oOutcome.getUnfulfilledOrder().getQuantity() > 0) {
                book.getBids().addOrder(oOutcome.getUnfulfilledOrder());
            }
        } else {
            oOutcome = book.getBids().processOrder(o, time);
            if (oOutcome.getUnfulfilledOrder().getQuantity() > 0) {
                book.getAsks().addOrder(oOutcome.getUnfulfilledOrder());
            }
        }
        if (oOutcome.getResultingTrades() != null) {
            tradesLog.addAll(oOutcome.getResultingTrades());
        } else {
            return;
        }
        for (ITrade t : oOutcome.getResultingTrades()) {
            t.setBuyerFee(accounts.getTraderAccount(t.getBuyer()).getFee(t));
            accounts.getTraderAccount(t.getBuyer()).applyFee(t);
            accounts.getTraderAccount(t.getBuyer()).withdrawMoney((int) t.getValue());
            accounts.getTraderAccount(t.getBuyer()).addToPosition(t.getSecurity().getTicker(), t.getQuantity());
            t.setSellerFee(accounts.getTraderAccount(t.getSeller()).getFee(t));
            accounts.getTraderAccount(t.getSeller()).applyFee(t);
            accounts.getTraderAccount(t.getSeller()).addMoney((int) t.getValue());
            accounts.getTraderAccount(t.getSeller()).deductFromPosition(t.getSecurity().getTicker(), t.getQuantity());
            this.totalFees += t.getBuyerFee() + t.getSellerFee();
        }
    }

    public void readSecurityListfromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    String tkr = parts[0].trim();
                    String title = parts[1].trim();
                    securities.addSecurity(new Security(tkr, title));
                } else {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readAccountsListFromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length >= 4) {
                    String traderTitle = parts[0].trim();
                    String traderType = parts[1].trim();
                    String accType = parts[2].trim();
                    long initBalance = Long.parseLong(parts[3].trim());
                    Trader t;
                    if (traderType.equals("Retail")) {
                        t = new TraderRetail(traderTitle);
                    } else {
                        t = new TraderInstitutional(traderTitle);
                    }
                    if (accType.equals("Basic")) {
                        accounts.addAccount(new AccountBasic(t, initBalance));
                    } else {
                        accounts.addAccount(new AccountPro(t, initBalance));
                    }
                } else {
                    System.err.println("Skipping malformed line (two few attributes): " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInitialPositionsFromFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length >= 3) {
                    int traderID = Integer.valueOf(parts[0].trim());
                    String tkr = parts[1].trim();
                    int qty = Integer.valueOf(parts[2].trim());
                    Trader t = getAccounts().getTraderByID(traderID);
                    if (t != null) {
                        accounts.getTraderAccount(t).addToPosition(tkr, qty);
                    }
                } else {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processOrderFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length >= 6) {
                    int traderID = Integer.valueOf(parts[0].trim());
                    String tkr = parts[1].trim();
                    String type = parts[2].trim();
                    int qty = Integer.valueOf(parts[3].trim());
                    int price = Integer.valueOf(parts[4].trim());
                    long time = Long.valueOf(parts[5].trim());
                    Trader t = getAccounts().getTraderByID(traderID);
                    Security sec = getSecurities().getSecurityByTicker(tkr);
                    if ((t != null) && (sec != null)) {
                        if (type.equals("ask")) {
                            submitOrder(new Ask(t, sec, price, qty, time), time);
                        } else if (type.equals("bid")) {
                            submitOrder(new Bid(t, sec, price, qty, time), time);
                        } else {
                            System.err.println("Order type not found (skipping): " + line);
                        }
                    }
                } else {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String printAskTable(boolean header) {
        return book.getAsks().toString(header);
    }

    public String printBidTable(boolean header) {
        return book.getBids().toString(header);
    }

    public String printTradesLog(boolean header) {
        String output = "";
        if (header) {
            output = "[From____  To______  Tkr_  Quantity  Price__  Time____]\n";
        }
        for (Trade t : tradesLog) {
            output += t.toString();
        }
        return output;
    }

    public String printBalances(boolean header) {
        return accounts.printBalances(header);
    }

    public String printFeesCollected(boolean header) {
        if (!header) {
            return String.format("$%,.2f\n", totalFees / 100.0);
        }
        return String.format("[Fees collected: %s]\n", String.format("$%,.2f", totalFees / 100.0));
    }

    public SecurityList getSecurities() {
        return securities;
    }

    public AccountsList getAccounts() {
        return accounts;
    }
}
