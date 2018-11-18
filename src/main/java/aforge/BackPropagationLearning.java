package aforge;

/// <summary>
/// Back propagation learning algorithm.
/// </summary>
///
/// <remarks><para>The class implements back propagation learning algorithm,
/// which is widely used for training multi-layer neural networks with
/// continuous activation functions.</para>
///
/// <para>Sample usage (training network to calculate XOR function):</para>
/// <code>
/// // initialize input and output values
/// double[][] input = new double[4][] {
///     new double[] {0, 0}, new double[] {0, 1},
///     new double[] {1, 0}, new double[] {1, 1}
/// };
/// double[][] output = new double[4][] {
///     new double[] {0}, new double[] {1},
///     new double[] {1}, new double[] {0}
/// };
/// // create neural network
/// ActivationNetwork   network = new ActivationNetwork(
///     SigmoidFunction( 2 ),
///     2, // two inputs in the network
///     2, // two neurons in the first layer
///     1 ); // one neuron in the second layer
/// // create teacher
/// BackPropagationLearning teacher = new BackPropagationLearning( network );
/// // loop
/// while ( !needToStop )
/// {
///     // run epoch of learning procedure
///     double error = teacher.RunEpoch( input, output );
///     // check error value to see if we need to stop
///     // ...
/// }
/// </code>
/// </remarks>
///
/// <seealso cref="EvolutionaryLearning"/>
///
public class BackPropagationLearning implements ISupervisedLearning {

    // network to teach
    private ActivationNetwork network;

    // momentum
    private double momentum = 0.0;

    // neuron's errors
    private double[][] neuronErrors = null;
    // weight's updates
    private double[][][] weightsUpdates = null;
    // threshold's updates
    private double[][] thresholdsUpdates = null;

    /// <summary>
    /// Learning rate, [0, 1].
    /// </summary>
    ///
    /// <remarks><para>The value determines speed of learning.</para>
    ///
    /// <para>Default value equals to <b>0.1</b>.</para>
    /// </remarks>
    ///

