package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Zoo extends TrainingTestingEvaluator {

    public Zoo() throws IOException {
        super(20, new DataSet("src/resources-elm", "zoo.train", 16), new DataSet("src/resources-elm", "zoo.test", 16), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Zoo", 3000);
    }

}