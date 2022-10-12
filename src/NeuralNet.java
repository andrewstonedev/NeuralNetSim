import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class NeuralNet implements Runnable
{
	int numberOfLayers_;
	int INDEX_OF_MAX_OUTPUT;

	List<Double> inputVector_;
	List<NeuralLayer> neuralLayers_;

	CyclicBarrier cyclicBarrier = new CyclicBarrier(numberOfLayers_);

	public NeuralNet(int numberOfInternalLayers, List<Double> inputVector, List<NeuralLayer> neuralLayers)
	{
		this.numberOfLayers_ = numberOfInternalLayers + 2;
		this.inputVector_ = inputVector;
		this.neuralLayers_ = neuralLayers;
	}

	@Override public void run()
	{
		neuralLayers_.get(0).inputValues_ = this.inputVector_;
		neuralLayers_.get(0).run();

		for (var _layer : neuralLayers_) {
			_layer.cyclicBarrier = this.cyclicBarrier;
		}

	}
}
