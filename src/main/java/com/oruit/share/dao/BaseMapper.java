package com.oruit.share.dao;

import java.io.Serializable;

public interface BaseMapper<T, PK extends Serializable> {
	int deleteByPrimaryKey(PK id);

	int insert(T record);

	int insertSelective(T record);

	T selectByPrimaryKey(PK id);

	int updateByPrimaryKeySelective(T record);

	int updateByPrimaryKey(T record);
}
