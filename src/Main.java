import java.io.IOException;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		NeuralNetSimulator NNS = new NeuralNetSimulator();
		NNS.CreateNeuralNet();
		NNS.RunSimulation();
	}
}
