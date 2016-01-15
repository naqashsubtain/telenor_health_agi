package com.health.common;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.asteriskjava.fastagi.AgiServerThread;
import org.asteriskjava.fastagi.DefaultAgiServer;

public class MainClass {

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		AgiServerThread agiServerThread = new AgiServerThread();
		agiServerThread.setAgiServer(new DefaultAgiServer());
		agiServerThread.setDaemon(false);
		agiServerThread.startup();

	}
}
