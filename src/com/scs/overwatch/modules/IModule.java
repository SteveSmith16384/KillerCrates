package com.scs.overwatch.modules;


public interface IModule {//extends ActionListener {

	void init();
	
	void update(float tpf);
	
	void destroy();
	
}
