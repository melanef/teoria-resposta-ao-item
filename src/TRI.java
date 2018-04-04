import java.lang.Math;

public class TRI {
	static double chance(double theta, double ai, double bi)
	{
		return Math.pow(Math.E, ai * (theta - bi)) / 1.0 + Math.pow(Math.E, ai * (theta - bi));
	}
}
