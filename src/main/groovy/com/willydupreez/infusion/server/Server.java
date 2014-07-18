package com.willydupreez.infusion.server;

public interface Server {

	void init(ServerContext context);
	void start();
	void stop();

}
