package wbh.finanzapp.business;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represent an Analysis which contain the information of all transaction
 * within an start and end date.
 */
public class AnalysisBean {

    private CashFlow total = new CashFlow();

    private List<String> transactionNames = new ArrayList<>();

    private List<Long> groupIds = new ArrayList<>();

    @SuppressLint("UseSparseArrays")
    private Map<Long, CashFlow> groups = new HashMap<>();

    public CashFlow getTotal() {
        return total;
    }

    void setTotal(CashFlow total) {
        this.total = total;
    }

    public Map<Long, CashFlow> getGroups() {
        return groups;
    }

    public void setGroups(Map<Long, CashFlow> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "CashFlow: " + total;
    }

    public List<String> getTransactionNames() {
        return transactionNames;
    }

    public List<Long> getGroupIds() {
        return groupIds;
    }

    /**
     * The CashFlow class contains the statistics objects for the income and expenses.
     */
    public static class CashFlow {

        private Statistic income = new Statistic();
        private Statistic expenses = new Statistic();

        public Statistic getIncome() {
            return income;
        }

        void setIncome(Statistic income) {
            this.income = income;
        }

        public Statistic getExpenses() {
            return expenses;
        }

        public void setExpenses(Statistic expenses) {
            this.expenses = expenses;
        }

        @Override
        public String toString() {
            return "Income: " + income + "Expenses: " + expenses;
        }
    }

    /**
     * Statistic class contains the sum over all transactions.
     */
    public static class Statistic {

        private int count = 0;
        private double sum = 0;

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

        @Override
        public String toString() {
            return "Count: " + count + " Sum: " + sum;
        }
    }
}
