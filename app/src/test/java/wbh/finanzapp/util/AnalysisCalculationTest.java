package wbh.finanzapp.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.AnalysisBean;
import wbh.finanzapp.business.TransactionBean;

@RunWith(JUnit4.class)
public class AnalysisCalculationTest {

    private static List<AbstractBean> transactions;
    private static Date startDate = new Date(1559347200000L); // Sat Jun 01 2019 02:00:00 GMT+0200
    private static Date endDate = new Date(1567209600000L); // Sat Aug 31 2019 02:00:00 GMT+0200
    private static Date firstDay_20 = new Date(946684800000L); // Sat Jan 01 2000 01:00:00 GMT+0100
    private static Date lastDay_20 = new Date(1609372800000L); // Thu Dec 31 2020 01:00:00 GMT+0100

    @Test
    public void uniqueCalculationTest() {
        AnalysisBean analysisBean;
        Long uniqueDate = 1562198400000L; // Thu Jul 04 2019 02:00:00 GMT+0200
        Date unique_Date = new Date(1562198400000L); // Thu Jul 04 2019 02:00:00 GMT+0200

        setUpTransactionBean(1, uniqueDate, null, null, null, null);
        Assert.assertNotNull(transactions);

        //check over period of time
        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(1, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //check over long period of time
        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);
        Assert.assertEquals(1, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //startDate > endDate and included
        analysisBean = AnalysisCalculation.createAnalysisBean(endDate, startDate, transactions);
        Assert.assertEquals(0, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //startDate == endDate and not included
        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, startDate, transactions);
        Assert.assertEquals(0, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(00.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(00.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //startDate == endDate and included
        analysisBean = AnalysisCalculation.createAnalysisBean(unique_Date, unique_Date, transactions);
        Assert.assertEquals(1, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);
    }

    @Test
    public void dailyCalculationTest() {
        AnalysisBean analysisBean;

        setUpTransactionBean(2, null, null, null, null, null);
        Assert.assertNotNull(transactions);

        //check weeklyCalculation over a long period of 20 years
        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);
        Assert.assertEquals(7671, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(76710.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //check dailyCalculation for a fixed period of time
        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(92, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(920.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //startDate > endDate
        analysisBean = AnalysisCalculation.createAnalysisBean(endDate, startDate, transactions);
        Assert.assertEquals(0, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //startDate == endDate
        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, startDate, transactions);
        Assert.assertEquals(1, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);
    }

    @Test
    public void weeklyCalculationTest() {
        AnalysisBean analysisBean;
        Date firstDay;
        Date lastDay;

        //offset = -2; sunday should be 1;
        Integer sunday = 6;
        Integer monday = 0;
        Integer tuesday = 1;
        Integer wednesday = 2;
        Integer thursday = 3;
        Integer friday = 4;
        Integer saturday = 5;

        //startDate == endDate
        //transaction not included
        setUpTransactionBean(3, null, monday, null, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, startDate, transactions);
        Assert.assertEquals(0, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //transacion included
        setUpTransactionBean(3, null, saturday, null, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, startDate, transactions);
        Assert.assertEquals(1, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //startDate > endDate
        setUpTransactionBean(3, null, monday, null, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(endDate, startDate, transactions);
        Assert.assertEquals(0, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //startDate < endDate
        //monday is not included
        firstDay = new Date(946684800000L); // Sat Jan 01 2000 01:00:00 GMT+0100
        lastDay = new Date(946771200000L); // Sun Jan 02 2000 01:00:00 GMT+0100

        setUpTransactionBean(3, null, monday, null, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay, lastDay, transactions);
        Assert.assertEquals(0, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //Check weeklyCalculation for a fixed period of time for every weekday
        setUpTransactionBean(3, null, monday, null, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(13, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(130.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(3, null, tuesday, null, null, null);
        Assert.assertNotNull(transactions);
        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

        Assert.assertEquals(13, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(130.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(3, null, wednesday, null, null, null);
        Assert.assertNotNull(transactions);
        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

        Assert.assertEquals(13, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(130.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(3, null, thursday, null, null, null);
        Assert.assertNotNull(transactions);
        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

        Assert.assertEquals(13, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(130.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(3, null, friday, null, null, null);
        Assert.assertNotNull(transactions);
        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

        Assert.assertEquals(13, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(130.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(3, null, saturday, null, null, null);
        Assert.assertNotNull(transactions);
        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

        Assert.assertEquals(14, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(140.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(3, null, sunday, null, null, null);
        Assert.assertNotNull(transactions);
        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

        Assert.assertEquals(13, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(130.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //check weeklyCalculation for a long period of 20 years
        setUpTransactionBean(3, null, monday, null, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);
        Assert.assertEquals(1096, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(10960.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);
    }

    @Test
    public void monthlyCalculationTest() {
        AnalysisBean analysisBean;
        Integer monthlyDay_1 = 1;
        Integer monthlyDay_2 = 2;
        Integer monthlyDay_3 = 3;
        Integer monthlyDay_4 = 4;
        Integer monthlyDay_5 = 5;
        Integer monthlyDay_6 = 6;
        Integer monthlyDay_7 = 7;
        Integer monthlyDay_8 = 8;
        Integer monthlyDay_9 = 9;
        Integer monthlyDay_10 = 10;
        Integer monthlyDay_11 = 11;
        Integer monthlyDay_12 = 12;
        Integer monthlyDay_13 = 13;
        Integer monthlyDay_14 = 14;
        Integer monthlyDay_15 = 15;
        Integer monthlyDay_16 = 16;
        Integer monthlyDay_17 = 17;
        Integer monthlyDay_18 = 18;
        Integer monthlyDay_19 = 19;
        Integer monthlyDay_20 = 20;
        Integer monthlyDay_21 = 21;
        Integer monthlyDay_22 = 22;
        Integer monthlyDay_23 = 23;
        Integer monthlyDay_24 = 24;
        Integer monthlyDay_25 = 25;
        Integer monthlyDay_26 = 26;
        Integer monthlyDay_27 = 27;
        Integer monthlyDay_28 = 28;
        Integer monthlyDay_29 = 29;
        Integer monthlyDay_30 = 30;
        Integer monthlyDay_31 = 31;


        //startDate == endDate transaction included
        setUpTransactionBean(4, null, null, monthlyDay_1, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, startDate, transactions);

        Assert.assertEquals(1, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //startDate == endDate transaction not included
        setUpTransactionBean(4, null, null, monthlyDay_2, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, startDate, transactions);

        Assert.assertEquals(0, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //startDate > endDate
        setUpTransactionBean(4, null, null, monthlyDay_1, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(endDate, startDate, transactions);

        Assert.assertEquals(0, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(00.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(00.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //Check weeklyCalculation for a fixed period of time for every day of month
        setUpTransactionBean(4, null, null, monthlyDay_1, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_2, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_3, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_4, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_5, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_6, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_6, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_7, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_8, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_9, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_10, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_11, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_12, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_13, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_14, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_15, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_16, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_17, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_18, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_19, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_20, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_21, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_22, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_23, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_24, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_25, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_26, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_27, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        setUpTransactionBean(4, null, null, monthlyDay_28, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);


        //#TODO 01.01.2000 - 31.12.2020 -> 2004, 2008, 2012, 2016, 2020 -> leap-year -> 252 - 21 (february) + 5 (leap-year 29daysOfMonth) = 236
        /*setUpTransactionBean(4, null, null, monthlyDay_29, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(236, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2360.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);*/

        //#TODO 01.01.2000 - 31.12.2020 -> 252 - 21 (february) = 231
        /*setUpTransactionBean(4, null, null, monthlyDay_30, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(231, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2310.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);*/

        //#TODO 01.01.2000 - 31.12.2020 -> 252 - ( 5 * 21 ) (5 month per year with less then 31 days) = 147
        /*setUpTransactionBean(4, null, null, monthlyDay_31, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(147, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(1470.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);*/


        //check monthlyCalculation for a short period
        setUpTransactionBean(4, null, null, monthlyDay_15, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

        Assert.assertEquals(3, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(30.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        //check monthlyCalculation for a long period of 20 years
        setUpTransactionBean(4, null, null, monthlyDay_15, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay_20, lastDay_20, transactions);

        Assert.assertEquals(252, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(2520.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);
    }

    @Test
    public void yearlyCalculationTest() {
        Integer yearlyMonth = 7;
        Integer yearlyDay = 15;
        setUpTransactionBean(5, null, null, null, yearlyMonth, yearlyDay);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(1, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);
    }

    private void setUpTransactionBean(int state, Long uniqueDate, Integer dayOfWeek, Integer monthlyDay, Integer yearlyMonth, Integer yearlyDay) {
        transactions = new ArrayList<>();
        TransactionBean transactionBean = new TransactionBean(10, "testName", "testDescription", 5, 20, 10.0,
                state, uniqueDate, dayOfWeek, monthlyDay, yearlyMonth, yearlyDay);
        transactions.add(transactionBean);
    }
}
