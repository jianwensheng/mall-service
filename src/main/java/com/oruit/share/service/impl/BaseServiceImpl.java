package com.oruit.share.service.impl;

import com.oruit.share.dao.BaseMapper;
import com.oruit.share.service.BaseService;

import java.io.Serializable;

public abstract class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {

	protected BaseMapper<T, PK> baseMapper;

	@Override
	public int deleteByPrimaryKey(PK id) {
		return baseMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(T record) {
		return baseMapper.insert(record);
	}

	@Override
	public int insertSelective(T record) {
		return baseMapper.insertSelective(record);
	}

	@Override
	public T selectByPrimaryKey(PK id) {
		return baseMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(T record) {
		return baseMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(T record) {
		return baseMapper.updateByPrimaryKey(record);
	}

	public BaseMapper<T, PK> getBaseMapper() {
		return baseMapper;
	}

	public void setBaseMapper(BaseMapper<T, PK> baseMapper) {
		this.baseMapper = baseMapper;
	}
	
	public abstract void setBaseMapper();
	

}
