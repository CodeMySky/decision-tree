package me.xiaohan.decision_tree;

import java.util.ArrayList;
import java.util.List;

public class Entity {
	List<Double> m_data;
	String m_label;
	public Entity(){
		m_data = new ArrayList<>();
	}
	
	public Entity addFeature(double value) {
		m_data.add(value);
		return this;
	}
	
	public Entity setLabel(String label) {
		m_label = label;
		return this;
	}
	
	public int getFeatureNo() {
		return m_data.size();
	}
	
	public double getFeature(int feature) {
		return m_data.get(feature);
	}
	
	public String getLabel() {
		return m_label;
	}
	
	@Override
	public String toString(){
		return m_data + "-" + m_label;
	}
}
