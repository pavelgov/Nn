package aforge;

public class ActivationNeuron extends Neuron {
    /// <summary>
    /// Threshold value.
    /// </summary>
    ///
    /// <remarks>The value is added to inputs weighted sum before it is passed to activation
    /// function.</remarks>
    ///
    protected double threshold = 0.0;

    /// <summary>
    /// Activation function.
    /// </summary>
    ///
    /// <remarks>The function is applied to inputs weighted sum plus
    /// threshold value.</remarks>
    ///
    protected IActivationFunction function = null;

    /// <summary>
    /// Threshold value.
    /// </summary>
    ///
    /// <remarks>The value is added to inputs weighted sum before it is passed to activation
    /// function.</remarks>
    ///


    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }


    /// <summary>
    /// Neuron's activation function.
    /// </summary>
    ///

    public IActivationFunction getFunction() {
        return function;
    }

    public void setFunction(IActivationFunction function) {
        this.function = function;
    }


    /// <summary>
    /// Initializes a new instance of the <see cref="ActivationNeuron"/> class.
    /// </summary>
    ///
    /// <param name="inputs">Neuron's inputs count.</param>
    /// <param name="function">Neuron's activation function.</param>
    ///
    public ActivationNeuron(int inputs, IActivationFunction function) {
        super(inputs);
        this.function = function;
    }

    /// <summary>
    /// Randomize neuron.
    /// </summary>
    ///
    /// <remarks>Calls base class <see cref="Neuron.Randomize">Randomize</see> method
    /// to randomize neuron's weights and then randomizes threshold's value.</remarks>
    ///
    @Override
    public void randomize() {
        // randomize weights
        super.randomize();
        // randomize threshold
        threshold = rand.nextDouble() * (randRange.length()) + randRange.getMin();
    }

    /// <summary>
    /// Computes output value of neuron.
    /// </summary>
    ///
    /// <param name="input">Input vector.</param>
    ///
    /// <returns>Returns neuron's output value.</returns>
    ///
    /// <remarks><para>The output value of activation neuron is equal to value
    /// of nueron's activation function, which parameter is weighted sum
    /// of its inputs plus threshold value. The output value is also stored
    /// in <see cref="Neuron.Output">Output</see> property.</para>
    ///
    /// <para><note>The method may be called safely from multiple threads to compute neuron's
    /// output value for the specified input values. However, the value of
    /// <see cref="Neuron.Output"/> property in multi-threaded environment is not predictable,
    /// since it may hold neuron's output computed from any of the caller threads. Multi-threaded
    /// access to the method is useful in those cases when it is required to improve performance
    /// by utilizing several threads and the computation is based on the immediate return value
    /// of the method, but not on neuron's output property.</note></para>
    /// </remarks>
    ///
    /// <exception cref="ArgumentException">Wrong length of the input vector, which is not
    /// equal to the <see cref="Neuron.InputsCount">expected value</see>.</exception>
    ///
    @Override
    public double compute(double[] input) {
        // check for corrent input vector
        if (input.length != inputsCount)

            System.err.println("Wrong length of the input vector.");


        // initial sum value
        double sum = 0.0;

        // compute weighted sum of inputs
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * input[i];
        }
        sum += threshold;

        // local variable to avoid mutlithreaded conflicts
        double output = function.function(sum);
        // assign output property as well (works correctly for single threaded usage)
        this.output = output;

        return output;
    }
}



