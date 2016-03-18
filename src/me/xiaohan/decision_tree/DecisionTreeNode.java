package me.xiaohan.decision_tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecisionTreeNode {
	private transient DataFrame m_data;
	private double m_threshold;
	private int m_decisionFeature;
	private DecisionTreeNode m_leftChild;
	private DecisionTreeNode m_rightChild;
	private int m_depth;
	private String m_decision;
	private boolean m_isLeft;
	
	public DecisionTreeNode(DataFrame data) {
		m_data = data;
		m_depth = 0;
	}
	
	public DecisionTreeNode(DataFrame data, int depth, boolean isLeft) {
		m_data = data;
		m_depth = depth;
		m_isLeft = isLeft;
	}
	
	
	public void train() {
		int nFeature = m_data.getFeatureNo();
		Map<String, Integer> originalCount = m_data.getLabelCount();
//		System.out.println(originalCount);
		double originalEntropy = Probability.entropy(originalCount.values());
		
		double maxInfoGain = 0;
		int splitPoint = -1;
		//Search for threshold among features
		for (int ithFeature = 0; ithFeature < nFeature; ithFeature++) {
			// Reset count
			Map<String, Integer> countPart1 = new HashMap<>();
			Map<String, Integer> countPart2 = new HashMap<>();
			for (String key: originalCount.keySet()) {
				countPart2.put(key, originalCount.get(key));
			}
			m_data.sortBy(ithFeature);
			
			// Search for threshold in this data set
			for (int jthEntity = 0; jthEntity < m_data.getEntityCount() - 1; jthEntity++) {
				Entity currentEntity = m_data.getEntity(jthEntity);
				Entity nextEntity = m_data.getEntity(jthEntity+1);
				double currentFeature = currentEntity.getFeature(ithFeature);
				String currentLabel = currentEntity.getLabel();
				double nextFeature = nextEntity.getFeature(ithFeature);
				
				// Update count
				Integer count = countPart1.get(currentLabel);
				countPart1.put(currentLabel, count == null? 1: count+1);
				countPart2.put(currentLabel, countPart2.get(currentLabel) - 1);
				
				if (currentFeature != nextFeature){
					Double newEntropy  = Probability.entropy(countPart1.values(), countPart2.values());
					Double infoGain = originalEntropy - newEntropy;
					if (infoGain > maxInfoGain) {
						maxInfoGain = infoGain;
						m_decisionFeature = ithFeature;
						splitPoint = jthEntity;
						m_threshold = (currentEntity.getFeature(ithFeature) + nextEntity.getFeature(ithFeature)) / 2.0;
					}
				}
			}
		}
		if (splitPoint == -1) {
			m_decision = m_data.getEntity(0).getLabel();
//			System.out.println("Pure node: " + m_decision);
//			System.out.println(m_data);
		} else {
			System.out.println(String.format("%d, %f, %f", m_decisionFeature, m_threshold, maxInfoGain));
			m_data.sortBy(m_decisionFeature);
			List<DataFrame> chunks =  m_data.split(splitPoint);
			m_leftChild = new DecisionTreeNode(chunks.get(0), m_depth+1, true);
			m_leftChild.train();
			m_rightChild = new DecisionTreeNode(chunks.get(1), m_depth+1, false);
			m_rightChild.train();
		}
	}
	
	public String test(Entity e) {
		if (m_decision != null) {
			return m_decision;
		} else if (e.getFeature(m_decisionFeature) <= m_threshold) {
			return m_leftChild.test(e);
		} else {
			return m_rightChild.test(e);
		}
	}
	
	public List<String> test(DataFrame df) {
		List<String> yHat = new ArrayList<>();
		for (int i = 0; i < df.getEntityCount();i++) {
			String prediction = test(df.getEntity(i));
			yHat.add(prediction);
			System.out.println(String.format("%s:%s", prediction, df.getEntity(i).getLabel()));
		}
		return yHat;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i=0;i< m_depth-1;i++) {
			sb.append(' ');
		}
		if (m_depth > 0) {
			sb.append("└" + (m_isLeft?"<":">"));
		}
		
		if (m_leftChild == null && m_rightChild == null) {
			sb.append(m_decision + "\n");
		} else {
			sb.append(String.format("Feature %d split on %f \n", m_decisionFeature, m_threshold));
			sb.append(m_leftChild.toString());
			sb.append(m_rightChild.toString());
		}
		return sb.toString();
	}
}
