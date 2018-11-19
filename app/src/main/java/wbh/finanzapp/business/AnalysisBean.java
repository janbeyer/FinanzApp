package wbh.finanzapp.business;

/**
 * The AnalysisBean class store every properties of a single Analysis.
 */
public class AnalysisBean extends AbstractBean {
    /**
     * Create a new AnalysisBean.
     */
    AnalysisBean(long id, String name, String description) {
        super(id, name, description);
    }
}
