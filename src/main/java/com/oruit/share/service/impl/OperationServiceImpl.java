package com.oruit.share.service.impl;

import com.oruit.share.dao.OperationMapper;
import com.oruit.share.domain.Operation;
import com.oruit.share.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OperationServiceImpl implements OperationService {

	@Autowired
	private OperationMapper operationMapper;
	
	@Override
	public int userOperation(Operation operation) {
		int rows = operationMapper.insertSelective(operation);
		return rows;
	}

}
