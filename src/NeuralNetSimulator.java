import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NeuralNetSimulator
{
	private final String WEIGHT_FILENAME = "src/weights.txt";
	private final String INPUT_VECTOR_FILENAME = "src/inputs.txt";

	private final int NUM_NEURONS_INPUT_LAYER = 4;
	private final int NUM_NEURONS_INTERNAL_LAYER = 5;
	private final int NUM_NEURONS_OUTPUT_LAYER = 3;

	int NUM_INTERNAL_LAYERS;

	NeuralNet neuralNet_;
	public List<Double> inputVector_ = new ArrayList<>();
	public List<List<Double>> edgeWeights_ = new ArrayList<>();
	List<NeuralLayer> neuralLayers_ = new ArrayList<>();

	public void CreateNeuralNet() throws FileNotFoundException
	{
		ListifyInputVector();
		ListifyWeights();
		GenerateLayers();
		neuralNet_ = new NeuralNet(NUM_INTERNAL_LAYERS, inputVector_, neuralLayers_);
	}

	private void GenerateLayers()
	{
		// reusable list for creating neural layers
		List<Neuron> _layerNeurons = new ArrayList<>();

		// generate input layer
		for (int neuronIdx = 0; neuronIdx < NUM_NEURONS_INPUT_LAYER; neuronIdx++) {
			_layerNeurons.add(new Neuron(0, neuronIdx + 1));
		}
		neuralLayers_.add(new NeuralLayer(0, NeuralLayer.LayerType.INPUT, _layerNeurons));

		// generate internal layers
		NeuralLayer _layer;
		List<List<Double>> _layerWeights;

		for (int L = 0; L < NUM_INTERNAL_LAYERS; L++) {
			_layerNeurons = new ArrayList<>();
			for (int neuronIdx = 0; neuronIdx < NUM_NEURONS_INTERNAL_LAYER; neuronIdx++) {
				_layerNeurons.add(new Neuron(L + 1, neuronIdx + 1));
			}

			// Neurons in 1st internal layer have degree-4 connectivity to input layer
			_layerWeights = new ArrayList<>();
			if (L == 0) {
				for (int i = 0; i < 4; i++) {
					_layerWeights.add(edgeWeights_.remove(0));
				}
			}
			// Neurons in subsequent internal layers have degree-5 connectivity to previous internal layer
			else {
				for (int i = 0; i < 5; i++) {
					_layerWeights.add(edgeWeights_.remove(0));
				}
			}

			_layer = new NeuralLayer(L + 1, NeuralLayer.LayerType.INTERNAL, _layerNeurons);
			_layer.setEdgeWeights(_layerWeights);
			neuralLayers_.add(_layer);
		}

		// generate output layer

		_layerNeurons = new ArrayList<>();
		for (int neuronIdx = 0; neuronIdx < NUM_NEURONS_OUTPUT_LAYER; neuronIdx++) {
			_layerNeurons.add(new Neuron(NUM_INTERNAL_LAYERS + 1, neuronIdx + 1));
		}

		// Neurons in output layer have degree-5 connectivity to last internal layer
		// Output layer edge weights will be the remaining List<List<Doubles>> in edgeWeights_
		_layerWeights = edgeWeights_;
		_layer = new NeuralLayer(NUM_INTERNAL_LAYERS + 1, NeuralLayer.LayerType.OUTPUT, _layerNeurons);
		_layer.setEdgeWeights(_layerWeights);
		neuralLayers_.add(_layer);
	}

	private void ListifyInputVector() throws FileNotFoundException
	{
		Scanner _scanner = new Scanner(new File(INPUT_VECTOR_FILENAME));
		_scanner.useDelimiter(",");

		while (_scanner.hasNext()) {
			inputVector_.add(Double.parseDouble(_scanner.next().trim()));
		}
	}

	private void ListifyWeights() throws FileNotFoundException
	{
		Scanner _lineScanner = new Scanner(new File(WEIGHT_FILENAME));
		_lineScanner.useDelimiter("\n");
		NUM_INTERNAL_LAYERS = _lineScanner.nextInt();

		Scanner _tokenScanner;
		String _line;
		String _token;
		double _weight;

		while (_lineScanner.hasNext()) {
			_line = _lineScanner.next();
			_tokenScanner = new Scanner(_line).useDelimiter(",");

			List<Double> _edgeWeights = new ArrayList<>();

			while (_tokenScanner.hasNext()) {
				_token = _tokenScanner.next().trim();
				if (_token.equals("0")) {
					_edgeWeights.add(0.0);
				} else {
					_weight = Double.parseDouble(_token);
					_edgeWeights.add(_weight);
				}
			}
			edgeWeights_.add(_edgeWeights);
		}
	}


	public void PrintInputVector()
	{
		for (var i : inputVector_) {
			System.out.print(i + "\t");
		}
		System.out.println();
	}
}
