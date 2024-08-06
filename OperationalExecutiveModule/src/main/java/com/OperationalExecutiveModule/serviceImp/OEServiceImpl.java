package com.OperationalExecutiveModule.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.OperationalExecutiveModule.repository.OERepository;
import com.OperationalExecutiveModule.serviceI.OEServiceI;

@Service
public class OEServiceImpl implements OEServiceI {

	@Autowired
	OERepository or;
	
	
}
