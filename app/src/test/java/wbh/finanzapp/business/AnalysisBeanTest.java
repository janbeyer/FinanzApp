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


    AnalysisBean TestAnalysisBean = new AnalysisBean();
    AnalysisBean.CashFlow TestCashFlow = new AnalysisBean.CashFlow();
    AnalysisBean.Statistic TestStatistic = new AnalysisBean.Statistic();
    Map<Long, AnalysisBean.CashFlow> TestGroups = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        TestStatistic.setSum(sum_income);
        TestStatistic.setCount(count);
        TestCashFlow.setIncome(TestStatistic);
        TestAnalysisBean.setTotal(TestCashFlow);
        TestAnalysisBean.setGroups(TestGroups);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getTotal() {
        assertEquals(TestAnalysisBean.getTotal(), TestAnalysisBean.getTotal());
    }

    @Test
    public void getGroups() {
        assertEquals(TestAnalysisBean.getGroups(), TestAnalysisBean.getGroups());
    }

}