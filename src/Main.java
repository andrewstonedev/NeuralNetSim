import java.io.IOException;

public class Main
{
	public static void main(String[] args) throws InterruptedException, IOException
	{

		NeuralNetSimulator NNS = new NeuralNetSimulator();
		NNS.CreateNeuralNet();
		NNS.RunSimulation();


	}

}
