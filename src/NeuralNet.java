import java.util.ArrayList;
import java.util.List;

public class NeuralNet
{
	int NUM_INTERNAL_LAYERS;
	int INDEX_OF_MAX_OUTPUT;
	int NEURONS_PER_INTERNAL_LAYER = 5;

	List<Double> INPUT_VECTOR;
	List<Neuron> INPUT_LAYER_NEURONS;
	List<Neuron> INTERNAL_NEURONS;

	NeuralLayer INPUT_LAYER;
	List<NeuralLayer> INTERNAL_LAYERS = new ArrayList<>(NUM_INTERNAL_LAYERS);

	public NeuralNet(int NUM_INTERNAL_LAYERS,
	                 List<Double> INPUT_VECTOR,
	                 List<Neuron> INPUT_LAYER,
	                 List<Neuron> INTERNAL_NEURONS)
	{
		this.NUM_INTERNAL_LAYERS = NUM_INTERNAL_LAYERS;
		this.INPUT_VECTOR = INPUT_VECTOR;
		this.INPUT_LAYER_NEURONS = INPUT_LAYER;
		this.INTERNAL_NEURONS = INTERNAL_NEURONS;
	}

	public void CreateNeuralNetLayer()
	{
		CreateInputLayer();
		CreateInternalNeuralLayers();
	}

	private void CreateInputLayer()
	{
		this.INPUT_LAYER = new NeuralLayer(0, INPUT_LAYER_NEURONS);
	}

	private void CreateInternalNeuralLayers()
	{
		int layerLevel = 0;
		for (int index = 0; index < INTERNAL_NEURONS.size(); index += NEURONS_PER_INTERNAL_LAYER) {
			INTERNAL_LAYERS.add(new NeuralLayer(++layerLevel, INTERNAL_NEURONS.subList(index, index + NEURONS_PER_INTERNAL_LAYER)));
		}
	}


	public void TestRun()
	{
		this.INPUT_LAYER.INPUT_VALUES = this.INPUT_VECTOR;
		INPUT_LAYER.run();
	}

/* 	int NUM_LAYERS;
	int INDEX_OF_MAX_OUTPUT;
	private CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

	List<Integer> inputVector = Arrays.asList(9, 17, 21, 44);
	NeuralLayer InputLayer = new NeuralLayer(4, inputVector, 1, false);

	NeuralLayer InternalLayer1 = new NeuralLayer(5, InputLayer.OUTPUT_LAYER, 2);
	NeuralLayer InternalLayer2 = new NeuralLayer(5, InternalLayer1.OUTPUT_LAYER, 3);
	NeuralLayer InternalLayer3 = new NeuralLayer(5, InternalLayer2.OUTPUT_LAYER, 4);

	NeuralLayer OutputLayer = new NeuralLayer(3, InternalLayer3.OUTPUT_LAYER, 5);
*/


}
