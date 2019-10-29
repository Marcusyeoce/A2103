/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import Entity.CustomerEntity;
import ejb.session.stateless.CustomerSessionBeanRemote;
import java.util.Scanner;
import javax.ejb.EJB;

/**
 *
 * @author Marcus Yeo
 */
public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBean;

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Welcome To CaRMS");
        System.err.print("Enter your name: ");
        
        CustomerEntity newCustomerEntity = new CustomerEntity(sc.nextLine());
        
        customerSessionBean.registerNewCustomer(newCustomerEntity);
    }
    
}
