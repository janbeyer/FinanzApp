package wbh.finanzapp.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wbh.finanzapp.business.AbstractBean;
import wbh.finanzapp.business.AnalysisBean;
import wbh.finanzapp.business.TransactionBean;

public class AnalysisCalculationTest {

    private static List<AbstractBean> transactions;
    private static Date startDate = new Date(1559347200000L);
    private static Date endDate = new Date(1567209600000L);

    @Test
    public void uniqueCalculationTest() {
        Long uniqueDate = 61520342400000L; // 04.07.2019.
        setUpTransactionBean(1, uniqueDate, null, null, null, null);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(analysisBean.getTotal().getIncome(), 10.0);
    }

    @Test
    public void dailyCalculationTest() {
        setUpTransactionBean(2, null, null, null, null, null);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(analysisBean.getTotal().getIncome(), 920.0);
    }

    @Test
    public void weeklyCalculationTest() {
        Integer dayOfWeek = 4; // Wednesday.
        setUpTransactionBean(3, null, dayOfWeek, null, null, null);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(analysisBean.getTotal().getIncome(), 130.0);
    }

    @Test
    public void monthlyCalculationTest() {
        Integer monthlyDay = 15;
        setUpTransactionBean(4, null, null, monthlyDay, null, null);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(analysisBean.getTotal().getIncome(), 30.0);
    }

    @Test
    public void yearlyCalculationTest() {
        Integer yearlyMonth = 7;
        Integer yearlyDay = 15;
        setUpTransactionBean(4, null, null, null, yearlyMonth, yearlyDay);
        Assert.assertNotNull(transactions);
        AnalysisBean analysisBean = AnalysisCalculation.createAnalysisBean(startDate, endDate, transactions);
        Assert.assertEquals(analysisBean.getTotal().getIncome(), 10.0);
    }

    private void setUpTransactionBean(int state, Long uniqueDate, Integer dayOfWeek, Integer monthlyDay, Integer yearlyMonth, Integer yearlyDay) {
        transactions = new ArrayList<>();
        TransactionBean transactionBean = new TransactionBean(10, "testName", "testDescription", 5, 20, 10.0,
                1, uniqueDate, dayOfWeek, monthlyDay, yearlyMonth, yearlyDay);
        transactions.add(transactionBean);
    }
}
