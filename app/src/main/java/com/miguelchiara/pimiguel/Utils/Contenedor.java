package com.miguelchiara.pimiguel.Utils;

public class Contenedor
{

    private boolean contenedorlleno = Boolean.FALSE;

    /**
     * Obtiene de forma concurrente o síncrona el elemento que hay en el contenedor
     * @return Contenido el contenedor
     */
    public synchronized void get()
    {
        while (!contenedorlleno)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                System.err.println("Contenedor: Error en get -> " + e.getMessage());
            }
        }
        contenedorlleno = Boolean.FALSE;
        notifyAll();
    }

    /**
     * Introduce de forma concurrente o síncrona un elemento en el contenedor
     * @param value Elemento a introducir en el contenedor
     */
    public synchronized void put()
    {
        while (contenedorlleno)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                System.err.println("Contenedor: Error en put -> " + e.getMessage());
            }
        }
        contenedorlleno = Boolean.TRUE;
        notifyAll();
    }
}