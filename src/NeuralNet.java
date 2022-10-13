import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class NeuralNet implements Runnable
{
	int numberOfLayers_;
	int INDEX_OF_MAX_OUTPUT;

	List<Double> inputVector_;
	List<NeuralLayer> neuralLayers_;

	CyclicBarrier cyclicBarrier;

	public NeuralNet(int numberOfInternalLayers, List<Double> inputVector, List<NeuralLayer> neuralLayers)
	{
		this.numberOfLayers_ = numberOfInternalLayers + 2;
		this.inputVector_ = inputVector;
		this.neuralLayers_ = neuralLayers;
		cyclicBarrier = new CyclicBarrier(this.numberOfLayers_);
	}


	@Override public void run()
	{
		neuralLayers_.get(0).inputValues_ = this.inputVector_;

		for (int L = 0; L < neuralLayers_.size(); L++) {
			// neuralLayers_.get(L).cyclicBarrier = this.cyclicBarrier;
			if (neuralLayers_.get(L).layerType_ != NeuralLayer.LayerType.OUTPUT) {
				neuralLayers_.get(L).nextLayerRef_ = neuralLayers_.get(L + 1);
			}
		}

		neuralLayers_.get(0).run();

	}
}
