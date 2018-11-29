package wbh.finanzapp.business;

import java.util.HashMap;
import java.util.Map;

public class AnalysisBean {

    private CashFlow total = new CashFlow();
    private Map<Long, CashFlow> groups = new HashMap<>();

    public AnalysisBean() {}

    public CashFlow getTotal() {
        return total;
    }

    public void setTotal(CashFlow total) {
        this.total = total;
    }

    public Map<Long, CashFlow> getGroups() {
        return groups;
    }

    public void setGroups(Map<Long, CashFlow> groups) {
        this.groups = groups;
    }

    public static class CashFlow {
        private Statistic income = new Statistic();
        private Statistic expenses = new Statistic();

        public CashFlow() {}

        public Statistic getIncome() {
            return income;
        }

        public void setIncome(Statistic income) {
            this.income = income;
        }

        public Statistic getExpenses() {
            return expenses;
        }

        public void setExpenses(Statistic expenses) {
            this.expenses = expenses;
        }
    }

    public static class Statistic {
        private int count;
        private double sum ;

        public Statistic() {}

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getSum() {
            return sum;
        }

        public void setSum(double sum) {
            this.sum = sum;
        }

        public double getAverage() {
            return sum / count;
        }
    }
}
