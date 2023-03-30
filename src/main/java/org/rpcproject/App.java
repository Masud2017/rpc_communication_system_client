package org.rpcproject;

import org.checkerframework.checker.units.qual.C;
import org.rpcproject.service.Client;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        Client client = new Client("localhost",4444);
        client.start();
    }
}
