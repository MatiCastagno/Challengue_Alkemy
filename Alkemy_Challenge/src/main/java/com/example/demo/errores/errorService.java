package com.example.demo.errores;

@SuppressWarnings("serial")
public class errorService extends Exception {

	public errorService(String msn) {
		super(msn);
	}
}
