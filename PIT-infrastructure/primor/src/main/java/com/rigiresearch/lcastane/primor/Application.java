package com.rigiresearch.lcastane.primor;
/**
 * Copyright 2017 University of Victoria
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * TODO
 * @date 2017-06-13
 * @version $Id$
 * @since 0.0.1
 */
public final class Application extends Thread {

    /**
     * The RMI registry.
     */
    private final Registry registry;

    /**
     * The exported manager instance.
     */
    private Manager exportedManager;

    /**
     * Default constructor.
     * @throws RemoteException In case there is a problem with the RMI registry
     */
    public Application() throws RemoteException {
        this.registry = LocateRegistry.createRegistry(1099);
    }

    /**
     * Binds Primor services through their remote interfaces.
     * @throws IOException If something goes wrong while binding the services
     * @throws AlreadyBoundException If an instance of primor is already
     *  running
     */
    private void bindServices() throws IOException, AlreadyBoundException {
        this.exportedManager = 
            (Manager) UnicastRemoteObject.exportObject(new ManagerIml(), 0);
        // Bind the remote object's stub in the registry
        this.registry.bind(
            RemoteService.MANAGER.toString(),
            exportedManager
        );
    }

    /**
     * Unbinds primor's remote services.
     * @throws NotBoundException In case the services have been already unbound
     * @throws RemoteException See {@link Registry#unbind(String)}
     * @throws AccessException See {@link Registry#unbind(String)}
     */
    private void unbindServices()
        throws AccessException, RemoteException, NotBoundException {
        this.registry.unbind(RemoteService.MANAGER.toString());
        UnicastRemoteObject.unexportObject(this.exportedManager, true);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        try {
            this.bindServices();
            System.out.println("PriMoR services have been started "
                    + "successfully");
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        Application.this.unbindServices();
                        System.out.println("PriMoR services have been stopped "
                                + "successfully");
                    } catch (RemoteException | NotBoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) throws RemoteException {
        new Application().start();
        while(true);
    }

}
