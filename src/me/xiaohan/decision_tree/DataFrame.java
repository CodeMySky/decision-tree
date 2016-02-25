package me.xiaohan.decision_tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DataFrame {
	private List<Entity> m_data;
	
	public DataFrame(String filename) {
		m_data = new ArrayList<>();
		
		Scanner fin;
		try {
			fin = new Scanner(new File(filename));
			while (fin.hasNext()) {
				String inputLine = fin.nextLine();
				String[] items = inputLine.split(",");
				Entity temp = new Entity();
				for (int i = 0; i < items.length - 1; i++) {
					temp.addFeature(Double.valueOf(items[i]));
				}
				temp.setLabel(items[items.length - 1]);
				this.append(temp);
			}
			fin.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DataFrame() {
		m_data = new ArrayList<>();
	}
	
	public Entity getEntity(int index) {
		return m_data.get(index);
	}
	
	public List<DataFrame> split(int splitPoint) {
		DataFrame part0 = new DataFrame();
		DataFrame part1 = new DataFrame();
		for (int i = 0; i < m_data.size(); i++) {
			if (i <= splitPoint) {
				part0.append(m_data.get(i));
			} else {
				part1.append(m_data.get(i));
			}
		}
		List<DataFrame> list = new ArrayList<>();
		list.add(part0);
		list.add(part1);
		return list;
	}
	
	public List<Double> getFeatureList(int feature) {
		List<Double> featureList = new ArrayList<>();
		for (Entity e : m_data) {
			featureList.add(e.getFeature(feature));
		}
		return featureList;
	}
	
	public void append(Entity row) {
		m_data.add(row);	
	}
	
	public int getFeatureNo() {
		return m_data.get(0).getFeatureNo();
	}
	public int getEntityCount() {
		return m_data.size();
	}
	
	public Map<String, Integer> getLabelCount() {
		Map<String, Integer> labelCount = new HashMap<>();
		for (Entity e: m_data) {
			Integer previousValue = labelCount.get(e.getLabel());
			labelCount.put(e.getLabel(), previousValue == null ? 1 : previousValue + 1);
		}
		return labelCount;
	}
	
	public DataFrame sortBy(int index){
		Collections.sort(m_data, new SortEntity(index));
		return this;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		for (Entity e: m_data) {
			sb.append(e);
			sb.append(",\n");
		}
		sb.append("]");
		return sb.toString();
	}
}
