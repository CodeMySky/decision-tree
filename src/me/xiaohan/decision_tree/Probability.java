package me.xiaohan.decision_tree;
import java.lang.Math;
import java.util.Collection;

public class Probability {
	public static double entropy(Collection<Integer> counts) {
		int sum = counts.stream().mapToInt(Integer::intValue).sum();
		double entropy = 0;
		for (Integer count: counts) {
			if (count > 0) {
				double p = count.doubleValue()/sum;
				entropy -= p * Math.log(p);
			}
		}
		return entropy;
	}
	
	public static double entropy(Collection<Integer> part1, Collection<Integer> part2) {
		int sumPart1 = part1.stream().mapToInt(Integer::intValue).sum();
		int sumPart2 = part2.stream().mapToInt(Integer::intValue).sum();
		int sum = sumPart1 + sumPart2;
		return sumPart1 * entropy(part1) / sum  + sumPart2 * entropy(part2) /sum;
	}
}
