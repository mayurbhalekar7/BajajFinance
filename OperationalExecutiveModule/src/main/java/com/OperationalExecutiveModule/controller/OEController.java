package com.OperationalExecutiveModule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.OperationalExecutiveModule.serviceI.OEServiceI;

@RestController
public class OEController {

	@Autowired
	OEServiceI oei;
	
}
