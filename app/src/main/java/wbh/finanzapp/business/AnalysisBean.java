package wbh.finanzapp.business;

import java.util.Map;

public class AnalysisBean {

    private CashFlow total;
    private Map<Integer, CashFlow> groups;

    public AnalysisBean(CashFlow total, Map<Integer, CashFlow> groups) {
        this.total = total;
        this.groups = groups;
    }

    public CashFlow getTotal() {
        return total;
    }

    public void setTotal(CashFlow total) {
        this.total = total;
    }

    public Map<Integer, CashFlow> getGroups() {
        return groups;
    }

    public void setGroups(Map<Integer, CashFlow> groups) {
        this.groups = groups;
    }

    class CashFlow {
        private Statistic income;
        private Statistic expenses;

        public CashFlow(Statistic income, Statistic expenses) {
            this.income = income;
            this.expenses = expenses;
        }

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

    class Statistic {
        private int count;
        private int sum;
        private int average;
        private int median;

        public Statistic(int count, int sum, int average, int median) {
            this.count = count;
            this.sum = sum;
            this.average = average;
            this.median = median;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public int getAverage() {
            return average;
        }

        public void setAverage(int average) {
            this.average = average;
        }

        public int getMedian() {
            return median;
        }

        public void setMedian(int median) {
            this.median = median;
        }
    }
}
