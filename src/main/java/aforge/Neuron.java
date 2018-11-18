package aforge;

public abstract class Neuron {
    /// <summary>
    /// Neuron's inputs count.
    /// </summary>
    protected int inputsCount = 0;

    /// <summary>
    /// Nouron's wieghts.
    /// </summary>
    protected double[] weights = null;

    /// <summary>
    /// Neuron's output value.
    /// </summary>
    protected double output = 0;

    /// <summary>
    /// Random number generator.
    /// </summary>
    ///
    /// <remarks>The generator is used for neuron's weights randomization.</remarks>
    ///
    protected static ThreadSafeRandom rand = new ThreadSafeRandom();

    /// <summary>
    /// Random generator range.
    /// </summary>
    ///
    /// <remarks>Sets the range of random generator. Affects initial values of neuron's weight.
    /// Default value is [0, 1].</remarks>
    ///
    protected static Range randRange = new Range(0.0f, 1.0f);

    /// <summary>
    /// Random number generator.
    /// </summary>
    ///
    /// <remarks>The property allows to initialize random generator with a custom seed. The generator is
    /// used for neuron's weights randomization.</remarks>
    ///

    public static ThreadSafeRandom getRand() {
        return rand;
    }

    public static void setRand(ThreadSafeRandom rand) {
        if ( rand != null )

        Neuron.rand = rand;
    }


    /// <summary>
    /// Random generator range.
    /// </summary>
    ///
    /// <remarks>Sets the range of random generator. Affects initial values of neuron's weight.
    /// Default value is [0, 1].</remarks>
    ///

    public static Range getRandRange() {
        return randRange;
    }

    public static void setRandRange(Range randRange) {
        Neuron.randRange = randRange;
    }


    /// <summary>
    /// Neuron's inputs count.
    /// </summary>

    public int getInputsCount() {
        return inputsCount;
    }

    /// <summary>
    /// Neuron's output value.
    /// </summary>
    ///
    /// <remarks>The calculation way of neuron's output value is determined by inherited class.</remarks>
    ///

    public double getOutput() {
        return output;
    }

    /// <summary>
    /// Neuron's weights.
    /// </summary>

    public double[] getWeights() {
        return weights;
    }

    /// <summary>
    /// Initializes a new instance of the <see cref="Neuron"/> class.
    /// </summary>
    ///
    /// <param name="inputs">Neuron's inputs count.</param>
    ///
    /// <remarks>The new neuron will be randomized (see <see cref="Randomize"/> method)
    /// after it is created.</remarks>
    ///
    protected Neuron(int inputs) {
        // allocate weights
        inputsCount = Math.max(1, inputs);
        weights = new double[inputsCount];
        // randomize the neuron
        randomize();
    }

    /// <summary>
    /// Randomize neuron.
    /// </summary>
    ///
    /// <remarks>Initialize neuron's weights with random values within the range specified
    /// by <see cref="RandRange"/>.</remarks>
    ///
    public void randomize() {
        double d = randRange.length();

        // randomize weights
        for (int i = 0; i < inputsCount; i++)
            weights[i] = rand.nextDouble() * d + randRange.getMin();
    }

    /// <summary>
    /// Computes output value of neuron.
    /// </summary>
    ///
    /// <param name="input">Input vector.</param>
    ///
    /// <returns>Returns neuron's output value.</returns>
    ///
    /// <remarks>The actual neuron's output value is determined by inherited class.
    /// The output value is also stored in <see cref="Output"/> property.</remarks>
    ///
    public abstract double compute(double[] input);
}
