package com.minkyo.bookManagementServer;

import SockNet.NetServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        NetServer s = new NetServer(9999);
        
        try {
        	s.startServer();
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        
        while(true) {
        	Thread.currentThread().sleep(10000000);
        }
        
        
    }
}
