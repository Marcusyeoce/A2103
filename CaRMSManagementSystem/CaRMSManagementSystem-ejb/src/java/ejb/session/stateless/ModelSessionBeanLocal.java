/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import Entity.ModelEntity;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface ModelSessionBeanLocal {

    public ModelEntity retrieveModelEntityByModelAndMake(String model, String make);
    
}