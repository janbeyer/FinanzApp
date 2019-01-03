package wbh.finanzapp.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AnalysisBeanTest {

    int count = 3;
    double sum_income = 3000;
    AnalysisBean testAnalysisBean = new AnalysisBean(null, null);
    AnalysisBean.CashFlow testCashFlow = new AnalysisBean.CashFlow();
    AnalysisBean.Statistic testStatistic = new AnalysisBean.Statistic();
    Map<Long, AnalysisBean.CashFlow> testGroups = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        testStatistic.setSum(sum_income);
        testStatistic.setCount(count);
        testCashFlow.setIncome(testStatistic);
        testAnalysisBean.setTotal(testCashFlow);
        testAnalysisBean.setGroups(testGroups);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getTotal() {
        assertEquals(testAnalysisBean.getTotal(), testAnalysisBean.getTotal());
    }

    @Test
    public void getGroups() {
        assertEquals(testAnalysisBean.getGroups(), testAnalysisBean.getGroups());
    }

}