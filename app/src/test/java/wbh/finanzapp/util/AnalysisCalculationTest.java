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
    private static Date startDate = new Date(1559347200000L); // 1.6.19.
    private static Date endDate = new Date(1567209600000L); // 31.8.19.

    @Test
    public void uniqueCalculationTest() {
        Long uniqueDate = 1562198400000L; // 04.07.2019.
        setUpTransactionBean(1, uniqueDate, null, null, null, null);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getCount(), 1);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getSum(), 10.0, 0.0);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getAverage(), 10.0, 0.0);
    }

    @Test
    public void dailyCalculationTest() {
        setUpTransactionBean(2, null, null, null, null, null);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getCount(), 92);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getSum(), 920.0, 0.0);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getAverage(), 10.0, 0.0);
    }

    @Test
    public void weeklyCalculationTest() {
        Integer dayOfWeek = 4; // Wednesday.
        setUpTransactionBean(3, null, dayOfWeek, null, null, null);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getCount(), 13);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getSum(), 130.0, 0.0);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getAverage(), 10.0, 0.0);
    }

    @Test
    public void monthlyCalculationTest() {
        Integer monthlyDay = 15;
        setUpTransactionBean(4, null, null, monthlyDay, null, null);
        Assert.assertNotNull(transactions);

        AnalysisBean analysisBean =
                AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);

        Assert.assertEquals(analysisBean.getTotal().getIncome().getCount(), 3);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getSum(), 30.0, 0.0);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getAverage(), 10.0, 0.0);
    }

    @Test
    public void yearlyCalculationTest() {
        Integer yearlyMonth = 7;
        Integer yearlyDay = 15;
        setUpTransactionBean(5, null, null, null, yearlyMonth, yearlyDay);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getCount(), 1);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getSum(), 10.0, 0.0);
        Assert.assertEquals(analysisBean.getTotal().getIncome().getAverage(), 10.0, 0.0);
    }

    private void setUpTransactionBean(int state, Long uniqueDate, Integer dayOfWeek, Integer monthlyDay, Integer yearlyMonth, Integer yearlyDay) {
        transactions = new ArrayList<>();
        TransactionBean transactionBean = new TransactionBean(10, "testName", "testDescription", 5, 20, 10.0,
                state, uniqueDate, dayOfWeek, monthlyDay, yearlyMonth, yearlyDay);
        transactions.add(transactionBean);
    }
}
