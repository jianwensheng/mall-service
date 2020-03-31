package com.oruit.share.util;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;

public class TransactionValue {

    private DataSourceTransactionManager transactionManager;
    private TransactionStatus status;
    
    public TransactionValue() {
        super();
    }
    
    public TransactionValue(DataSourceTransactionManager transactionManager, TransactionStatus status) {
        super();
        this.transactionManager = transactionManager;
        this.status = status;
    }
    public DataSourceTransactionManager getTransactionManager() {
        return transactionManager;
    }
    public TransactionStatus getStatus() {
        return status;
    }

}
