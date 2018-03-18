/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.unicauca.problem.cross_validation;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import co.edu.unicauca.problem.AbstractELMEvaluator;
import java.io.IOException;

/**
 *
 * @author Daniel
 */
public class AutoPrice extends AbstractCrossValidationEvaluator {

    public AutoPrice() throws IOException {
        super(AbstractELMEvaluator.EvaluatorType.CV, "AutoPrice", new DataSet("src/resources-elm", "autoPrice.train", 15), new DataSet("src/resources-elm", "autoPrice.test", 15), 5, 50, new Sigmoid(), new RidgeRegressionTheory(new double[]{0,0000001}), 3000);
    }

}