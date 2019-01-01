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

    @Test
    public void uniqueCalculationTest() {
        Long uniqueDate = 1562198400000L; // Thu Jul 04 2019 02:00:00 GMT+0200
        setUpTransactionBean(1, uniqueDate, null, null, null, null);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(1, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);
    }

    @Test
    public void dailyCalculationTest() {
        setUpTransactionBean(2, null, null, null, null, null);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(92, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(920.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
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

        //Check weeklyCalculation for a fixed period of time, one by one, for every weekday
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

        //check weeklyCalculation over a long period of 20 years
        firstDay = new Date(946684800000L); // Sat Jan 01 2000 01:00:00 GMT+0100
        lastDay = new Date(1609372800000L); // Thu Dec 31 2020 01:00:00 GMT+0100

        setUpTransactionBean(3, null, monday, null, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay, lastDay, transactions);
        Assert.assertEquals(1096, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(10960.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(10.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);

        /* #TODO getAverage is NaN when there is no transaction over a given period

        check weeklyCalculation with 0 value
        firstDay = new Date(946684800000L); // Sat Jan 01 2000 01:00:00 GMT+0100
        lastDay = new Date(946771200000L); // Sun Jan 02 2000 01:00:00 GMT+0100

        setUpTransactionBean(3, null, monday, null, null, null);
        Assert.assertNotNull(transactions);

        analysisBean = AnalysisCalculation.createAnalysisBean(firstDay, lastDay, transactions);
        Assert.assertEquals(0, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
        Assert.assertEquals(0.0, analysisBean.getTotal().getIncome().getAverage(), 0.0);*/
    }

    @Test
    public void monthlyCalculationTest() {
        Integer monthlyDay = 15;
        setUpTransactionBean(4, null, null, monthlyDay, null, null);
        Assert.assertNotNull(transactions);

        AnalysisBean analysisBean =
                AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

        Assert.assertEquals(3, analysisBean.getTotal().getIncome().getCount());
        Assert.assertEquals(30.0, analysisBean.getTotal().getIncome().getSum(), 0.0);
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
