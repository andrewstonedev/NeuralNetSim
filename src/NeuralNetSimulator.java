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

	public List<Double> INPUT_VECTOR = new ArrayList<>();

	public List<Neuron> INPUT_LAYER_NEURONS = new ArrayList<>();

	public List<Neuron> INTERNAL_LAYER_NEURONS = new ArrayList<>();


	public void ListifyInputVector() throws FileNotFoundException
	{
		Scanner _scanner = new Scanner(new File(INPUT_VECTOR_FILENAME));
		_scanner.useDelimiter(",");

		while (_scanner.hasNext()) {
			INPUT_VECTOR.add(Double.parseDouble(_scanner.next().trim()));
		}
	}

	public void ListifyNeurons() throws FileNotFoundException
	{
		Scanner _lineScanner = new Scanner(new File(WEIGHT_FILENAME));
		_lineScanner.useDelimiter("\n");
		NUM_INTERNAL_LAYERS = _lineScanner.nextInt();

		Scanner _tokenScanner;
		String _line;
		String _token;
		double _weight;

		int neuron_index = 1;
		int layer_index = 0;

		while (_lineScanner.hasNext()) {
			_line = _lineScanner.next();
			ArrayList<Double> neuronWeights = new ArrayList<>();

			_tokenScanner = new Scanner(_line).useDelimiter(",");


			while (_tokenScanner.hasNext()) {
				_token = _tokenScanner.next().trim();
				if (_token.equals("0")) {
					neuronWeights.add(0.0);
				} else {
					_weight = Double.parseDouble(_token);
					neuronWeights.add(_weight);
				}
			}

			if (layer_index == 0) {
				INPUT_LAYER_NEURONS.add(new Neuron(layer_index, neuron_index, neuronWeights));
				neuron_index++;
				if (neuron_index > 4) {
					layer_index++;
					neuron_index = 1;
				}
			} else {

				INTERNAL_LAYER_NEURONS.add(new Neuron(layer_index, neuron_index, neuronWeights));

				neuron_index++;
				if (neuron_index > 5) {
					layer_index++;
					neuron_index = 1;
				}
			}

		}
	}
	

	public void PrintInternalNeurons()
	{
		for (var w : INTERNAL_LAYER_NEURONS) {
			System.out.println("Layer-" + w.LAYER_INDEX + ", Neuron-" + w.NEURON_INDEX);
			System.out.println(w.WEIGHTS_LIST);
			System.out.println("------------------------");
		}
	}

	public void PrintInputLayer()
	{
		for (var i : INPUT_LAYER_NEURONS) {
			System.out.println("Layer-" + i.LAYER_INDEX + ", Neuron-" + i.NEURON_INDEX);
			System.out.println(i.WEIGHTS_LIST);
			System.out.println("------------------------");
		}
	}

	public void PrintInputVectorList()
	{
		for (var i : INPUT_VECTOR) {
			System.out.print(i + "\t");
		}
		System.out.println();
	}
}
