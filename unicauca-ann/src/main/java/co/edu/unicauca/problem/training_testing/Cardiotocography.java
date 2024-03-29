package co.edu.unicauca.problem.training_testing;

import co.edu.unicauca.dataset.DataSet;
import co.edu.unicauca.elm_function.impl.Sigmoid;
import co.edu.unicauca.moore_penrose.impl.RidgeRegressionTheory;
import java.io.IOException;

public class Cardiotocography extends TrainingTestingEvaluator {

    public Cardiotocography() throws IOException {
        super(20, new DataSet("src/resources-elm", "cardiotocography.train", 21), new DataSet("src/resources-elm", "cardiotocography.test", 21), new Sigmoid(), new RidgeRegressionTheory(new double[]{0,000000001}), "Cardiotocography", 3000);
    }
}
