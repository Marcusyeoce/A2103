/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author user
 */
public class ModelExistException extends Exception {

    /**
     * Creates a new instance of <code>ModelExistException</code> without detail
     * message.
     */
    public ModelExistException() {
    }

    /**
     * Constructs an instance of <code>ModelExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ModelExistException(String msg) {
        super(msg);
    }
}
