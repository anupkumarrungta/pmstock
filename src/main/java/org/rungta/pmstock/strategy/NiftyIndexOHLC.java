package org.rungta.pmstock.strategy;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Date;

@DynamoDBTable(tableName = "NiftyIndexOHLCTable")
public class NiftyIndexOHLC {


    private Date date;
    private double Close;
    private double High;
    private double Low;
    private double Open;
    private double SharesTraded;
    private double Turnover;


    public NiftyIndexOHLC(Date date, double open, double high, double low, double close, double sharesTraded, double turnover ) {
        this.date = date;
        this.Open = open;
        this.High = high;
        this.Low = low;
        this.Close = close;
        this.SharesTraded = sharesTraded;
        this.Turnover = turnover;
    }

    public Date getDate() {
        return date;
    }

    public double getOpen() { return Open; }
    public double getHigh() { return High; }
    public double getLow() { return Low; }
    public double getClose() { return Close; }
    public double getSharesTraded() { return SharesTraded; }
    public double getTurnover() { return Turnover; }
}