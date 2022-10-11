import java.io.IOException;

public class Main
{
	public static void main(String[] args) throws InterruptedException, IOException
	{

		// CyclicBarrierDemo demo = new CyclicBarrierDemo();
		// demo.runSimulation(5, 3);

		// List<Double> sampleWeights = Arrays.asList(-0.14751046652300515, -2.1604179228776754, 1.4275918461026382, 0.0, 5.989728207724674);
		// List<Double> inputVector = Arrays.asList(9.0, 17.0, 21.0, 44.0, 4.0);
		//
		// NeuralLayer NL = new NeuralLayer(5, inputVector, sampleWeights, 1, false);
		// NL.run();


		NeuralNetSimulator NNS = new NeuralNetSimulator();
		NNS.ListifyInputVector();
		NNS.ListifyNeurons();

		NeuralNet neuralNet = new NeuralNet(NNS.NUM_INTERNAL_LAYERS, NNS.INPUT_VECTOR, NNS.INPUT_LAYER_NEURONS, NNS.INTERNAL_LAYER_NEURONS);
		neuralNet.CreateNeuralNetLayer();

		neuralNet.TestRun();

	/* 	var neuip = NNS.getINPUT_LAYER_NEURONS();
		for (var n : neuip) {
			System.out.println(n.NEURON_INDEX);
		}


		var neuIL = NNS.getINTERNAL_LAYER_NEURONS();
		for (var n : neuIL) {
			System.out.println(n.WEIGHTED_SUM);
		}
 */
		// NNS.PrintInputLayer();
		// NNS.PrintInternalNeurons();


	}

}
