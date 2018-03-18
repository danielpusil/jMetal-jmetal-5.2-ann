package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Soybean extends TrainingTestingEvaluator {

    public Soybean() throws IOException {
        super(20, new DataSet("src/resources-elm", "soybean.train", 35), new DataSet("src/resources-elm", "soybean.test", 35), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Soybean", 3000);
    }

}
