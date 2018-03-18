package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Diabetes extends TrainingTestingEvaluator {

    public Diabetes() throws IOException {
        super(20, new DataSet("src/resources-elm", "diabetes.train", 8), new DataSet("src/resources-elm", "diabetes.test", 8), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Diabetes", 3000);
    }

}