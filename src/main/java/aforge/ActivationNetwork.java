package aforge;

public class ActivationNetwork extends Network {
        /// <summary>
        /// Initializes a new instance of the <see cref="ActivationNetwork"/> class.
        /// </summary>
        ///
        /// <param name="function">Activation function of neurons of the network.</param>
        /// <param name="inputsCount">Network's inputs count.</param>
        /// <param name="neuronsCount">Array, which specifies the amount of neurons in
        /// each layer of the neural network.</param>
        ///
        /// <remarks>The new network is randomized (see <see cref="ActivationNeuron.Randomize"/>
        /// method) after it is created.</remarks>
        ///
        public ActivationNetwork( IActivationFunction function, int inputsCount, int... neuronsCount )
        {
            super( inputsCount, neuronsCount.length );
            // create each layer
            for ( int i = 0; i < layers.length; i++ )
            {
                layers[i] = new ActivationLayer(
                        // neurons count in the layer
                        neuronsCount[i],
                        // inputs count of the layer
                        ( i == 0 ) ? inputsCount : neuronsCount[i - 1],
                        // activation function of the layer
                        function );
            }
        }

        /// <summary>
        /// Set new activation function for all neurons of the network.
        /// </summary>
        ///
        /// <param name="function">Activation function to set.</param>
        ///
        /// <remarks><para>The method sets new activation function for all neurons by calling
        /// <see cref="ActivationLayer.SetActivationFunction"/> method for each layer of the network.</para></remarks>
        ///
        public void SetActivationFunction( IActivationFunction function )
        {
            for ( int i = 0; i < layers.length; i++ )
            {
                ( (ActivationLayer) layers[i] ).SetActivationFunction( function );
            }
        }
    }