    // learning rate
    private double learningRate = 0.1;


    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = Math.max(0.0, Math.min(1.0, learningRate));
    }


    /// <summary>
    /// Momentum, [0, 1].
    /// </summary>
    ///
    /// <remarks><para>The value determines the portion of previous weight's update
    /// to use on current iteration. Weight's update values are calculated on
    /// each iteration depending on neuron's error. The momentum specifies the amount
    /// of update to use from previous iteration and the amount of update
    /// to use from current iteration. If the value is equal to 0.1, for example,
    /// then 0.1 portion of previous update and 0.9 portion of current update are used
    /// to update weight's value.</para>
    ///
    /// <para>Default value equals to <b>0.0</b>.</para>
    ///  </remarks>
    ///

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = Math.max(0.0, Math.min(1.0, momentum));
    }


    /// <summary>
    /// Initializes a new instance of the <see cref="BackPropagationLearning"/> class.
    /// </summary>
    ///
    /// <param name="network">Network to teach.</param>
    ///
    public BackPropagationLearning(ActivationNetwork network) {
        this.network = network;

        // create error and deltas arrays
        neuronErrors = new double[network.layers.length][];
        weightsUpdates = new double[network.layers.length][][];
        thresholdsUpdates = new double[network.layers.length][];

        // initialize errors and deltas arrays for each layer
        for (int i = 0; i < network.layers.length; i++) {
            Layer layer = network.layers[i];

            neuronErrors[i] = new double[layer.getNeurons().length];
            weightsUpdates[i] = new double[layer.getNeurons().length][];
            thresholdsUpdates[i] = new double[layer.getNeurons().length];

            // for each neuron
            for (int j = 0; j < weightsUpdates[i].length; j++) {
                weightsUpdates[i][j] = new double[layer.getInputsCount()];
            }
        }
    }

    /// <summary>
    /// Runs learning iteration.
    /// </summary>
    ///
    /// <param name="input">Input vector.</param>
    /// <param name="output">Desired output vector.</param>
    ///
    /// <returns>Returns squared error (difference between current network's output and
    /// desired output) divided by 2.</returns>
    ///
    /// <remarks><para>Runs one learning iteration and updates neuron's
    /// weights.</para></remarks>
    ///
    public double run(double[] input, double[] output) {
        // compute the network's output
        network.Compute(input);

        // calculate network error
        double error = CalculateError(output);

        // calculate weights updates
        CalculateUpdates(input);

        // update the network
        UpdateNetwork();

        return error;
    }

    /// <summary>
    /// Runs learning epoch.
    /// </summary>
    ///
    /// <param name="input">Array of input vectors.</param>
    /// <param name="output">Array of output vectors.</param>
    ///
    /// <returns>Returns summary learning error for the epoch. See <see cref="Run"/>
    /// method for details about learning error calculation.</returns>
    ///
    /// <remarks><para>The method runs one learning epoch, by calling <see cref="Run"/> method
    /// for each vector provided in the <paramref name="input"/> array.</para></remarks>
    ///
    public double runEpoch(double[][] input, double[][] output) {
        double error = 0.0;

        // run learning procedure for all samples
        for (int i = 0; i < input.length; i++) {
            error += run(input[i], output[i]);
        }

        // return summary error
        return error;
    }


    /// <summary>
    /// Calculates error values for all neurons of the network.
    /// </summary>
    ///
    /// <param name="desiredOutput">Desired output vector.</param>
    ///
    /// <returns>Returns summary squared error of the last layer divided by 2.</returns>
    ///
    private double CalculateError(double[] desiredOutput) {
        // current and the next layers
        Layer layer, layerNext;
        // current and the next errors arrays
        double[] errors, errorsNext;
        // error values
        double error = 0, e, sum;
        // neuron's output value
        double output;
        // layers count
        int layersCount = network.layers.length;

        // assume, that all neurons of the network have the same activation function
        //Предпологаем, что все нейроны сети имеют одинаковую функцию активации
        IActivationFunction function =((ActivationNeuron) network.getLayers()[0].neurons[0]).getFunction();
        // calculate error values for the last layer first
        layer = network.layers[layersCount - 1];
        errors = neuronErrors[layersCount - 1];

        for (int i = 0; i < layer.getNeurons().length; i++) {
            output = layer.getNeurons()[i].output;
            // error of the neuron
            e = desiredOutput[i] - output;
            // error multiplied with activation function's derivative
            errors[i] = e * function.derivative2(output);
            // squre the error and sum it
            error += (e * e);
        }

        // calculate error values for other layers
        for (int j = layersCount - 2; j >= 0; j--) {
            layer = network.layers[j];
            layerNext = network.layers[j + 1];
            errors = neuronErrors[j];
            errorsNext = neuronErrors[j + 1];

            // for all neurons of the layer
            for (int i = 0; i < layer.getNeurons().length; i++) {
                sum = 0.0;
                // for all neurons of the next layer
                for (int k = 0; k < layerNext.getNeurons().length; k++) {
                    sum += errorsNext[k] * layerNext.getNeurons()[k].getWeights()[i];
                }
                errors[i] = sum * function.derivative2(layer.getNeurons()[i].output);
            }
        }

        // return squared error of the last layer divided by 2
        return error / 2.0;
    }

    /// <summary>
    /// Calculate weights updates.
    /// </summary>
    ///
    /// <param name="input">Network's input vector.</param>
    ///
    private void CalculateUpdates(double[] input) {
        // current neuron
        Neuron neuron;
        // current and previous layers
        Layer layer, layerPrev;
        // layer's weights updates
        double[][] layerWeightsUpdates;
        // layer's thresholds updates
        double[] layerThresholdUpdates;
        // layer's error
        double[] errors;
        // neuron's weights updates
        double[] neuronWeightUpdates;
        // error value
        // double             error;

        // 1 - calculate updates for the first layer
        layer = network.layers[0];
        errors = neuronErrors[0];
        layerWeightsUpdates = weightsUpdates[0];
        layerThresholdUpdates = thresholdsUpdates[0];

        // cache for frequently used values
        double cachedMomentum = getLearningRate() * getMomentum();
        double cached1mMomentum = getLearningRate() * (1 - getMomentum());
        double cachedError;

        // for each neuron of the layer
        for (int i = 0; i < layer.getNeurons().length; i++) {
            neuron = layer.getNeurons()[i];
            cachedError = errors[i] * cached1mMomentum;
            neuronWeightUpdates = layerWeightsUpdates[i];

            // for each weight of the neuron
            for (int j = 0; j < neuronWeightUpdates.length; j++) {
                // calculate weight update
                neuronWeightUpdates[j] = cachedMomentum * neuronWeightUpdates[j] + cachedError * input[j];
            }

            // calculate treshold update
            layerThresholdUpdates[i] = cachedMomentum * layerThresholdUpdates[i] + cachedError;
        }

        // 2 - for all other layers
        for (int k = 1; k < network.layers.length; k++) {
            layerPrev = network.layers[k - 1];
            layer = network.layers[k];
            errors = neuronErrors[k];
            layerWeightsUpdates = weightsUpdates[k];
            layerThresholdUpdates = thresholdsUpdates[k];

            // for each neuron of the layer
            for (int i = 0; i < layer.getNeurons().length; i++) {
                neuron = layer.getNeurons()[i];
                cachedError = errors[i] * cached1mMomentum;
                neuronWeightUpdates = layerWeightsUpdates[i];

                // for each synapse of the neuron
                for (int j = 0; j < neuronWeightUpdates.length; j++) {
                    // calculate weight update
                    neuronWeightUpdates[j] = cachedMomentum * neuronWeightUpdates[j] + cachedError * layerPrev.getNeurons()[j].output;
                }

                // calculate treshold update
                layerThresholdUpdates[i] = cachedMomentum * layerThresholdUpdates[i] + cachedError;
            }
        }
    }

    /// <summary>
    /// Update network'sweights.
    /// </summary>
    ///
    private void UpdateNetwork() {
        // current neuron
        ActivationNeuron neuron;
        // current layer
        Layer layer;
        // layer's weights updates
        double[][] layerWeightsUpdates;
        // layer's thresholds updates
        double[] layerThresholdUpdates;
        // neuron's weights updates
        double[] neuronWeightUpdates;

        // for each layer of the network
        for (int i = 0; i < network.layers.length; i++) {
            layer = network.layers[i];
            layerWeightsUpdates = weightsUpdates[i];
            layerThresholdUpdates = thresholdsUpdates[i];

            // for each neuron of the layer
            for (int j = 0; j < layer.getNeurons().length; j++) {
                neuron = (ActivationNeuron) layer.getNeurons()[j] ;
                neuronWeightUpdates = layerWeightsUpdates[j];

                // for each weight of the neuron
                for (int k = 0; k < neuron.getWeights().length; k++) {
                    // update weight
                    neuron.getWeights()[k] += neuronWeightUpdates[k];
                }
                // update treshold
                neuron.threshold += layerThresholdUpdates[j];
            }
        }

    }
}
