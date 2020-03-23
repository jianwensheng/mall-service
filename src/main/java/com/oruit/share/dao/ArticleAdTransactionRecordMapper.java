package com.oruit.share.dao;


import com.oruit.share.domain.ArticleAdTransactionRecord;

public interface ArticleAdTransactionRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ArticleAdTransactionRecord record);

    int insertSelective(ArticleAdTransactionRecord record);

    ArticleAdTransactionRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArticleAdTransactionRecord record);

    int updateByPrimaryKey(ArticleAdTransactionRecord record);
}