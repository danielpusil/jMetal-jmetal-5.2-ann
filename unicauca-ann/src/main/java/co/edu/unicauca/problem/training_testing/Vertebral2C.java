package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Vertebral2C extends TrainingTestingEvaluator {

    public Vertebral2C() throws IOException {
        super(20, new DataSet("src/resources-elm", "vertebral(2c).train", 6), new DataSet("src/resources-elm", "vertebral(2c).test", 6), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Vertebral(2C)", 3000);
    }

}