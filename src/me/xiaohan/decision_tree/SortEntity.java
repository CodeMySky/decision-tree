package me.xiaohan.decision_tree;
import java.util.Comparator;

public class SortEntity implements Comparator<Entity> {
	int m_idx;
	
	public SortEntity(int idx){
		m_idx = idx;
	}
	
	@Override
	public int compare(Entity o1, Entity o2) {
		double f1 = o1.getFeature(m_idx);
		double f2 = o2.getFeature(m_idx);
		if (f1 < f2) return -1;
		if (f1 > f2) return 1;
		return o1.getLabel().compareTo(o2.getLabel());
	}

}
