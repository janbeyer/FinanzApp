package wbh.finanzapp.business;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represent an Analysis which contain the information of all transaction
 * within an start and end date.
 */
public class AnalysisBean {

    public AnalysisBean(Calendar startCalendar, Calendar endCalendar) {
        this.startCalendar = startCalendar;
        this.endCalendar = endCalendar;
    }

    /**
     * The total cash flow for a period of time.
     */
    private CashFlow total = new CashFlow();

    private Calendar startCalendar;
    private Calendar endCalendar;

    public Calendar getEndCalendar() {
        return endCalendar;
    }

    public Calendar getStartCalendar() {
        return startCalendar;
    }

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
     * Statistic class contains the sum over all transactions and the count of times the transaction was occur.
     */
    public static class Statistic {

        /**
         * The count of times the transaction was occur.
         */
        private int count = 0;
        /**
         * The sum of all transactions for a group.
         */
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
            if (count == 0) {
                return 0.0;
            }
            return sum / count;
        }

        @Override
        public String toString() {
            return "Count: " + count + " Sum: " + sum;
        }
    }
}
